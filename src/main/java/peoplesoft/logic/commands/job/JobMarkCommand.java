package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.PaymentHandler;
import peoplesoft.model.money.exceptions.PaymentRequiresPersonException;

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

    private final Index toMark;
    private boolean state;
    private Employment instance;

    /**
     * Creates a {@code JobMarkCommand} to mark a {@code Job} by {@code Index}.
     *
     * @param toMark Index of job to mark.
     * @param instance Employment to use (for easier testing).
     */
    public JobMarkCommand(Index toMark, Employment instance) {
        requireAllNonNull(toMark, instance);
        this.toMark = toMark;
        this.instance = instance;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (toMark.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToMark = lastShownList.get(toMark.getZeroBased());

        if (jobToMark.isFinal()) {
            throw new CommandException("Cannot modify a job that has finalized payment.");
        }

        try {
            if (jobToMark.hasPaid()) {
                // Because of implementation of removePendingPayments, the exception will never be thrown
                // unless there are no persons at all.
                PaymentHandler.removePendingPayments(jobToMark, model, instance);
                state = true;
                model.setJob(jobToMark, jobToMark.setAsNotPaid());
            } else {
                PaymentHandler.createPendingPayments(jobToMark, model, instance);
                state = false;
                model.setJob(jobToMark, jobToMark.setAsPaid());
            }
        } catch (PaymentRequiresPersonException e) {
            throw new CommandException("Need at least one person assigned to this job.", e);
        } finally {
            // Turns out model equals() tests filtered lists
            model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
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
