package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static peoplesoft.testutil.Assert.assertThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.ModelStub;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.AddressBook;
import peoplesoft.model.ReadOnlyAddressBook;
import peoplesoft.model.job.Job;
import peoplesoft.model.util.ID;
import peoplesoft.testutil.JobBuilder;

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

    @Test
    public void execute_jobAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingJobAdded modelStub = new ModelStubAcceptingJobAdded();
        Job validJob = new JobBuilder().build();

        CommandResult commandResult = new JobAddCommand(validJob).execute(modelStub);

        assertEquals(String.format(JobAddCommand.MESSAGE_SUCCESS, validJob.toString()),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validJob), modelStub.jobsAdded);
    }

    @Test
    public void execute_duplicateJob_throwsCommandException() {
        Job validJob = new JobBuilder().build();
        JobAddCommand jobAddCommand = new JobAddCommand(validJob);
        ModelStub modelStub = new JobAddCommandTest.ModelStubWithJob(validJob);

        assertThrows(CommandException.class, Messages.MSG_DUPLICATE_JOB, ()
                -> jobAddCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Job auditing = new JobBuilder().withDesc("Auditing").build();
        Job webDev = new JobBuilder().withDesc("Web development")
                .build();
        JobAddCommand addAuditingCommand = new JobAddCommand(auditing);
        JobAddCommand addWebDevCommand = new JobAddCommand(webDev);

        // same object -> returns true
        assertEquals(addAuditingCommand, addAuditingCommand);

        // same values -> returns true
        JobAddCommand addAuditingCommandCopy = new JobAddCommand(auditing);
        assertEquals(addAuditingCommand, addAuditingCommandCopy);

        // different types -> returns false
        assertFalse(addAuditingCommand.equals(0));

        // null -> returns false
        assertFalse(addAuditingCommand.equals(null));

        // different job -> returns false
        assertFalse(addAuditingCommand.equals(addWebDevCommand));
    }

    /**
     * A Model stub that contains a single job.
     */
    private class ModelStubWithJob extends ModelStub {
        private final Job job;

        ModelStubWithJob(Job job) {
            requireNonNull(job);
            this.job = job;
        }

        @Override
        public boolean hasJob(ID jobId) {
            requireNonNull(jobId);
            return this.job.getJobId().equals(jobId);
        }

        @Override
        public boolean hasJob(Job job) {
            requireNonNull(job);
            return this.job.isSameJob(job);
        }
    }

    /**
     * A Model stub that always accept the job being added.
     */
    private class ModelStubAcceptingJobAdded extends ModelStub {
        final ArrayList<Job> jobsAdded = new ArrayList<>();

        @Override
        public boolean hasJob(Job job) {
            requireNonNull(job);
            return jobsAdded.stream().anyMatch(job::isSameJob);
        }

        @Override
        public boolean hasJob(ID jobId) {
            requireNonNull(jobId);
            return jobsAdded.stream().anyMatch(j -> jobId.equals(j.getJobId()));
        }

        @Override
        public void addJob(Job job) {
            requireNonNull(job);
            jobsAdded.add(job);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
