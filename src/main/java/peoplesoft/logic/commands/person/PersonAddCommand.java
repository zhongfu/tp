package peoplesoft.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_EMAIL;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_PHONE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_RATE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_TAG;

import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.model.Model;
import peoplesoft.model.person.Person;

/**
 * Adds a person to the database.
 */
public class PersonAddCommand extends Command {

    public static final String COMMAND_WORD = "personadd";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " "
            + PREFIX_NAME + "Nicole Tan "
            + PREFIX_PHONE + "99338558 "
            + PREFIX_EMAIL + "nicole@stffhub.org "
            + PREFIX_ADDRESS + "1 Tech Drive, S138572 "
            + PREFIX_RATE + "37.50 "
            + PREFIX_TAG + "Hardware "
            + PREFIX_TAG + "Senior";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_RATE + "RATE "
            + "[" + PREFIX_TAG + "TAG]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the database.\n"
            + "Format: "
            + COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_RATE + "RATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_RATE + "3.20 "
            + PREFIX_TAG + "Intern "
            + PREFIX_TAG + "Painting";

    public static final String MESSAGE_SUCCESS = "%s was added.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the database";

    private final Person toAdd;

    /**
     * Creates an PersonAddCommand to add the specified {@code Person}
     */
    public PersonAddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonAddCommand // instanceof handles nulls
                && toAdd.equals(((PersonAddCommand) other).toAdd));
    }
}
