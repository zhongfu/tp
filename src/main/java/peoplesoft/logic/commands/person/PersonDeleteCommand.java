package peoplesoft.logic.commands.person;

import static java.util.Objects.requireNonNull;

import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the database.
 */
public class PersonDeleteCommand extends Command {

    public static final String COMMAND_WORD = "persondelete";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " 3";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " PERSON_INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Format: " + COMMAND_WORD + " INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "%s was removed.";

    private final Index targetIndex;

    public PersonDeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
        }

        Person p = lastShownList.get(targetIndex.getZeroBased()); // p is the person to delete
        model.deletePerson(p);
        // Deletes employment associations
        Employment.getInstance().deletePerson(p);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, p.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonDeleteCommand // instanceof handles nulls
                && targetIndex.equals(((PersonDeleteCommand) other).targetIndex)); // state check
    }
}
