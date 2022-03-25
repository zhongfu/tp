package peoplesoft.model.job.util;

import java.time.Duration;

import peoplesoft.model.job.exceptions.NonPositiveDurationException;

/**
 * Contains utility method for validating the value of {@code Duration}.
 */
public class DurationUtil {
    /**
     * Throws NonPositiveDurationException if {@code duration} is not positive.
     */
    public static void requirePositive(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            throw new NonPositiveDurationException();
        }
    }
}
