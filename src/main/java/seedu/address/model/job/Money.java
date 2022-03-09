package seedu.address.model.job;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents some value of money. Immutable.
 */
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
        this(BigDecimal.valueOf(value).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
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
    public Money multiply(double multiplicand) {
        return new Money(value.multiply(BigDecimal.valueOf(multiplicand)).setScale(VALUE_SCALE, RoundingMode.HALF_UP));
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
    public Money divide(double divisor) {
        return new Money(value.divide(BigDecimal.valueOf(divisor), VALUE_SCALE, RoundingMode.HALF_UP));
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
        return other == this
                || (other instanceof Money
                && value.equals(((Money) other).value));
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
}
