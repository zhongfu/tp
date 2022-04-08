package peoplesoft.logic.commands;

import static peoplesoft.logic.commands.HelpCommand.PERSON_ADD_COMMAND;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void asString_success() {
        ObservableList<CommandHelpMessage> commands = FXCollections.observableArrayList(
                PERSON_ADD_COMMAND);
        String expectedResult = PersonAddCommand.COMMAND_WORD
                + CommandHelpMessage.PROPERTY_DELIMITER
                + PersonAddCommand.COMMAND_FORMAT
                + CommandHelpMessage.PROPERTY_DELIMITER
                + PersonAddCommand.COMMAND_EXAMPLES
                + CommandHelpMessage.MESSAGE_DELIMITER;
        assert(CommandHelpMessage.asString(commands).equals(expectedResult));
    }

    @Test
    public void asObservableList_success() {
        String commandString = PersonAddCommand.COMMAND_WORD
                + CommandHelpMessage.PROPERTY_DELIMITER
                + PersonAddCommand.COMMAND_FORMAT
                + CommandHelpMessage.PROPERTY_DELIMITER
                + PersonAddCommand.COMMAND_EXAMPLES
                + CommandHelpMessage.MESSAGE_DELIMITER;

        ObservableList<CommandHelpMessage> result =
                CommandHelpMessage.asObservableList(commandString);

        ObservableList<CommandHelpMessage> expectedResult =
                FXCollections.observableArrayList(PERSON_ADD_COMMAND);

        String resultStr = Stream.of(result).map(x -> x.toString())
                .collect(Collectors.joining());
        String expectedResultStr = Stream.of(expectedResult).map(x -> x.toString())
                .collect(Collectors.joining());
        assert(resultStr.equals(expectedResultStr));
    }
}
