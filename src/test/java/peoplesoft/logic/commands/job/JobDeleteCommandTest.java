package peoplesoft.logic.commands.job;

import static peoplesoft.logic.commands.CommandTestUtil.assertCommandFailure;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.Money;
import peoplesoft.model.job.Rate;

public class JobDeleteCommandTest {

    private static final String CORRECT_ARGS = "correct";
    private static final String INCORRECT_ARGS = "incorrect";

    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Job job = new Job(CORRECT_ARGS, "The Right Job",
            new Rate(new Money(1), Duration.ofHours(1)), Duration.ofHours(2), false);

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobDeleteCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobDeleteCommand(CORRECT_ARGS).execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() throws Exception {
        JobDeleteCommand cmd = new JobDeleteCommand(INCORRECT_ARGS);
        assertCommandFailure(cmd, expectedModel, JobDeleteCommand.MESSAGE_JOB_NOT_FOUND);
    }

    @Test
    public void execute_correctArgs_success() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addJob(job);
        JobDeleteCommand cmd = new JobDeleteCommand(CORRECT_ARGS);
        assertCommandSuccess(cmd, model, String.format(JobDeleteCommand.MESSAGE_SUCCESS, job.getJobId()),
                expectedModel);
    }
}
