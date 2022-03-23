package peoplesoft.logic.parser.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.parser.exceptions.ParseException;

public class JobDeleteCommandParserTest {

    private static final String VALID_STRING = "valid";
    private static final String WHITESPACE = " \t\r\n";

    private JobDeleteCommandParser parser = new JobDeleteCommandParser();

    @Test
    public void parse_whitespace_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(WHITESPACE));
    }

    @Test
    public void parse_validValue_returnsString() throws Exception {
        assertEquals(VALID_STRING, parser.parse(VALID_STRING));
        // With whitespace
        assertEquals(VALID_STRING, parser.parse(WHITESPACE + VALID_STRING + WHITESPACE));
        // TODO: Currently exactly the same as ParserUtil.parseString()
    }
}
