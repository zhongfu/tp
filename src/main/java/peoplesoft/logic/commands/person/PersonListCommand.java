package peoplesoft.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static peoplesoft.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class PersonListCommand extends Command {

    public static final String COMMAND_WORD = "personlist";

    public static final String COMMAND_EXAMPLES = "N.A.";

    public static final String COMMAND_FORMAT = COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All people are now listed under Employees.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
