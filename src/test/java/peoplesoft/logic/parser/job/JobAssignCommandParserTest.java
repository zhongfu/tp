package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobAssignCommand;
import peoplesoft.model.employment.Employment;

public class JobAssignCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MSG_INVALID_CMD_FORMAT, JobAssignCommand.MESSAGE_USAGE);

    private JobAssignCommandParser parser = new JobAssignCommandParser();

    @Test
    public void parse_missingArgs_failure() {
        // Missing index for person
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // Missing index for job
        assertParseFailure(parser, "i/1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // Negative index
        assertParseFailure(parser, "-5 i/1", MESSAGE_INVALID_FORMAT);

        // Zero index
        assertParseFailure(parser, "0 i/1", MESSAGE_INVALID_FORMAT);

        // Invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // Invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 n/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Negative index
        assertParseFailure(parser, "1 i/-5", MESSAGE_INVALID_FORMAT);

        // Zero index
        assertParseFailure(parser, "1 i/0", MESSAGE_INVALID_FORMAT);

        // Invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 i/1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArgs_success() {
        // One index for person
        JobAssignCommand expected = new JobAssignCommand(Index.fromOneBased(3),
                new HashSet<Index>(Arrays.asList(Index.fromOneBased(2))), new Employment());
        assertParseSuccess(parser, "3 i/2", expected);

        // Multiple indexes for person
        expected = new JobAssignCommand(Index.fromOneBased(2), new HashSet<Index>(Arrays.asList(
                Index.fromOneBased(1), Index.fromOneBased(3), Index.fromOneBased(4))), new Employment());
        assertParseSuccess(parser, "2 i/1 i/4 i/3", expected);
    }
}
