package peoplesoft.model.job;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.function.UnaryOperator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.util.ID;

/**
 * Represents a job. Immutable.
 */
@JsonSerialize(using = Job.JobSerializer.class)
@JsonDeserialize(using = Job.JobDeserializer.class)
public class Job {

    private final ID jobId;
    private final String desc;
    private final Rate rate;
    private final Duration duration;

    private final boolean hasPaid;

    /**
     * Constructor for an immutable job.
     * All fields must not be null.
     */
    public Job(ID jobId, String desc, Rate rate, Duration duration, boolean hasPaid) {
        requireAllNonNull(jobId, desc, rate, duration, hasPaid);
        this.jobId = jobId;
        this.desc = desc;
        this.rate = rate;
        this.duration = duration;
        this.hasPaid = hasPaid;
    }

    public ID getJobId() {
        return jobId;
    }

    public String getDesc() {
        return desc;
    }

    public Rate getRate() {
        return rate;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean hasPaid() {
        return hasPaid;
    }

    /**
     * Returns the pay of the job.
     * Calculated from rate and duration.
     *
     * @return Pay.
     */
    public Money calculatePay() {
        return rate.calculateAmount(duration);
    }

    /**
     * Returns a new instance of the job with isPaid as true;
     *
     * @return Paid job.
     */
    public Job setAsPaid() {
        return new Job(jobId, desc, rate, duration, true);
    }

    /**
     * Returns a new instance of the job with isPaid as false;
     *
     * @return Unpaid job.
     */
    public Job setAsNotPaid() {
        return new Job(jobId, desc, rate, duration, false);
    }

    /**
     * Returns true if both jobs have the same {@code jobId}.
     * This defines a weaker notion of equality between two jobs.
     *
     * @param other the other Job to compare against
     * @return true if both jobs have the same {@code jobId}
     */
    public boolean isSameJob(Job other) {
        if (other == this) {
            return true;
        }

        return other != null
            && other.getJobId().equals(getJobId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Job)) {
            return false;
        }
        Job otherJob = (Job) other;
        return otherJob.getJobId().equals(getJobId())
                && otherJob.getDesc().equals(getDesc())
                && otherJob.getRate().equals(getRate())
                && otherJob.getDuration().equals(getDuration())
                && otherJob.hasPaid() == hasPaid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, desc, rate, duration, hasPaid);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID: ")
            .append(getJobId())
            .append("; Name: ")
            .append(getDesc())
            .append("; Rate: ")
            .append(getRate())
            .append("; Duration: ")
            .append(getDuration().toHoursPart())
            .append("H")
            .append(getDuration().toMinutesPart())
            .append("M")
            .append("; Has paid: ")
            .append(hasPaid());

        return builder.toString();
    }

    protected static class JobSerializer extends StdSerializer<Job> {
        private JobSerializer(Class<Job> val) {
            super(val);
        }

        private JobSerializer() {
            this(null);
        }

        @Override
        public void serialize(Job value, JsonGenerator gen, SerializerProvider provider)throws IOException {
            gen.writeStartObject();

            gen.writeObjectField("jobId", value.getJobId());
            gen.writeStringField("desc", value.getDesc());
            gen.writeObjectField("rate", value.getRate());
            gen.writeObjectField("duration", value.getDuration());
            gen.writeBooleanField("hasPaid", value.hasPaid());

            gen.writeEndObject();
        }
    }

    protected static class JobDeserializer extends StdDeserializer<Job> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The job instance is invalid or missing!";
        private static final UnaryOperator<String> INVALID_VAL_FMTR =
            k -> String.format("This job's %s value is invalid!", k);

        private JobDeserializer(Class<?> vc) {
            super(vc);
        }

        private JobDeserializer() {
            this(null);
        }

        private static JsonNode getNonNullNode(ObjectNode node, String key, DeserializationContext ctx)
                throws JsonMappingException {
            return JsonUtil.getNonNullNode(node, key, ctx, INVALID_VAL_FMTR);
        }

        private static <T> T getNonNullNodeWithType(ObjectNode node, String key, DeserializationContext ctx,
                Class<T> cls) throws JsonMappingException {
            return JsonUtil.getNonNullNodeWithType(node, key, ctx,
                INVALID_VAL_FMTR, cls);
        }

        @Override
        public Job deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode job = (ObjectNode) node;

            ID jobId = getNonNullNode(job, "jobId", ctx)
                .traverse(codec)
                .readValueAs(ID.class);

            String desc = getNonNullNodeWithType(job, "desc", ctx, TextNode.class).textValue();

            Rate rate = getNonNullNode(job, "rate", ctx)
                .traverse(codec)
                .readValueAs(Rate.class);

            Duration duration = getNonNullNode(job, "duration", ctx)
                .traverse(codec)
                .readValueAs(Duration.class);

            Boolean hasPaid = getNonNullNodeWithType(job, "hasPaid", ctx, BooleanNode.class).booleanValue();

            return new Job(jobId, desc, rate, duration, hasPaid);
        }

        @Override
        public Job getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
