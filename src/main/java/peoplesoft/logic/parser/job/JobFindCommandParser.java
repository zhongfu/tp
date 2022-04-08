package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;

import java.util.Arrays;

import peoplesoft.logic.commands.job.JobFindCommand;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.job.JobContainsKeywordsPredicate;

public class JobFindCommandParser implements Parser<JobFindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobFindCommand}
     * and returns a {@code JobFindCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public JobFindCommand parse(String args) throws ParseException {
        String trimmedArgs;
        try {
            trimmedArgs = ParserUtil.parseString(args);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MSG_INVALID_CMD_FORMAT, JobFindCommand.MESSAGE_USAGE), pe);
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new JobFindCommand(new JobContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
