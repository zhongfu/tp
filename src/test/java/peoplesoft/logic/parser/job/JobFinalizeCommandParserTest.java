package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobFinalizeCommand;
import peoplesoft.model.employment.Employment;

public class JobFinalizeCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MSG_INVALID_CMD_FORMAT, JobFinalizeCommand.MESSAGE_USAGE);

    private JobFinalizeCommandParser parser = new JobFinalizeCommandParser();

    @Test
    public void parse_missingArgs_failure() {
        // Missing confirmation prefix
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // Missing index
        assertParseFailure(parser, "y/", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // Negative index
        assertParseFailure(parser, "-5 y/", MESSAGE_INVALID_FORMAT);

        // Zero index
        assertParseFailure(parser, "0 y/", MESSAGE_INVALID_FORMAT);

        // Invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // Invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 n/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidConfirmationPrefix_failure() {
        assertParseFailure(parser, "1 i/ anything behind", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArgs_success() {
        JobFinalizeCommand expected = new JobFinalizeCommand(Index.fromOneBased(3), new Employment());
        assertParseSuccess(parser, "3 y/", expected);
    }
}
