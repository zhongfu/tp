package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobDeleteCommandParser;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;

/**
 * Deletes a {@code Job} with a given {@code JobId}.
 */
public class JobDeleteCommand extends Command {

    //TODO: change if needed
    public static final String COMMAND_WORD = "jobdelete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the job identified by the job ID.\n"
        + "Parameters: JOBID\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Deleted Job: %s";
    public static final String MESSAGE_JOB_NOT_FOUND = "This job does not exist";

    private final String toDelete;

    /**
     * Creates a {@code JobDeleteCommand} to delete a {@code Job} by {@code JobId}.
     *
     * @param args Arguments.
     * @throws ParseException Thrown if there is an error with parsing.
     */
    public JobDeleteCommand(String args) throws ParseException {
        requireNonNull(args);
        toDelete = new JobDeleteCommandParser().parse(args);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasJob(toDelete)) {
            throw new CommandException(MESSAGE_JOB_NOT_FOUND);
        }

        try {
            // TODO: This line breaks LoD
            Job jobToDelete = model.getAddressBook().getJobList()
                    .filtered(job -> job.getJobId().equals(toDelete)).get(0);
            model.deleteJob(jobToDelete);
        } catch (IndexOutOfBoundsException e) {
            // Asserts that filtered list should always contain exactly the filtered element
            assert false;
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, toDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobDeleteCommand // instanceof handles nulls
            && toDelete.equals(((JobDeleteCommand) other).toDelete));
    }
}
