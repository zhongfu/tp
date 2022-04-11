package peoplesoft.logic.commands.job;

import static peoplesoft.logic.commands.CommandTestUtil.assertCommandFailure;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.job.Job;
import peoplesoft.model.util.ID;

public class JobDeleteCommandTest {

    private static final String TEST_ID = "test";
    private static final Job JOB = new Job(new ID(TEST_ID), "The Right Job", Duration.ofHours(2));

    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobDeleteCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobDeleteCommand(Index.fromOneBased(1))
                .execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() {
        // No job at index 3
        JobDeleteCommand cmd = new JobDeleteCommand(Index.fromOneBased(3));
        assertCommandFailure(cmd, expectedModel, Messages.MSG_INVALID_JOB_DISPLAYED_IDX);
    }

    @Test
    public void execute_correctArgs_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addJob(JOB);
        JobDeleteCommand cmd = new JobDeleteCommand(Index.fromOneBased(1));
        assertCommandSuccess(cmd, model, String.format(JobDeleteCommand.MESSAGE_SUCCESS, JOB.getDesc()),
                expectedModel);
    }
}
