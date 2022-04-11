package peoplesoft.logic.commands.job;

import static peoplesoft.logic.commands.CommandTestUtil.assertCommandFailure;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.Payment;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

public class JobMarkCommandTest {

    private static final String TEST_ID = "test";
    private static final Job UNPAID = new Job(new ID(TEST_ID), "The Right Job", Duration.ofHours(2))
            .setAsNotPaid();
    private static final Job PAID = new Job(new ID(TEST_ID), "The Right Job", Duration.ofHours(2))
            .setAsPaid();

    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(null, new Employment()));
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JobMarkCommand(Index.fromOneBased(1), new Employment())
                .execute(null));
    }

    @Test
    public void execute_incorrectArgs_throwsCommandException() {
        // No job at index 3
        JobMarkCommand cmd = new JobMarkCommand(Index.fromOneBased(3), new Employment());
        assertCommandFailure(cmd, expectedModel, Messages.MSG_INVALID_JOB_DISPLAYED_IDX);
    }

    @Test
    public void execute_correctArgs_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Employment emp = new Employment();
        model.addJob(UNPAID);
        emp.associate(UNPAID, ALICE); // Alice is guaranteed to be in model.

        // Mark as paid
        Person aliceToo = new Person(ALICE.getPersonId(), ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getRate(), ALICE.getTags(), Map.of(UNPAID.getJobId(),
                Payment.createPayment(ALICE, UNPAID, UNPAID.calculatePay(ALICE.getRate()))));
        expectedModel.addJob(PAID);
        expectedModel.setPerson(ALICE, aliceToo);
        JobMarkCommand cmd = new JobMarkCommand(Index.fromOneBased(1), emp);
        assertCommandSuccess(cmd, model, String.format(JobMarkCommand.MESSAGE_SUCCESS, PAID.getDesc(),
                JobMarkCommand.MARK_PART), expectedModel);

        // Mark as not paid
        expectedModel.setJob(PAID, UNPAID);
        expectedModel.setPerson(aliceToo, ALICE);
        cmd = new JobMarkCommand(Index.fromOneBased(1), emp);
        assertCommandSuccess(cmd, model, String.format(JobMarkCommand.MESSAGE_SUCCESS, UNPAID.getDesc(),
                JobMarkCommand.UNMARK_PART), expectedModel);
    }
}
