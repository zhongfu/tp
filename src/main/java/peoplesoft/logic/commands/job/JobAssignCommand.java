package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobAssignCommandParser;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.employment.exceptions.DuplicateEmploymentException;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * Assigns a {@code Job} to a {@code Person}.
 */
public class JobAssignCommand extends Command {

    //TODO: change if needed
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a job to a person. "
        + "Parameters: "
        + "JOBID "
        + PREFIX_INDEX + "INDEX ";

    public static final String MESSAGE_SUCCESS = "Assigned Job %s to %s\n%s has the following jobs: %s";
    public static final String MESSAGE_JOB_NOT_FOUND = "This job does not exist";
    public static final String MESSAGE_DUPLICATE_EMPLOYMENT = "This person is already assigned to this job";

    private ID jobId;
    private Index personIndex;

    /**
     * Creates a {@code JobAssignCommand} to assign a {@code Job} to a {@code Person}.
     *
     * @param args Arguments.
     * @throws ParseException Thrown if there is an error with parsing.
     */
    public JobAssignCommand(String args) throws ParseException {
        requireNonNull(args);
        parseArgs(args);
    }

    private void parseArgs(String args) throws ParseException {
        // TODO: Arguably does not follow SRP/LoD
        ArgumentMultimap argumentMultimap = new JobAssignCommandParser().parse(args);
        try {
            jobId = new ID(ParserUtil.parseString(argumentMultimap.getPreamble()));
        } catch (IllegalArgumentException e) { // from ID constructor
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                JobAssignCommand.MESSAGE_USAGE));
        }
        personIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_INDEX).get());
    }

    // TODO: Change implementation if needed.
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasJob(jobId)) {
            throw new CommandException(MESSAGE_JOB_NOT_FOUND);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(personIndex.getZeroBased());
        try {
            // TODO: This code breaks LoD.
            Job assignedJob = model.getAddressBook().getJobList()
                    .filtered(job -> job.getJobId().equals(jobId)).get(0);
            Employment.getInstance().associate(assignedJob, person);
        } catch (IndexOutOfBoundsException e) {
            // Asserts that filtered list should always contain exactly the filtered element
            assert false;
        } catch (DuplicateEmploymentException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EMPLOYMENT);
        }

        List<Job> jobs = Employment.getInstance().getJobs(person, model);

        // TODO: For now just prints a list of the jobs associated with a person to console.
        return new CommandResult(String.format(MESSAGE_SUCCESS, jobId, person.getName(),
                person.getName(), jobs));
    }
}
