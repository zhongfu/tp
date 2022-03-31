package peoplesoft.logic.commands.job;

import static peoplesoft.testutil.Assert.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.model.job.Job;
import peoplesoft.model.util.ID;

public class JobAddCommandTest {

    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofHours(1));

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAddCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAddCommand(EATING).execute(null));
    }

    // TODO: Add model stubs to test command.
    // Perhaps a common model stub class can be made.
}
