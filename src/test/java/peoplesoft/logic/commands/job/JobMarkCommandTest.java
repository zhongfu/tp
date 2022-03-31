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
import peoplesoft.model.job.Money;
import peoplesoft.model.job.Rate;
import peoplesoft.model.util.ID;

public class JobMarkCommandTest {

    private static final String TEST_ID = "test";
    private static final Job UNPAID = new Job(new ID(TEST_ID), "The Right Job",
            new Rate(new Money(1), Duration.ofHours(1)), Duration.ofHours(2), false);
    private static final Job PAID = new Job(new ID(TEST_ID), "The Right Job",
            new Rate(new Money(1), Duration.ofHours(1)), Duration.ofHours(2), true);

    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(Index.fromOneBased(1))
                .execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() {
        // No job at index 3
        JobMarkCommand cmd = new JobMarkCommand(Index.fromOneBased(3));
        assertCommandFailure(cmd, expectedModel, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
    }

    @Test
    public void execute_correctArgs_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addJob(UNPAID);

        // Mark as paid
        expectedModel.addJob(PAID);
        JobMarkCommand cmd = new JobMarkCommand(Index.fromOneBased(1));
        assertCommandSuccess(cmd, model, String.format(JobMarkCommand.MESSAGE_SUCCESS, PAID.getDesc(), "paid"),
                expectedModel);

        // Mark as not paid
        expectedModel.setJob(PAID, UNPAID);
        cmd = new JobMarkCommand(Index.fromOneBased(1));
        assertCommandSuccess(cmd, model, String.format(JobMarkCommand.MESSAGE_SUCCESS, UNPAID.getDesc(), "not paid"),
                expectedModel);
    }
}
