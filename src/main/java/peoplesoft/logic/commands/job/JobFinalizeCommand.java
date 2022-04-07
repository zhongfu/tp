package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_CONFIRMATION;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.exceptions.JobNotPaidException;
import peoplesoft.model.money.PaymentHandler;
import peoplesoft.model.money.exceptions.PaymentRequiresPersonException;

/**
 * Finalizes a {@code Job} and its associated {@code Payment}s.
 */
public class JobFinalizeCommand extends Command {

    public static final String COMMAND_WORD = "pay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finalizes payments for the job identified by the index number used in the displayed job list.\n"
            + "Note: This command is irreversible!\n"
            + "Parameters: JOB_INDEX " + PREFIX_CONFIRMATION + "\n"
            + "Example: " + COMMAND_WORD + " 1" + PREFIX_CONFIRMATION;

    public static final String MESSAGE_SUCCESS = "Finalized payments for Job %s.";
    public static final String MESSAGE_JOB_NOT_PAID_FAILURE =
            "Payments cannot be finalized if the job is not yet marked as paid.";
    // TODO: change message if needed

    private final Index toFinalize;
    private Employment instance;

    /**
     * Creates a {@code JobFinalizeCommand} to finalize a {@code Job}.
     *
     * @param toFinalize Index of job to finalize.
     * @param instance Employment to use (for easier testing).
     */
    public JobFinalizeCommand(Index toFinalize, Employment instance) {
        requireAllNonNull(toFinalize, instance);
        this.toFinalize = toFinalize;
        this.instance = instance;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Job> lastShownList = model.getFilteredJobList();

        if (toFinalize.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job job = lastShownList.get(toFinalize.getZeroBased());

        if (job.isFinal()) {
            throw new CommandException(Messages.MESSAGE_MODIFY_FINAL_JOB);
        }

        try {
            Job finalJob = job.setAsFinal();
            model.setJob(job, finalJob);
            PaymentHandler.finalizePayments(finalJob, model, instance);
        } catch (JobNotPaidException e) {
            throw new CommandException(MESSAGE_JOB_NOT_PAID_FAILURE, e);
        } catch (PaymentRequiresPersonException e) {
            throw new CommandException(Messages.MESSAGE_ASSIGN_PERSON_TO_JOB, e);
        } finally {
            model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, job.getDesc()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobFinalizeCommand // instanceof handles nulls
            && toFinalize.equals(((JobFinalizeCommand) other).toFinalize));
    }
}
