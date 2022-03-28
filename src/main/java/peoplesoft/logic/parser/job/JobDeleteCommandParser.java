package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import peoplesoft.logic.commands.job.JobDeleteCommand;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.util.ID;

/**
 * Parses a {@code jobId} to delete.
 */
public class JobDeleteCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobDeleteCommand}
     * and returns a {@code JobId} string for {@code JobDeleteCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ID parse(String args) throws ParseException {
        try {
            String strVal = ParserUtil.parseString(args); // throws ParseException?
            return new ID(strVal); // throws IllegalArgumentException
        } catch (ParseException | IllegalArgumentException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, JobDeleteCommand.MESSAGE_USAGE));
        }
    }
}
