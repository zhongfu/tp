package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.ExportCommand;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ExportCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MSG_INVALID_CMD_FORMAT, ExportCommand.MESSAGE_USAGE), pe);
        }
    }

}
