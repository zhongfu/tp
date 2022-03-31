package peoplesoft.model.job.util;

import peoplesoft.model.money.Money;
import peoplesoft.model.money.exceptions.NegativeMoneyValueException;

/**
 * Contains utility method for validating the value of {@code Money}.
 */
public class MoneyUtil {
    /**
     * Throws NegativeMoneyValueException if {@code money} is negative.
     */
    public static void requireNonNegative(Money money) {
        if (money.getValue().signum() < 0) {
            throw new NegativeMoneyValueException();
        }
    }
}
