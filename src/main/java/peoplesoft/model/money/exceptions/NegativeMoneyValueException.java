package peoplesoft.model.money.exceptions;

/**
 * Signals that the operation will result in money with negative value.
 */
public class NegativeMoneyValueException extends RuntimeException {
    public NegativeMoneyValueException() {
        super("Operation would result in money with negative value");
    }
}
