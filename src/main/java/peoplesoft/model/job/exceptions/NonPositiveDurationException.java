package peoplesoft.model.job.exceptions;

/**
 * Signals that the operation will result in non-positive duration.
 */
public class NonPositiveDurationException extends RuntimeException {
    public NonPositiveDurationException() {
        super("Operation would result in duration with non-positive value");
    }
}
