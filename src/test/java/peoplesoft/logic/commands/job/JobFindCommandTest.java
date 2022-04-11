package peoplesoft.logic.commands.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.JobContainsKeywordsPredicate;
import peoplesoft.model.util.ID;

public class JobFindCommandTest {
    private static final String TEST_ID = "test";
    private static final Job JOB = new Job(new ID(TEST_ID), "The Right Job", Duration.ofHours(2));

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        model.addJob(JOB);
        expectedModel.addJob(JOB);
        String expectedMessage = String.format(Messages.MSG_JOBS_LISTED_OVERVIEW, 0);
        JobContainsKeywordsPredicate predicate = preparePredicate(" ");
        JobFindCommand command = new JobFindCommand(predicate);
        expectedModel.updateFilteredJobList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredJobList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        model.addJob(JOB);
        expectedModel.addJob(JOB);
        String expectedMessage = String.format(Messages.MSG_JOBS_LISTED_OVERVIEW, 1);
        JobContainsKeywordsPredicate predicate = preparePredicate("right");
        JobFindCommand command = new JobFindCommand(predicate);
        expectedModel.updateFilteredJobList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(JOB), model.getFilteredJobList());
    }

    /**
     * Parses {@code userInput} into a {@code JobontainsKeywordsPredicate}.
     * @return
     */
    private JobContainsKeywordsPredicate preparePredicate(String userInput) {
        return new JobContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
