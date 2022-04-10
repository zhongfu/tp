package peoplesoft.logic.parser.person;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import java.util.Arrays;

import peoplesoft.logic.commands.person.PersonFindCommand;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new PersonFindCommand object
 */
public class PersonFindCommandParser implements Parser<PersonFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PersonFindCommand
     * and returns a PersonFindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PersonFindCommand parse(String args) throws ParseException {
        String trimmedArgs;
        try {
            trimmedArgs = ParserUtil.parseString(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MSG_INVALID_CMD_FORMAT, PersonFindCommand.MESSAGE_USAGE), pe);
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new PersonFindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
