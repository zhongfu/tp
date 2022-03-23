package peoplesoft.logic.commands.job;

import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.parser.exceptions.ParseException;

public class JobAssignCommandTest {

    private static final String CORRECT_ARGS = " n/correct i/1";

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(CORRECT_ARGS).execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() {
        // TODO
    }

    // TODO: Add model stubs to test command.
    // Perhaps a common model stub class can be made.

    // TODO: May shift to parser depending on implementation
    @Test
    public void constructor_wrongFormatArgs_throwsParseException() {
        // Empty name
        assertThrows(ParseException.class, () -> new JobAssignCommand(" i/1"));
        // Incorrect index parse
        assertThrows(ParseException.class, () -> new JobAssignCommand("n/correct i/0"));
    }
}
