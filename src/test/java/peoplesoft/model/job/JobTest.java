package peoplesoft.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.BOB;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import org.junit.jupiter.api.Test;

class JobTest {

    private static final Job EATING = new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(ALICE));
    private static final Job RUNNING = new Job("3175", "Running",
            new Rate(new Money(6), Duration.ofHours(4)), Duration.ofHours(8), true, Set.of(BOB));

    @Test
    public void getPersons_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> EATING.getPersons().remove(0));
    }

    @Test
    public void calculatePay() {
        assertTrue(EATING.calculatePay().getValue().compareTo(BigDecimal.valueOf(132)) == 0);
        assertTrue(RUNNING.calculatePay().getValue().compareTo(BigDecimal.valueOf(12)) == 0);
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
    public void testEquals() {
        // same object -> returns true
        assertTrue(EATING.equals(EATING));

        // same values -> returns true
        assertTrue(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(ALICE))));

        // null -> returns false
        assertFalse(EATING.equals(null));

        // another type -> returns false
        assertFalse(EATING.equals("Eating"));

        // another value -> returns false
        assertFalse(EATING.equals(RUNNING));
        assertFalse(EATING.equals(new Job("1044", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(2)), Duration.ofDays(1), false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Running",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(6), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(2), false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), true, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("1043", "Eating",
            new Rate(new Money(5.5), Duration.ofHours(1)), Duration.ofDays(1), false, Set.of(BOB))));
    }
}
