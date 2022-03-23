package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.util.Employment;

/**
 * Lists the {@code Jobs} stored in {@code AddressBook}.
 */
public class JobListCommand extends Command {

    // TODO: change if needed
    public static final String COMMAND_WORD = "joblist";

    public static final String MESSAGE_SUCCESS = "Listed all jobs: %s\nEmployment: %s";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // TODO: UI interaction, currently prints to console
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getFilteredJobList(),
                Employment.getInstance().getAllJobs()));
    }
}
