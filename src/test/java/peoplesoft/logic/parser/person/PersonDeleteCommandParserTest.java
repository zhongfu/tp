package peoplesoft.logic.parser.person;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.person.PersonDeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the PersonDeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the PersonDeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class PersonDeleteCommandParserTest {

    private PersonDeleteCommandParser parser = new PersonDeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new PersonDeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MSG_INVALID_CMD_FORMAT, PersonDeleteCommand.MESSAGE_USAGE));
    }
}
