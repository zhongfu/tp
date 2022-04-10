package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.employment.Employment;

/**
 * Parses an {@code Index} of a {@code Job} to mark.
 */
public class JobMarkCommandParser implements Parser<JobMarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobMarkCommand}
     * and returns a {@code JobMarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public JobMarkCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new JobMarkCommand(index, Employment.getInstance());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MSG_INVALID_CMD_FORMAT, JobMarkCommand.MESSAGE_USAGE), pe);
        }
    }
}
