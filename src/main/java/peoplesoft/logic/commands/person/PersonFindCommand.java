package peoplesoft.logic.commands.person;

import static java.util.Objects.requireNonNull;

import peoplesoft.commons.core.Messages;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.model.Model;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class PersonFindCommand extends Command {

    public static final String COMMAND_WORD = "personfind";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " Aircon,\n"
            + COMMAND_WORD + " Nicole Hardware";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Format: "
            + COMMAND_WORD + " "
            + "KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final PersonContainsKeywordsPredicate predicate;

    public PersonFindCommand(PersonContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MSG_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonFindCommand // instanceof handles nulls
                && predicate.equals(((PersonFindCommand) other).predicate)); // state check
    }
}
