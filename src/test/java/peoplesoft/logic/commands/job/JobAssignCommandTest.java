package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.testutil.Assert.assertThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.EmploymentStub;
import peoplesoft.logic.commands.ModelStub;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.AddressBook;
import peoplesoft.model.ReadOnlyAddressBook;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;
import peoplesoft.testutil.JobBuilder;
import peoplesoft.testutil.PersonBuilder;

public class JobAssignCommandTest {

    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofHours(1));

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

    @Test
    public void execute_successful() throws Exception {
        Job validJob = new JobBuilder().build();
        Person validPerson = new PersonBuilder().build();

        ModelStubWithJobAndPerson modelStub = new ModelStubWithJobAndPerson();
        EmploymentStub employmentStub = new EmploymentStub();
        modelStub.addJob(validJob);
        modelStub.addPerson(validPerson);

        Index jobIndex = Index.fromZeroBased(0);
        Set<Index> personIndexes = new HashSet<>();
        personIndexes.add(Index.fromZeroBased(0));

        CommandResult commandResult = new JobAssignCommand(jobIndex, personIndexes, employmentStub)
                .execute(modelStub);
        assertEquals(String.format(JobAssignCommand.MESSAGE_SUCCESS, validJob.getDesc(), validPerson.getName()),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validJob), modelStub.jobsAdded);
    }

    @Test
    public void execute_invalidJobIndex_throwsCommandException() {
        Job validJob = new JobBuilder().build();
        Person validPerson = new PersonBuilder().build();

        ModelStubWithJobAndPerson modelStub = new ModelStubWithJobAndPerson();
        EmploymentStub employmentStub = new EmploymentStub();
        modelStub.addJob(validJob);
        modelStub.addPerson(validPerson);

        Index invalidJobIndex = Index.fromZeroBased(10);
        Set<Index> personIndexes = new HashSet<>();
        personIndexes.add(Index.fromZeroBased(0));

        assertThrows(CommandException.class, ()
                -> new JobAssignCommand(invalidJobIndex, personIndexes, employmentStub).execute(modelStub));
    }

    @Test
    public void execute_finalJob_throwsCommandException() {
        Job validJob = new JobBuilder().withIsFinal(true).build();
        Person validPerson = new PersonBuilder().build();

        ModelStubWithJobAndPerson modelStub = new ModelStubWithJobAndPerson();
        EmploymentStub employmentStub = new EmploymentStub();
        modelStub.addJob(validJob);
        modelStub.addPerson(validPerson);

        Index jobIndex = Index.fromZeroBased(0);
        Set<Index> personIndexes = new HashSet<>();
        personIndexes.add(Index.fromZeroBased(0));

        assertThrows(CommandException.class, ()
                -> new JobAssignCommand(jobIndex, personIndexes, employmentStub).execute(modelStub));
    }

    @Test
    public void execute_paidJob_throwsCommandException() {
        Job validJob = new JobBuilder().withHasPaid(true).build();
        Person validPerson = new PersonBuilder().build();

        ModelStubWithJobAndPerson modelStub = new ModelStubWithJobAndPerson();
        EmploymentStub employmentStub = new EmploymentStub();
        modelStub.addJob(validJob);
        modelStub.addPerson(validPerson);

        Index jobIndex = Index.fromZeroBased(0);
        Set<Index> personIndexes = new HashSet<>();
        personIndexes.add(Index.fromZeroBased(0));

        assertThrows(CommandException.class, ()
                -> new JobAssignCommand(jobIndex, personIndexes, employmentStub).execute(modelStub));
    }

    /**
     * A Model stub that always accept the job being added.
     */
    private class ModelStubWithJobAndPerson extends ModelStub {
        final ArrayList<Job> jobsAdded = new ArrayList<>();
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public boolean hasPerson(ID personId) {
            requireNonNull(personId);
            return personsAdded.stream().anyMatch(p -> personId.equals(p.getPersonId()));
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

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
        public ObservableList<Job> getFilteredJobList() {
            return FXCollections.observableList(jobsAdded);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(personsAdded);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
