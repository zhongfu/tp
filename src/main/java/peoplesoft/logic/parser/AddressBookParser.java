package peoplesoft.logic.parser;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.commons.core.Messages.MSG_UNKNOWN_CMD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.ExitCommand;
import peoplesoft.logic.commands.ExportCommand;
import peoplesoft.logic.commands.HelpCommand;
import peoplesoft.logic.commands.job.JobAddCommand;
import peoplesoft.logic.commands.job.JobAssignCommand;
import peoplesoft.logic.commands.job.JobDeleteCommand;
import peoplesoft.logic.commands.job.JobFinalizeCommand;
import peoplesoft.logic.commands.job.JobFindCommand;
import peoplesoft.logic.commands.job.JobListCommand;
import peoplesoft.logic.commands.job.JobMarkCommand;
import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.logic.commands.person.PersonDeleteCommand;
import peoplesoft.logic.commands.person.PersonEditCommand;
import peoplesoft.logic.commands.person.PersonFindCommand;
import peoplesoft.logic.commands.person.PersonListCommand;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.logic.parser.job.JobAddCommandParser;
import peoplesoft.logic.parser.job.JobAssignCommandParser;
import peoplesoft.logic.parser.job.JobDeleteCommandParser;
import peoplesoft.logic.parser.job.JobFinalizeCommandParser;
import peoplesoft.logic.parser.job.JobFindCommandParser;
import peoplesoft.logic.parser.job.JobMarkCommandParser;
import peoplesoft.logic.parser.person.PersonAddCommandParser;
import peoplesoft.logic.parser.person.PersonDeleteCommandParser;
import peoplesoft.logic.parser.person.PersonEditCommandParser;
import peoplesoft.logic.parser.person.PersonFindCommandParser;

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
            throw new ParseException(String.format(MSG_INVALID_CMD_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case PersonAddCommand.COMMAND_WORD:
            return new PersonAddCommandParser().parse(arguments);

        case PersonEditCommand.COMMAND_WORD:
            return new PersonEditCommandParser().parse(arguments);

        case PersonDeleteCommand.COMMAND_WORD:
            return new PersonDeleteCommandParser().parse(arguments);

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case PersonFindCommand.COMMAND_WORD:
            return new PersonFindCommandParser().parse(arguments);

        case PersonListCommand.COMMAND_WORD:
            return new PersonListCommand();

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

        case JobFindCommand.COMMAND_WORD:
            return new JobFindCommandParser().parse(arguments);

        case JobAssignCommand.COMMAND_WORD:
            return new JobAssignCommandParser().parse(arguments);

        case JobFinalizeCommand.COMMAND_WORD:
            return new JobFinalizeCommandParser().parse(arguments);

        default:
            throw new ParseException(MSG_UNKNOWN_CMD);
        }
    }

}
