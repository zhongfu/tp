package peoplesoft.model.employment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.BOB;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.model.employment.exceptions.DuplicateEmploymentException;
import peoplesoft.model.employment.exceptions.EmploymentNotFoundException;
import peoplesoft.model.job.Job;
import peoplesoft.model.util.ID;

public class EmploymentTest {
    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofDays(1));
    private static final Job RUNNING = new Job(new ID(3175), "Running", Duration.ofHours(8));

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Employment(null));
    }

    @Test
    public void associate_null_throwsNullPointerException() {
        // null job
        assertThrows(NullPointerException.class, () -> new Employment().associate(null, ALICE));

        // null person
        assertThrows(NullPointerException.class, () -> new Employment().associate(EATING, null));
    }

    @Test
    public void associate_withoutDuplicates_success() {
        Employment employment = new Employment();

        // Associate eating to Alice
        employment.associate(EATING, ALICE);
        assertTrue(employment.getAllJobs().containsKey(EATING.getJobId()));
        assertTrue(employment.getAllJobs().get(EATING.getJobId()).contains(ALICE.getPersonId()));

        // Associate eating to Bob
        employment.associate(EATING, BOB);
        assertTrue(employment.getAllJobs().get(EATING.getJobId()).contains(BOB.getPersonId()));

        // Associate running to Alice
        employment.associate(RUNNING, ALICE);
        assertTrue(employment.getAllJobs().containsKey(RUNNING.getJobId()));
        assertTrue(employment.getAllJobs().get(RUNNING.getJobId()).contains(ALICE.getPersonId()));

        // Running is not associated to Bob
        assertFalse(employment.getAllJobs().get(RUNNING.getJobId()).contains(BOB.getPersonId()));
    }

    @Test
    public void associate_duplicateEmployment_throwsDuplicateEmploymentException() {
        Employment employment = new Employment();
        employment.associate(EATING, ALICE);
        assertThrows(DuplicateEmploymentException.class, () -> employment.associate(EATING, ALICE));
    }

    @Test
    public void disassociate_null_throwsNullPointerException() {
        // null job
        assertThrows(NullPointerException.class, () -> new Employment().disassociate(null, ALICE));

        // null person
        assertThrows(NullPointerException.class, () -> new Employment().disassociate(EATING, null));
    }

    @Test
    public void disassociate_employmentExists_success() {
        Employment employment = new Employment();
        employment.associate(EATING, ALICE);
        employment.associate(EATING, BOB);
        employment.associate(RUNNING, ALICE);

        // Disassociate running from Alice
        employment.disassociate(RUNNING, ALICE);
        assertFalse(employment.getAllJobs().containsKey(RUNNING.getJobId()));

        // Disassociate eating from Bob
        employment.disassociate(EATING, BOB);
        assertTrue(employment.getAllJobs().containsKey(EATING.getJobId()));
        assertTrue(employment.getAllJobs().get(EATING.getJobId()).contains(ALICE.getPersonId()));
        assertFalse(employment.getAllJobs().get(EATING.getJobId()).contains(BOB.getPersonId()));

        // Disassociate eating from Alice
        employment.disassociate(EATING, ALICE);
        assertFalse(employment.getAllJobs().containsKey(EATING.getJobId()));
    }

    @Test
    public void disassociate_employmentMissing_throwsEmploymentNotFoundException() {
        Employment employment = new Employment();
        employment.associate(EATING, ALICE);

        // Job missing
        assertThrows(EmploymentNotFoundException.class, () -> employment.disassociate(RUNNING, ALICE));

        // Person missing
        assertThrows(EmploymentNotFoundException.class, () -> employment.disassociate(EATING, BOB));
    }

    @Test
    public void deletePerson_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Employment().deletePerson(null));
    }

    @Test
    public void deletePerson_success() {
        Employment employment = new Employment();
        employment.associate(EATING, ALICE);
        employment.associate(EATING, BOB);
        employment.associate(RUNNING, ALICE);

        // Delete Bob
        employment.deletePerson(BOB);
        assertTrue(employment.getAllJobs().containsKey(EATING.getJobId()));
        assertTrue(employment.getAllJobs().get(EATING.getJobId()).contains(ALICE.getPersonId()));
        assertFalse(employment.getAllJobs().get(EATING.getJobId()).contains(BOB.getPersonId()));

        // Delete Alice
        employment.deletePerson(ALICE);
        assertFalse(employment.getAllJobs().containsKey(EATING.getJobId()));
        assertFalse(employment.getAllJobs().containsKey(RUNNING.getJobId()));
    }

    @Test
    public void deleteJob_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Employment().deleteJob(null));
    }

    @Test
    public void deleteJob_success() {
        Employment employment = new Employment();
        employment.associate(EATING, ALICE);
        employment.associate(EATING, BOB);
        employment.associate(RUNNING, ALICE);

        // Delete eating
        employment.deleteJob(EATING);
        assertFalse(employment.getAllJobs().containsKey(EATING.getJobId()));
        assertTrue(employment.getAllJobs().containsKey(RUNNING.getJobId()));

        // Delete running
        employment.deleteJob(RUNNING);
        assertFalse(employment.getAllJobs().containsKey(RUNNING));
    }
}
