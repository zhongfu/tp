package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobMarkCommandParser;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;

/**
 * Marks a {@code Job} as paid or unpaid.
 */
public class JobMarkCommand extends Command {

    // TODO: change if needed
    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Marks the job identified by the index number used in the displayed job list.\n"
        + "Parameters: JOBID\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked Job %s as %s";
    public static final String MESSAGE_JOB_NOT_FOUND = "This job does not exist";

    private final String toMark;
    private boolean state;

    /**
     * Creates a {@code JobDeleteCommand} to delete a {@code Job} by {@code JobId}.
     *
     * @param args Arguments.
     * @throws ParseException Thrown if there is an error with parsing.
     */
    public JobMarkCommand(String args) throws ParseException {
        requireNonNull(args);
        toMark = new JobMarkCommandParser().parse(args);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasJob(toMark)) {
            throw new CommandException(MESSAGE_JOB_NOT_FOUND);
        }

        try {
            // TODO: This line breaks LoD
            Job jobToMark = model.getAddressBook().getJobList()
                .filtered(job -> job.getJobId().equals(toMark)).get(0);
            // Creates immutable instances and replaces the existing ones
            if (jobToMark.hasPaid()) {
                state = true;
                model.setJob(jobToMark, jobToMark.setAsNotPaid());
            } else {
                state = false;
                model.setJob(jobToMark, jobToMark.setAsPaid());
            }
        } catch (IndexOutOfBoundsException e) {
            // Asserts that filtered list should always contain exactly the filtered element
            assert false;
        }
        // TODO: rudimentary result message
        return new CommandResult(String.format(MESSAGE_SUCCESS, toMark, state ? "not paid" : "paid"));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobMarkCommand // instanceof handles nulls
            && toMark.equals(((JobMarkCommand) other).toMark));
    }
}
