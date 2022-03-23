package peoplesoft.model.job;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Objects;

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
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;

/**
 * Represents a rate of payment, e.g. $5 per hour. Immutable.
 */
@JsonSerialize(using = Rate.RateSerializer.class)
@JsonDeserialize(using = Rate.RateDeserializer.class)
public class Rate {

    public final Money amount;
    public final Duration duration;

    /**
     * Constructs a {@code Rate} instance.
     *
     * @param amount A value as a double.
     */
    public Rate(Money amount, Duration duration) {
        requireAllNonNull(amount, duration);
        this.amount = amount;
        this.duration = duration;
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getDuration() {
        return duration;
    }

    /**
     * Calculates the resulting amount of {@code Money} from multiplying this rate
     * by the given
     * {@code Duration}.
     *
     * @param totalDuration the duration to multiply this rate by
     * @return the resulting amount of {@code Money} at this rate for the given
     *         duration
     */
    public Money calculateAmount(Duration totalDuration) {
        return amount.multiply(BigDecimal.valueOf(totalDuration.dividedBy(duration)));

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Rate)) {
            return false;
        }

        Rate otherRate = (Rate) other;

        return amount.equals(otherRate.amount)
                && duration.equals(otherRate.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, duration);
    }

    /**
     * Prints the 2 decimal place currency format of the value.
     *
     * @returns Value in currency format as a string.
     */
    @Override
    public String toString() {
        // TODO make it more user friendly, e.g. $5 / hour
        return String.format("%s / %dH", amount, duration.toHours());
    }

    protected static class RateSerializer extends StdSerializer<Rate> {
        private RateSerializer(Class<Rate> val) {
            super(val);
        }

        private RateSerializer() {
            this(null);
        }

        @Override
        public void serialize(Rate val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeObjectField("amount", val.getAmount());
            gen.writeStringField("duration", val.getDuration().toString());

            gen.writeEndObject();
        }
    }

    protected static class RateDeserializer extends StdDeserializer<Rate> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The rate instance is invalid or missing!";
        private static final String MISSING_OR_INVALID_VALUE = "The rate's %s field is invalid or missing!";

        private RateDeserializer(Class<?> vc) {
            super(vc);
        }

        private RateDeserializer() {
            this(null);
        }

        private static JsonNode getNonNullNode(ObjectNode node, String key, DeserializationContext ctx)
                throws JsonMappingException {
            JsonNode jsonNode = node.get(key);
            if (jsonNode == null) {
                throw JsonUtil.getWrappedIllegalValueException(
                        ctx, String.format(MISSING_OR_INVALID_VALUE, key));
            }

            return jsonNode;
        }

        @Override
        public Rate deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode rate = (ObjectNode) node;

            Money amount = codec.treeToValue(
                getNonNullNode(rate, "amount", ctx), Money.class);

            String durationString = getNonNullNode(rate, "duration", ctx).textValue();
            Duration duration;
            try {
                duration = Duration.parse(durationString);
            } catch (NullPointerException | DateTimeParseException e) {
                throw JsonUtil.getWrappedIllegalValueException(
                    ctx, String.format(MISSING_OR_INVALID_VALUE, "duration"), e);
            }

            return new Rate(amount, duration);
        }

        @Override
        public Rate getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
