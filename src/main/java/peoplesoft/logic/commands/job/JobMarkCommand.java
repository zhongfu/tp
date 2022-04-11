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

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " 2";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " JOB_INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the chosen job as completed.\n"
            + "Format: " + COMMAND_WORD + " INDEX"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Marked job \"%s\" as %s.";

    public static final String MARK_PART = "completed";
    public static final String UNMARK_PART = "not completed";

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
            throw new CommandException(Messages.MSG_INVALID_JOB_DISPLAYED_IDX);
        }

        Job jobToMark = lastShownList.get(toMark.getZeroBased());

        // Todo: Not sure if this should be caught here or later.
        if (jobToMark.isFinal()) {
            throw new CommandException(Messages.MSG_MODIFY_FINAL_JOB);
        }

        try {
            if (jobToMark.hasPaid()) {
                // Because of implementation of removePendingPayments, the exception will only be thrown
                // when there are no persons at all.
                PaymentHandler.removePendingPayments(jobToMark, model, instance);
                model.setJob(jobToMark, jobToMark.setAsNotPaid());
                state = true;
            } else {
                PaymentHandler.createPendingPayments(jobToMark, model, instance);
                model.setJob(jobToMark, jobToMark.setAsPaid());
                state = false;
            }
        } catch (PaymentRequiresPersonException e) {
            throw new CommandException(Messages.MSG_ASSIGN_PERSON_TO_JOB, e);
        } finally {
            // Turns out model equals() tests filtered lists
            model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, jobToMark.getDesc(),
                state ? UNMARK_PART : MARK_PART));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobMarkCommand // instanceof handles nulls
            && toMark.equals(((JobMarkCommand) other).toMark));
    }
}
