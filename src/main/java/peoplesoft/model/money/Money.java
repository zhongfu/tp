package peoplesoft.model.money;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;

/**
 * Represents some value of money. Immutable.
 */
@JsonSerialize(using = Money.MoneySerializer.class)
@JsonDeserialize(using = Money.MoneyDeserializer.class)
public class Money {

    private static final int VALUE_SCALE = 6;
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public final BigDecimal value;

    /**
     * Constructs a {@code Money}.
     *
     * @param value A value as a double.
     */
    public Money(double value) {
        this(BigDecimal.valueOf(value));
    }

    /**
     * Constructs a {@code Money}.
     *
     * @param value A value as a BigDecimal.
     */
    public Money(BigDecimal value) {
        requireNonNull(value);
        CURRENCY_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        this.value = value.setScale(VALUE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Returns true if a given string is a valid money string.
     *
     * TODO: should we accept strings with {@code $}, {@code ,}, etc?
     */
    public static boolean isValidMoneyString(String moneyString) {
        try {
            new BigDecimal(moneyString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns a {@code Money} with the value equal the sum of both values.
     *
     * @param augend Value to add.
     * @return Sum.
     */
    public Money add(Money augend) {
        return new Money(value.add(augend.value).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the sum of both values.
     *
     * @param augend Value to add.
     * @return Sum.
     */
    public Money add(BigDecimal augend) {
        return new Money(value.add(augend).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the difference of the second value from the first.
     *
     * @param augend Value to subtract.
     * @return Difference.
     */
    public Money subtract(Money augend) {
        return new Money(value.add(augend.value.negate()).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the product of both values.
     *
     * @param multiplicand Value to multiply.
     * @return Product.
     */
    public Money multiply(BigDecimal multiplicand) {
        return new Money(value.multiply(multiplicand).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the product of both values.
     *
     * @param multiplicand Value to multiply.
     * @return Product.
     */
    public Money multiply(Money multiplicand) {
        return new Money(value.multiply(multiplicand.value).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the quotient of the first value over the second.
     *
     * @param divisor Value to divide.
     * @return Quotient.
     */
    public Money divide(BigDecimal divisor) {
        return new Money(value.divide(divisor, VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Returns a {@code Money} with the value equal the quotient of the first value over the second.
     *
     * @param divisor Value to divide.
     * @return Quotient.
     */
    public Money divide(Money divisor) {
        return new Money(value.divide(divisor.value, VALUE_SCALE, RoundingMode.HALF_UP));
    }

    /**
     * Prints the 6 decimal place representation of the value.
     *
     * @return Value as a string.
     */
    public String printFullValue() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Money // instanceof handles nulls
                && value.equals(((Money) other).value)); // state check
    }

    @Override
    public int hashCode() {
        // Might change if scale not matching is an issue
        return value.hashCode();
    }

    /**
     * Prints the 2 decimal place currency format of the value.
     *
     * @returns Value in currency format as a string.
     */
    @Override
    public String toString() {
        // Might change if scale not matching is an issue
        return CURRENCY_FORMAT.format(value);
    }

    protected static class MoneySerializer extends StdSerializer<Money> {
        private MoneySerializer(Class<Money> val) {
            super(val);
        }

        private MoneySerializer() {
            this(null);
        }

        @Override
        public void serialize(Money val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(val.value.toString()); // to preserve precision
        }
    }

    protected static class MoneyDeserializer extends StdDeserializer<Money> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The money value is invalid or missing!";

        private MoneyDeserializer(Class<?> vc) {
            super(vc);
        }

        private MoneyDeserializer() {
            this(null);
        }

        @Override
        public Money deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();

            if (!(node instanceof TextNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            String valString = ((TextNode) node).textValue();

            if (!Money.isValidMoneyString(valString)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            return new Money(new BigDecimal(valString));
        }

        @Override
        public Money getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
