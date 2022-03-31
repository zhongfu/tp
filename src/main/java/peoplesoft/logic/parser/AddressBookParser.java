package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import peoplesoft.logic.commands.AddCommand;
import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.DeleteCommand;
import peoplesoft.logic.commands.EditCommand;
import peoplesoft.logic.commands.ExitCommand;
import peoplesoft.logic.commands.FindCommand;
import peoplesoft.logic.commands.HelpCommand;
import peoplesoft.logic.commands.ListCommand;
import peoplesoft.logic.commands.job.JobAddCommand;
import peoplesoft.logic.commands.job.JobAssignCommand;
import peoplesoft.logic.commands.job.JobDeleteCommand;
import peoplesoft.logic.commands.job.JobListCommand;
import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobAddCommandParser;
import peoplesoft.logic.parser.job.JobAssignCommandParser;
import peoplesoft.logic.parser.job.JobDeleteCommandParser;
import peoplesoft.logic.parser.job.JobMarkCommandParser;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

            // Job related commands

        case JobAddCommand.COMMAND_WORD:
            return new JobAddCommandParser().parse(arguments);

        case JobListCommand.COMMAND_WORD:
            return new JobListCommand();

        case JobDeleteCommand.COMMAND_WORD:
            return new JobDeleteCommandParser().parse(arguments);

        case JobMarkCommand.COMMAND_WORD:
            return new JobMarkCommandParser().parse(arguments);

        case JobAssignCommand.COMMAND_WORD:
            return new JobAssignCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
