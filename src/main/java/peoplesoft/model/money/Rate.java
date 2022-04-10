package peoplesoft.model.money;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;
import static peoplesoft.model.job.util.DurationUtil.requirePositive;
import static peoplesoft.model.job.util.MoneyUtil.requireNonNegative;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.format.DateTimeParseException;
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
import peoplesoft.model.money.exceptions.NegativeMoneyValueException;

/**
 * Represents a rate of payment, e.g. $5 per hour. Immutable.
 */
@JsonSerialize(using = Rate.RateSerializer.class)
@JsonDeserialize(using = Rate.RateDeserializer.class)
public class Rate {
    public static final String MESSAGE_CONSTRAINTS = "The rate should not be negative or "
            + "have more than 2 decimal places.";
    public static final String MESSAGE_TOO_LARGE = "The value for the rate is too large. "
            + "Are you sure you need to pay this employee more than $1000000 per hour?";

    public final Money amount;
    public final Duration duration;

    /**
     * Constructs a {@code Rate} instance.
     *
     * @param amount Money per unit time.
     * @param duration Unit time duration.
     * @throws NegativeMoneyValueException Throws when money is negative.
     */
    public Rate(Money amount, Duration duration) throws NegativeMoneyValueException {
        requireAllNonNull(amount, duration);
        requireNonNegative(amount);
        requirePositive(duration);
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
     * @return Value in currency format as a string.
     */
    @Override
    public String toString() {
        BigDecimal value = amount.getValue();
        float hours = duration.toHours();
        BigDecimal perHour = value.divide(BigDecimal.valueOf(hours), RoundingMode.HALF_UP);

        // @@author Sergey Vyacheslavovich Brunov for converting big decimal to 2dp
        // retrieved from https://stackoverflow.com/a/10457320/16777554
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        String result = df.format(perHour);

        return String.format("$%s/h", result);
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
        private static final UnaryOperator<String> INVALID_VAL_FMTR =
                k -> String.format("This rate's %s value is invalid!", k);

        private RateDeserializer(Class<?> vc) {
            super(vc);
        }

        private RateDeserializer() {
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
        public Rate deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode rate = (ObjectNode) node;

            Money amount = getNonNullNode(rate, "amount", ctx)
                    .traverse(codec)
                    .readValueAs(Money.class);

            String durationString = getNonNullNodeWithType(rate, "duration", ctx, TextNode.class)
                    .textValue();

            Duration duration;
            try {
                duration = Duration.parse(durationString);
            } catch (NullPointerException | DateTimeParseException e) {
                throw JsonUtil.getWrappedIllegalValueException(
                    ctx, INVALID_VAL_FMTR.apply("duration"), e);
            }

            return new Rate(amount, duration);
        }

        @Override
        public Rate getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
