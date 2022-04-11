package peoplesoft.logic.commands;

import static java.util.Objects.requireNonNull;

import peoplesoft.commons.core.JobIdFactory;
import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.model.AddressBook;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_EXAMPLES = "N.A.";
    public static final String COMMAND_FORMAT = COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Employee list has been cleared.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        // Resets association and jobId
        Employment.newInstance();
        JobIdFactory.setId(0);
        PersonIdFactory.setId(0);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
