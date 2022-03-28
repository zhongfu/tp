package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.util.ID;

/**
 * Parses a {@code JobId} to mark.
 */
public class JobMarkCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobMarkCommand}
     * and returns a {@code JobId} string for {@code JobMarkCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ID parse(String args) throws ParseException {
        try {
            String strVal = ParserUtil.parseString(args); // throws ParseException?
            return new ID(strVal); // throws IllegalArgumentException
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, JobMarkCommand.MESSAGE_USAGE));
        }
    }
}
