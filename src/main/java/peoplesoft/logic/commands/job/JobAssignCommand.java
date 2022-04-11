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

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " "
            + "2 "
            + PREFIX_INDEX + "1";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " "
            + "JOB_INDEX "
            + PREFIX_INDEX + "PERSON_INDEX [i/PERSON_INDEX]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a job to one or more person(s).\n"
            + "Format: " + COMMAND_WORD + " JOB_INDEX "
            + PREFIX_INDEX + "PERSON_INDEX [PERSON_INDEX]... (as many persons as needed)\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_INDEX + "3 " + PREFIX_INDEX + "4";

    public static final String MESSAGE_SUCCESS = "Assigned job \"%s\" to %s.";

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
            throw new CommandException(Messages.MSG_INVALID_JOB_DISPLAYED_IDX);
        }

        Job job = lastShownJobs.get(jobIndex.getZeroBased());

        if (job.isFinal()) {
            throw new CommandException(Messages.MSG_MODIFY_FINAL_JOB);
        }

        if (job.hasPaid()) {
            throw new CommandException(Messages.MSG_ASSIGN_MARKED_JOB);
        }

        for (Index i : personIndexes) {
            if (i.getZeroBased() >= lastShownPersons.size()) {
                throw new CommandException(Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
            }
        }

        Set<Person> persons = getPersons(personIndexes, lastShownPersons);

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
                throw new CommandException(String.format(Messages.MSG_DUPLICATE_EMPLOYMENT, dupeCount));
            } else {
                throw new CommandException(String.format(Messages.MSG_DUPLICATE_EMPLOYMENT, dupeCount) + "\n"
                        + String.format(MESSAGE_SUCCESS, job.getDesc(), successPersons));
            }
        }

        // show the new assignement
        model.updateFilteredJobList(Model.PREDICATE_SHOW_ALL_JOBS);

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
