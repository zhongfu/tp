package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.person.PersonDeleteCommand;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<PersonDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PersonDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new PersonDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MSG_INVALID_CMD_FORMAT, PersonDeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}