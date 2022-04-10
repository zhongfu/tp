package peoplesoft.logic.commands;

import javafx.beans.property.SimpleStringProperty;

/**
 * Handle help instructions for commands.
 */
public class CommandHelpMessage {
    public static final String DELIMITER = " ";

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
        return getCommand() + DELIMITER
                + getFormat() + DELIMITER
                + getExamples();
    }
}
