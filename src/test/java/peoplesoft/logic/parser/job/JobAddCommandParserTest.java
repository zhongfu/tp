package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_DURATION_CONSTRAINTS;
import static peoplesoft.commons.core.Messages.MSG_EMPTY_STRING;
import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_DURATION;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.job.JobAddCommand;

public class JobAddCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MSG_INVALID_CMD_FORMAT, JobAddCommand.MESSAGE_USAGE);

    private static final String VALID_NAME = " " + PREFIX_NAME + "name";
    private static final String VALID_DURATION = " " + PREFIX_DURATION + "3";

    private static final String PREAMBLE = "preamble";
    private static final String INVALID_NAME = " " + PREFIX_NAME + "";
    private static final String INVALID_DURATION = " " + PREFIX_DURATION + "world";


    private JobAddCommandParser parser = new JobAddCommandParser();

    @Test
    public void parse_missingArgs_throwsParseException() {
        assertParseFailure(parser, VALID_NAME, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, VALID_DURATION, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_hasPreamble_throwsParseException() {
        assertParseFailure(parser, PREAMBLE + VALID_NAME + VALID_DURATION,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_emptyNameArgs_throwsParseException() {
        // Empty name
        assertParseFailure(parser, INVALID_NAME + VALID_DURATION,
                MSG_EMPTY_STRING);

        // Incorrect duration parse
        assertParseFailure(parser, VALID_NAME + INVALID_DURATION,
                MSG_DURATION_CONSTRAINTS);
    }
}
