package peoplesoft.logic.parser.person;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.person.PersonFindCommand;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;

public class PersonFindCommandParserTest {

    private PersonFindCommandParser parser = new PersonFindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MSG_INVALID_CMD_FORMAT, PersonFindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        PersonFindCommand expectedPersonFindCommand =
                new PersonFindCommand(new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedPersonFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedPersonFindCommand);
    }

}
