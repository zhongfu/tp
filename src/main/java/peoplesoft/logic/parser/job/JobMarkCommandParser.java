package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses a {@code JobId} to mark.
 */
public class JobMarkCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobMarkCommand}
     * and returns a {@code JobId} string for {@code JobMarkCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public String parse(String args) throws ParseException {
        try {
            return ParserUtil.parseString(args);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, JobMarkCommand.MESSAGE_USAGE));
        }
    }
}
