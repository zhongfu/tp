package seedu.address.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;

class JobTest {

    private static final Job EATING = new Job("Eating",
            new Money(5.5), 1, false, Set.of(ALICE));
    private static final Job RUNNING = new Job("Running",
            new Money(6), 2, true, Set.of(BOB));

    @Test
    public void getPersons_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> EATING.getPersons().remove(0));
    }

    @Test
    public void calculatePay() {
        assertTrue(EATING.calculatePay().getValue().compareTo(new BigDecimal(5.5)) == 0);
        assertTrue(RUNNING.calculatePay().getValue().compareTo(new BigDecimal(12)) == 0);
    }

    @Test
    public void setAsPaid() {
        // paid -> returns true
        assertTrue(EATING.setAsPaid().isPaid());
        assertTrue(RUNNING.setAsPaid().isPaid());
    }

    @Test
    public void setAsNotPaid() {
        // not paid -> returns false
        assertFalse(EATING.setAsNotPaid().isPaid());
        assertFalse(RUNNING.setAsNotPaid().isPaid());
    }

    @Test
    public void testEquals() {
        // same object -> returns true
        assertTrue(EATING.equals(EATING));

        // same values -> returns true
        assertTrue(EATING.equals(new Job("Eating",
                new Money(5.5), 1, false, Set.of(ALICE))));

        // null -> returns false
        assertFalse(EATING.equals(null));

        // another type -> returns false
        assertFalse(EATING.equals("Eating"));

        // another value -> returns false
        assertFalse(EATING.equals(RUNNING));
        assertFalse(EATING.equals(new Job("Running",
            new Money(5.5), 1, false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("Eating",
            new Money(6), 1, false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("Eating",
            new Money(5.5), 2, false, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("Eating",
            new Money(5.5), 1, true, Set.of(ALICE))));
        assertFalse(EATING.equals(new Job("Eating",
            new Money(5.5), 1, false, Set.of(BOB))));
    }
}
