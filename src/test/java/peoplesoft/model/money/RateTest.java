package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.testutil.Assert.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.model.job.exceptions.NonPositiveDurationException;
import peoplesoft.model.money.exceptions.NegativeMoneyValueException;

public class RateTest {

    @Test
    public void rate() {
        assertEquals(new Rate(new Money(5.0), Duration.ofHours(1)),
                new Rate(new Money(5.0), Duration.ofHours(1)));
        assertEquals(new Rate(new Money(0), Duration.ofHours(1)),
                new Rate(new Money(0), Duration.ofHours(1)));
    }

    @Test
    public void rate_addNegativeMoney_throwsNegativeMoneyValueException() {
        assertThrows(NegativeMoneyValueException.class, () -> new Rate(new Money(-1.0), Duration.ofHours(1)));
    }

    @Test
    public void rate_addInvalidDuration_throwsNonPositiveDurationException() {
        // negative duration value -> throw NonPositiveDurationException
        assertThrows(NonPositiveDurationException.class, () -> new Rate(new Money(1.0), Duration.ofHours(-1)));

        // Duration.ZERO -> throw NonPositiveDurationException
        assertThrows(NonPositiveDurationException.class, () -> new Rate(new Money(1.0), Duration.ZERO));
    }
}
