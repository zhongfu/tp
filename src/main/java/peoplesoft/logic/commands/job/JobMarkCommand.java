package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;

/**
 * Marks a {@code Job} as paid or unpaid.
 */
public class JobMarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the job identified by the index number used in the displayed job list.\n"
            + "Parameters: JOB_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked Job %s as %s";
    public static final String MESSAGE_JOB_NOT_FOUND = "This job does not exist";

    private final Index toMark;
    private boolean state;

    /**
     * Creates a {@code JobMarkCommand} to mark a {@code Job} by {@code Index}.
     *
     * @param toMark Index of job to mark.
     */
    public JobMarkCommand(Index toMark) {
        requireNonNull(toMark);
        this.toMark = toMark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (toMark.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToMark = lastShownList.get(toMark.getZeroBased());

        if (jobToMark.hasPaid()) {
            state = true;
            model.setJob(jobToMark, jobToMark.setAsNotPaid());
        } else {
            state = false;
            model.setJob(jobToMark, jobToMark.setAsPaid());
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, jobToMark.getDesc(),
                state ? "not paid" : "paid"));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobMarkCommand // instanceof handles nulls
            && toMark.equals(((JobMarkCommand) other).toMark));
    }
}
