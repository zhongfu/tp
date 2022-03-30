package peoplesoft.logic.commands.job;

import static peoplesoft.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.index.Index;
import peoplesoft.model.employment.Employment;

public class JobAssignCommandTest {
    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(null, Set.of(),
                new Employment()));
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(Index.fromOneBased(1), null,
                new Employment()));
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(Index.fromOneBased(1), Set.of(),
                null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobAssignCommand(Index.fromOneBased(1), Set.of(),
                new Employment()).execute(null));
    }

    // TODO: Add model stubs to test command.
    // Perhaps a common model stub class can be made.
}
