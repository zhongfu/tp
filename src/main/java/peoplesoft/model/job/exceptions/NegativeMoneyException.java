package peoplesoft.model.job.exceptions;

/**
 * Signals that the operation will result in negative rate.
 */
public class NegativeMoneyException extends RuntimeException {
    public NegativeMoneyException() {
        super("Operation would result in negative money");
    }
}
