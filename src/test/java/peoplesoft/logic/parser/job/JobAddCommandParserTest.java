package peoplesoft.logic.parser.job;

import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.parser.exceptions.ParseException;

public class JobAddCommandParserTest {

    private JobAddCommandParser parser = new JobAddCommandParser();

    @Test
    public void parse_missingArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" n/name r/1.0"));
        assertThrows(ParseException.class, () -> parser.parse(" n/name d/3"));
        assertThrows(ParseException.class, () -> parser.parse(" r/1.0 d/3"));
    }

    @Test
    public void parse_wrongFormatArgs_throwsParseException() {
        // Empty name
        assertThrows(ParseException.class, () -> parser.parse(" n/ r/1.0 d/3"));
        // Incorrect rate parse
        assertThrows(ParseException.class, () -> parser.parse(" n/name r/hello d/3"));
        // Incorrect duration parse
        assertThrows(ParseException.class, () -> parser.parse(" n/name r/1.0 d/world"));
    }

    // TODO: create test for successful construction
    // Current issue is that a new jobId will be made and that may not be deterministic
}
