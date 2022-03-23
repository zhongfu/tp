package peoplesoft.logic.commands.job;

import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobAddCommandTest {

    private static final String CORRECT_ARGS = " n/name r/1.0 d/3";

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAddCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAddCommand(CORRECT_ARGS).execute(null));
    }

    // TODO: Add model stubs to test command.
    // Perhaps a common model stub class can be made.
}
