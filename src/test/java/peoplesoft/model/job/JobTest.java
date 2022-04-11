package peoplesoft.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.model.money.Money;
import peoplesoft.model.money.Rate;
import peoplesoft.model.util.ID;
import peoplesoft.testutil.JobBuilder;

class JobTest {

    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofDays(1));
    private static final Job RUNNING = new Job(new ID(3175), "Running", Duration.ofHours(8));
    private static final Rate RATE_ONE = new Rate(new Money(1), Duration.ofHours(1));

    @Test
    public void isSameJob() {
        // null -> returns false
        assertFalse(EATING.isSameJob(null));

        // same object -> returns true
        assertTrue(EATING.isSameJob(EATING));

        // same id, all other attributes different -> returns true
        Job editedJob = new JobBuilder(EATING)
                .withDesc(RUNNING.getDesc())
                .withDuration(RUNNING.getDuration())
                .withHasPaid(RUNNING.hasPaid())
                .withIsFinal(RUNNING.isFinal())
                .build();
        assertTrue(EATING.isSameJob(editedJob));

        // different id, all other attributes same -> returns false
        editedJob = new JobBuilder(EATING)
                .withId(new ID(10000))
                .build();
        assertFalse(EATING.isSameJob(editedJob));
    }

    @Test
    public void calculatePay() {
        assertTrue(EATING.calculatePay(RATE_ONE).getValue().compareTo(BigDecimal.valueOf(24)) == 0);
        assertTrue(RUNNING.calculatePay(RATE_ONE).getValue().compareTo(BigDecimal.valueOf(8)) == 0);
    }

    @Test
    public void setAsPaid() {
        // paid -> returns true
        assertTrue(EATING.setAsPaid().hasPaid());
        assertTrue(RUNNING.setAsPaid().hasPaid());
    }

    @Test
    public void setAsNotPaid() {
        // not paid -> returns false
        assertFalse(EATING.setAsNotPaid().hasPaid());
        assertFalse(RUNNING.setAsNotPaid().hasPaid());
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(EATING.equals(EATING));

        // same values -> returns true
        assertTrue(EATING.equals(new Job(new ID(1043), "Eating", Duration.ofDays(1))));

        // null -> returns false
        assertFalse(EATING.equals(null));

        // another type -> returns false
        assertFalse(EATING.equals("Eating"));

        // another value -> returns false
        assertFalse(EATING.equals(RUNNING));
        assertFalse(EATING.equals(new Job(new ID(1044), "Eating", Duration.ofDays(1))));
        assertFalse(EATING.equals(new Job(new ID(1043), "Running", Duration.ofDays(1))));
        assertFalse(EATING.equals(new Job(new ID(1043), "Eating", Duration.ofDays(2))));
        assertFalse(EATING.equals(new Job(new ID(1043), "Eating", Duration.ofDays(1))
                .setAsPaid()));
        assertFalse(EATING.equals(new Job(new ID(1043), "Eating", Duration.ofDays(1))
                .setAsPaid().setAsFinal()));
    }
}
