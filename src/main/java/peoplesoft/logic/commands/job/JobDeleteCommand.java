package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;

/**
 * Deletes a {@code Job} with a given {@code JobId}.
 */
public class JobDeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " 3";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " JOB_INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the job identified by the index.\n"
            + "Format: " + COMMAND_WORD + " INDEX\n"
            + "Example: " + COMMAND_WORD + " 3";

    public static final String MESSAGE_SUCCESS = "\"%s\" has been deleted.";

    private final Index toDelete;

    /**
     * Creates a {@code JobDeleteCommand} to delete a {@code Job} by {@code Index}.
     *
     * @param toDelete Index of job to delete.
     */
    public JobDeleteCommand(Index toDelete) {
        requireNonNull(toDelete);
        this.toDelete = toDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (toDelete.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MSG_INVALID_JOB_DISPLAYED_IDX);
        }

        Job jobToDelete = lastShownList.get(toDelete.getZeroBased());

        if (jobToDelete.isFinal()) {
            throw new CommandException(Messages.MSG_MODIFY_FINAL_JOB);
        }

        model.deleteJob(jobToDelete);
        // Deletes employment associations
        Employment.getInstance().deleteJob(jobToDelete);

        return new CommandResult(String.format(MESSAGE_SUCCESS, jobToDelete.getDesc()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobDeleteCommand // instanceof handles nulls
            && toDelete.equals(((JobDeleteCommand) other).toDelete));
    }
}
