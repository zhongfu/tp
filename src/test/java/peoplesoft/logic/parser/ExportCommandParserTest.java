package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validArgs_returnsExportCommand() {
        assertParseSuccess(parser, "1", new ExportCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "s",
                String.format(MSG_INVALID_CMD_FORMAT, ExportCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "r s/o m",
                String.format(MSG_INVALID_CMD_FORMAT, ExportCommand.MESSAGE_USAGE));
    }
}
