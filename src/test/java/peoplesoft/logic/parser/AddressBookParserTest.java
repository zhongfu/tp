package peoplesoft.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.commons.core.Messages.MSG_UNKNOWN_CMD;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.ExitCommand;
import peoplesoft.logic.commands.HelpCommand;
import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.logic.commands.person.PersonDeleteCommand;
import peoplesoft.logic.commands.person.PersonEditCommand;
import peoplesoft.logic.commands.person.PersonEditCommand.EditPersonDescriptor;
import peoplesoft.logic.commands.person.PersonFindCommand;
import peoplesoft.logic.commands.person.PersonListCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;
import peoplesoft.testutil.EditPersonDescriptorBuilder;
import peoplesoft.testutil.PersonBuilder;
import peoplesoft.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().withNextId().build();
        PersonAddCommand command = (PersonAddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new PersonAddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        PersonDeleteCommand command = (PersonDeleteCommand) parser.parseCommand(
                PersonDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new PersonDeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        PersonEditCommand command = (PersonEditCommand) parser.parseCommand(PersonEditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new PersonEditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        PersonFindCommand command = (PersonFindCommand) parser.parseCommand(
                PersonFindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new PersonFindCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(PersonListCommand.COMMAND_WORD) instanceof PersonListCommand);
        assertTrue(parser.parseCommand(PersonListCommand.COMMAND_WORD + " 3") instanceof PersonListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MSG_INVALID_CMD_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MSG_UNKNOWN_CMD, () -> parser.parseCommand("unknownCommand"));
    }
}
