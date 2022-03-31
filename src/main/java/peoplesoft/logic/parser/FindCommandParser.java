package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import peoplesoft.logic.commands.FindCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs;
        try {
            trimmedArgs = ParserUtil.parseString(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE), pe);
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
