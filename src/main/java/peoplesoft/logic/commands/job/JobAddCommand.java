package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_DURATION;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;

import peoplesoft.commons.core.Messages;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;

/**
 * Adds a {@code Job} to {@code AddressBook}.
 */
public class JobAddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " "
            + PREFIX_NAME + "Fix HDB Lock "
            + PREFIX_DURATION + "1";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_DURATION + "DURATION ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the database.\n"
            + "Format: "
            + COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_DURATION + "DURATION\n"
            + "Example: "
            + COMMAND_WORD + " "
            + PREFIX_NAME + "Fix washing machine "
            + PREFIX_DURATION + "2.5\n";

    public static final String MESSAGE_SUCCESS = "New job added: %s";

    private final Job toAdd;

    /**
     * Creates a {@code JobAddCommand} to add a {@code Job}.
     *
     * @param toAdd Job.
     * @throws ParseException Thrown if there is an error with parsing.
     */
    public JobAddCommand(Job toAdd) {
        requireNonNull(toAdd);
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasJob(toAdd.getJobId())) { // Note: user will never trigger this error
            throw new CommandException(Messages.MSG_DUPLICATE_JOB);
        }

        model.addJob(toAdd);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobAddCommand // instanceof handles nulls
            && toAdd.equals(((JobAddCommand) other).toAdd));
    }
}
