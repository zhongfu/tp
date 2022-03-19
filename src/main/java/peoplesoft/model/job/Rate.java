package peoplesoft.model.job;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;
import static peoplesoft.model.job.Money.requireNonNegative;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

import peoplesoft.model.job.exceptions.NonPositiveDurationException;

/**
 * Represents a rate of payment, e.g. $5 per hour. Immutable.
 */
public class Rate {

    public final Money amount;
    public final Duration duration;

    /**
     * Constructs a {@code Rate} instance.
     *
     * @param amount Money per unit time
     * @param duration Unit time duration
     */
    public Rate(Money amount, Duration duration) {
        requireAllNonNull(amount, duration);
        requireNonNegative(amount);
        requirePositive(duration);
        this.amount = amount;
        this.duration = duration;
    }

    /**
     * Throws NonPositiveDurationException if {@code duration} is not positive.
     */
    private void requirePositive(Duration duration) {
        if (duration.compareTo(Duration.ofHours(0)) <= 0) {
            throw new NonPositiveDurationException();
        }
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getDuration() {
        return duration;
    }

    /**
     * Calculates the resulting amount of {@code Money} from multiplying this rate by the given
     * {@code Duration}.
     *
     * @param totalDuration the duration to multiply this rate by
     * @return the resulting amount of {@code Money} at this rate for the given duration
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
        // TODO make it more user friendly, e.g. $5 / hour
        return String.format("%s / %s", amount, duration);
    }
}
