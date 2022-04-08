package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.job.JobFindCommand;
import peoplesoft.model.job.JobContainsKeywordsPredicate;

public class JobFindCommandParserTest {

    private JobFindCommandParser parser = new JobFindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " \r\t\n", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                JobFindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        JobFindCommand expected =
                new JobFindCommand(new JobContainsKeywordsPredicate(Arrays.asList("Power", "Puff")));
        assertParseSuccess(parser, "Power Puff", expected);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Power \n \t Puff  \t", expected);
    }
}
