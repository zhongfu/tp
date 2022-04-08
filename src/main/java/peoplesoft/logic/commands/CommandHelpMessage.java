package peoplesoft.logic.commands;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handle help instructions for commands.
 */
public class CommandHelpMessage {
    public static final String PROPERTY_DELIMITER = "----";
    public static final String MESSAGE_DELIMITER = "--------";

    private final SimpleStringProperty command;
    private final SimpleStringProperty format;
    private final SimpleStringProperty examples;


    /**
     * Constructor for CommandHelpMessage.
     *
     * @param command command word
     * @param format format descriptor of command
     * @param examples example usage of command
     */
    public CommandHelpMessage(String command, String format, String examples) {
        this.command = new SimpleStringProperty(command);
        this.format = new SimpleStringProperty(format);
        this.examples = new SimpleStringProperty(examples);
    }

    public String getCommand() {
        return command.get();
    }

    public String getFormat() {
        return format.get();
    }

    public String getExamples() {
        return examples.get();
    }

    /**
     * Converts a command's help message into string delimited by the property delimiter.
     *
     * @return formatted help message.
     */
    public String toString() {
        return getCommand() + PROPERTY_DELIMITER
                + getFormat() + PROPERTY_DELIMITER
                + getExamples();
    }

    /**
     * Convert list of commands to string.
     *
     * @param commands list of commands
     * @return string delimited by -------- and ----
     */
    public static String asString(ObservableList<CommandHelpMessage> commands) {
        StringBuilder parsedCommandMessages = new StringBuilder();

        for (CommandHelpMessage msg: commands) {
            parsedCommandMessages.append(msg);
            parsedCommandMessages.append(MESSAGE_DELIMITER);
        }

        return parsedCommandMessages.toString();
    }

    /**
     * Parse a string into an ObservableList of CommandHelpMessages.
     *
     * @param str string to be parsed
     * @return ObservableList of help messages for commands in string.
     */
    public static ObservableList<CommandHelpMessage> asObservableList(String str) {
        ObservableList<CommandHelpMessage> commandHelpMessages =
                FXCollections.observableArrayList();

        List<String> commands = Stream.of(str.split(MESSAGE_DELIMITER))
                .collect(Collectors.toList());
        for (String msg: commands) {
            List<String> data = Stream.of(msg.split(PROPERTY_DELIMITER))
                    .collect(Collectors.toList());

            try {
                CommandHelpMessage c = new CommandHelpMessage(data.get(0), data.get(1),
                        data.get(2));
                commandHelpMessages.add(c);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return commandHelpMessages;
    }
}
