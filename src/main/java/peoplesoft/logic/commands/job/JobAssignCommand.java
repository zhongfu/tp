package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.employment.exceptions.DuplicateEmploymentException;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;

/**
 * Assigns a {@code Job} to a {@code Person}.
 */
public class JobAssignCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a job to one or more person(s). "
            + "Parameters: "
            + "JOB_INDEX "
            + PREFIX_INDEX + "PERSON_INDEX [PERSON_INDEX]...";

    public static final String MESSAGE_SUCCESS = "Assigned Job %s to %s.";
    public static final String MESSAGE_DUPLICATE_EMPLOYMENT =
            "%s person(s) have already been assigned to this job.";
    public static final String MESSAGE_ASSIGN_MARKED_JOB = "Un-mark the job before assigning it.";

    private Index jobIndex;
    private Set<Index> personIndexes;
    private Employment instance;

    /**
     * Creates a {@code JobAssignCommand} to assign a {@code Job} to a set of {@code Person}s.
     *
     * @param jobIndex Index of the job.
     * @param personIndexes Set of indexes of persons.
     * @param employment Employment to use (for easier testing).
     */
    public JobAssignCommand(Index jobIndex, Set<Index> personIndexes, Employment employment) {
        requireAllNonNull(jobIndex, personIndexes, employment);
        this.jobIndex = jobIndex;
        this.personIndexes = personIndexes;
        this.instance = employment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownJobs = model.getFilteredJobList();
        List<Person> lastShownPersons = model.getFilteredPersonList();

        if (jobIndex.getZeroBased() >= lastShownJobs.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        for (Index i : personIndexes) {
            if (i.getZeroBased() >= lastShownPersons.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        Job job = lastShownJobs.get(jobIndex.getZeroBased());
        Set<Person> persons = getPersons(personIndexes, lastShownPersons);

        if (job.hasPaid()) {
            throw new CommandException(MESSAGE_ASSIGN_MARKED_JOB);
        }

        if (job.isFinal()) {
            throw new CommandException(Messages.MESSAGE_MODIFY_FINAL_JOB);
        }

        int dupeCount = 0;
        StringBuilder stringPersons = new StringBuilder();

        for (Person p : persons) {
            try {
                instance.associate(job, p);
                stringPersons.append(p.getName()).append(", ");
            } catch (DuplicateEmploymentException e) {
                dupeCount++;
            }
        }
        if (stringPersons.length() > 0) {
            stringPersons.delete(stringPersons.length() - 2, stringPersons.length());
        }
        String successPersons = stringPersons.toString();

        if (dupeCount > 0) {
            if (successPersons.isBlank()) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_EMPLOYMENT, dupeCount));
            } else {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_EMPLOYMENT, dupeCount) + "\n"
                        + String.format(MESSAGE_SUCCESS, job.getDesc(), successPersons));
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, job.getDesc(), successPersons));
    }

    private Set<Person> getPersons(Set<Index> personIndexes, List<Person> personList) {
        Set<Person> persons = new HashSet<>();
        for (Index i : personIndexes) {
            persons.add(personList.get(i.getZeroBased()));
        }
        return persons;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobAssignCommand)) {
            return false;
        }

        // state check
        JobAssignCommand c = (JobAssignCommand) other;
        return jobIndex.equals(c.jobIndex)
                && personIndexes.equals(c.personIndexes);
    }
}
