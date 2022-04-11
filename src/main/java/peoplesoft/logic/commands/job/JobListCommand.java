package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.model.Model.PREDICATE_SHOW_ALL_JOBS;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;

/**
 * Lists the {@code Jobs} stored in {@code AddressBook}.
 */
public class JobListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String COMMAND_EXAMPLES = "N.A.";

    public static final String COMMAND_FORMAT = COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all jobs";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
