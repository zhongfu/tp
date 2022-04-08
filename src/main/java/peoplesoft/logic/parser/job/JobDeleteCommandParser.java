package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobDeleteCommand;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses an {@code Index} to delete.
 */
public class JobDeleteCommandParser implements Parser<JobDeleteCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobDeleteCommand}
     * and returns a {@code JobDeleteCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public JobDeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new JobDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MSG_INVALID_CMD_FORMAT, JobDeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
