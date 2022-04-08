package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.model.employment.Employment;

public class JobMarkCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MSG_INVALID_CMD_FORMAT, JobMarkCommand.MESSAGE_USAGE);

    private static final String WHITESPACE = " \t\r\n";

    private JobMarkCommandParser parser = new JobMarkCommandParser();

    @Test
    public void parse_invalidPreamble_failure() {
        // Whitespace
        assertParseFailure(parser, WHITESPACE, MESSAGE_INVALID_FORMAT);

        // Negative index
        assertParseFailure(parser, "-5", MESSAGE_INVALID_FORMAT);

        // Zero index
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);

        // Invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // Invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 n/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validValue_returnsString() throws Exception {
        JobMarkCommand expected = new JobMarkCommand(Index.fromOneBased(1), new Employment());
        assertParseSuccess(parser, "1", expected);

        // With whitespace
        expected = new JobMarkCommand(Index.fromOneBased(5), new Employment());
        assertParseSuccess(parser, WHITESPACE + "5" + WHITESPACE, expected);
    }
}
