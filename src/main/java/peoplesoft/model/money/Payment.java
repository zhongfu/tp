package peoplesoft.model.money;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.exceptions.PaymentAlreadyPaidException;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * Represents a Payment for a Person for completing a Job.
 * Guarantees: details are present and not null, field values are validated, immutable.
 *
 * Note that there can only ever be one Payment for each person-job pairing; this will be the unique
 * identifier for Payment objects
 */
@JsonSerialize(using = Payment.PaymentSerializer.class)
@JsonDeserialize(using = Payment.PaymentDeserializer.class)
public abstract class Payment {
    // Identity fields
    private final ID personId;
    private final ID jobId; // the job that resulted in the creation of this instance; should already be completed

    // Data fields
    private final Money amount;

    private Payment(ID personId, ID jobId, Money amount) {
        requireAllNonNull(personId, jobId, amount);

        this.personId = personId;
        this.jobId = jobId;
        this.amount = amount;
    }

    private Payment(Payment payment) {
        requireNonNull(payment);
        assert payment.personId != null
            && payment.jobId != null
            && payment.amount != null
            : "Payment fields should not be null!";

        this.personId = payment.personId;
        this.jobId = payment.jobId;
        this.amount = payment.amount;
    }

    /**
     * Creates a {@code PendingPayment} object.
     *
     * @param person Person.
     * @param job Job.
     * @param amount Amount to pay.
     * @return
     */
    // TODO: not sure if should be public, but currently public for testing
    public static Payment createPayment(Person person, Job job, Money amount) {
        requireAllNonNull(person, job, amount);
        return new PendingPayment(person.getPersonId(), job.getJobId(), amount);
    }

    public ID getPersonId() {
        return personId;
    }

    public ID getJobId() {
        return jobId;
    }

    public Money getAmount() {
        return amount;
    }

    /**
     * Returns true if both payments have the person and job fields.
     * This defines a weaker notion of equality between two payments.
     */
    public boolean isSamePayment(Payment otherPayment) {
        if (otherPayment == this) {
            return true;
        }

        return otherPayment != null
                && getPersonId().equals(otherPayment.getPersonId())
                && getJobId().equals(otherPayment.getJobId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Payment)) {
            return false;
        }

        Payment p = (Payment) o;
        return isSamePayment(p) && getAmount().equals(p.getAmount());
    }

    @Override
    public String toString() {
        return String.format(
            "Payment for: %s; for job: %s; amount: %s",
            personId, jobId, amount);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(personId, jobId, amount);
    }

    public abstract boolean isCompleted();
    public abstract Payment pay();

    private static class CompletedPayment extends Payment {
        private CompletedPayment(Payment payment) {
            super(payment);
        }

        // this one's only used for deser
        private CompletedPayment(ID personId, ID jobId, Money money) {
            super(personId, jobId, money);
        }

        @Override
        public boolean isCompleted() {
            return true;
        }

        @Override
        public Payment pay() {
            throw new PaymentAlreadyPaidException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            return o instanceof CompletedPayment && super.equals(o);
        }

        @Override
        public String toString() {
            return super.toString() + "; paid: yes";
        }
    }

    private static class PendingPayment extends Payment {
        private PendingPayment(ID personId, ID jobId, Money money) {
            super(personId, jobId, money);
        }

        // PendingPayment(Payment) not included as we probably won't be creating instances that way

        @Override
        public boolean isCompleted() {
            return false;
        }

        @Override
        public Payment pay() {
            return new CompletedPayment(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            return o instanceof PendingPayment && super.equals(o);
        }

        @Override
        public String toString() {
            return super.toString() + "; paid: no";
        }
    }

    protected static class PaymentSerializer extends StdSerializer<Payment> {
        private PaymentSerializer(Class<Payment> val) {
            super(val);
        }

        private PaymentSerializer() {
            this(null);
        }

        @Override
        public void serialize(Payment val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            String state = val instanceof PendingPayment
                    ? "PENDING"
                    : val instanceof CompletedPayment
                            ? "COMPLETED"
                            : null;

            if (state == null) {
                throw new IllegalStateException("Unknown payment state!");
            }

            gen.writeStringField("state", state);
            // personId association is implicit (since Payment objs are contained within Persons)
            gen.writeObjectField("jobId", val.getJobId());
            gen.writeObjectField("amount", val.getAmount());

            gen.writeEndObject();
        }
    }

    protected static class PaymentDeserializer extends StdDeserializer<Payment> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The payment instance is invalid or missing!";
        private static final UnaryOperator<String> INVALID_VAL_FMTR =
                k -> String.format("This payment's %s value is invalid!", k);

        private PaymentDeserializer(Class<?> vc) {
            super(vc);
        }

        private PaymentDeserializer() {
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
        public Payment deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode payment = (ObjectNode) node;

            String state = getNonNullNodeWithType(payment, "state", ctx, TextNode.class)
                    .textValue();

            Object personIdAttr = ctx.getAttribute("personId");
            if (!(personIdAttr instanceof ID)) {
                // this would be a programming error, not a user error or w/e
                // TODO should we still throw a JsonMappingException to recover gracefully?
                throw new IllegalStateException("personId not present in deser ctx, or is of wrong type!");
            }

            ID personId = (ID) personIdAttr;

            ID jobId = getNonNullNode(payment, "jobId", ctx)
                    .traverse(codec)
                    .readValueAs(ID.class);

            Money amount = getNonNullNode(payment, "amount", ctx)
                    .traverse(codec)
                    .readValueAs(Money.class);

            switch (state) {
            case "PENDING":
                return new PendingPayment(personId, jobId, amount);
            case "COMPLETED":
                return new CompletedPayment(personId, jobId, amount);
            default:
                throw JsonUtil.getWrappedIllegalValueException(ctx, INVALID_VAL_FMTR.apply("state"));
            }
        }

        @Override
        public Payment getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
