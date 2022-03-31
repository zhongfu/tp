package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_DURATION;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_RATE;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.job.JobAddCommand;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.model.job.Rate;

public class JobAddCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, JobAddCommand.MESSAGE_USAGE);

    private static final String VALID_NAME = " " + PREFIX_NAME + "name";
    private static final String VALID_RATE = " " + PREFIX_RATE + "1.0";
    private static final String VALID_DURATION = " " + PREFIX_DURATION + "3";

    private static final String PREAMBLE = "preamble";
    private static final String INVALID_NAME = " " + PREFIX_NAME + "";
    private static final String INVALID_RATE = " " + PREFIX_RATE + "hello";
    private static final String INVALID_DURATION = " " + PREFIX_DURATION + "world";


    private JobAddCommandParser parser = new JobAddCommandParser();

    @Test
    public void parse_missingArgs_throwsParseException() {
        assertParseFailure(parser, VALID_NAME + VALID_RATE, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, VALID_NAME + VALID_DURATION, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, VALID_RATE + VALID_DURATION, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_hasPreamble_throwsParseException() {
        assertParseFailure(parser, PREAMBLE + VALID_NAME + VALID_RATE + VALID_DURATION,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_wrongFormatArgs_throwsParseException() {
        // Empty name
        assertParseFailure(parser, INVALID_NAME + VALID_RATE + VALID_DURATION,
                ParserUtil.STRING_MESSAGE_CONSTRAINTS);

        // Incorrect rate parse
        assertParseFailure(parser, VALID_NAME + INVALID_RATE + VALID_DURATION,
                Rate.MESSAGE_CONSTRAINTS);

        // Incorrect duration parse
        assertParseFailure(parser, VALID_NAME + VALID_RATE + INVALID_DURATION,
                ParserUtil.DURATION_MESSAGE_CONSTRAINTS);
    }
}
