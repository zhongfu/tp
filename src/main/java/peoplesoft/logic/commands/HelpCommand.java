package peoplesoft.logic.commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import peoplesoft.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_EXAMPLES = "N.A.";
    public static final String COMMAND_FORMAT = COMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened the help page.\n"
            + "Type any other command to return to the overview page.";

    public static final CommandHelpMessage JOB_ADD_COMMAND = new CommandHelpMessage(
            JobAddCommand.COMMAND_WORD,
            JobAddCommand.COMMAND_FORMAT,
            JobAddCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_ASSIGN_COMMAND = new CommandHelpMessage(
            JobAssignCommand.COMMAND_WORD,
            JobAssignCommand.COMMAND_FORMAT,
            JobAssignCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_DELETE_COMMAND = new CommandHelpMessage(
            JobDeleteCommand.COMMAND_WORD,
            JobDeleteCommand.COMMAND_FORMAT,
            JobDeleteCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_FINALIZE_COMMAND = new CommandHelpMessage(
            JobFinalizeCommand.COMMAND_WORD,
            JobFinalizeCommand.COMMAND_FORMAT,
            JobFinalizeCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_FIND_COMMAND = new CommandHelpMessage(
            JobFindCommand.COMMAND_WORD,
            JobFindCommand.COMMAND_FORMAT,
            JobFindCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_LIST_COMMAND = new CommandHelpMessage(
            JobListCommand.COMMAND_WORD,
            JobListCommand.COMMAND_FORMAT,
            JobListCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage JOB_MARK_COMMAND = new CommandHelpMessage(
            JobMarkCommand.COMMAND_WORD,
            JobMarkCommand.COMMAND_FORMAT,
            JobMarkCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage PERSON_ADD_COMMAND = new CommandHelpMessage(
            PersonAddCommand.COMMAND_WORD,
            PersonAddCommand.COMMAND_FORMAT,
            PersonAddCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage PERSON_DELETE_COMMAND = new CommandHelpMessage(
            PersonDeleteCommand.COMMAND_WORD,
            PersonDeleteCommand.COMMAND_FORMAT,
            PersonDeleteCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage PERSON_EDIT_COMMAND = new CommandHelpMessage(
            PersonEditCommand.COMMAND_WORD,
            PersonEditCommand.COMMAND_FORMAT,
            PersonEditCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage PERSON_FIND_COMMAND = new CommandHelpMessage(
            PersonFindCommand.COMMAND_WORD,
            PersonFindCommand.COMMAND_FORMAT,
            PersonFindCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage PERSON_LIST_COMMAND = new CommandHelpMessage(
            PersonListCommand.COMMAND_WORD,
            PersonListCommand.COMMAND_FORMAT,
            PersonListCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage CLEAR_COMMAND = new CommandHelpMessage(
            ClearCommand.COMMAND_WORD,
            ClearCommand.COMMAND_FORMAT,
            ClearCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage EXIT_COMMAND = new CommandHelpMessage(
            ExitCommand.COMMAND_WORD,
            ExitCommand.COMMAND_FORMAT,
            ExitCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage EXPORT_COMMAND = new CommandHelpMessage(
            ExportCommand.COMMAND_WORD,
            ExportCommand.COMMAND_FORMAT,
            ExportCommand.COMMAND_EXAMPLES);

    public static final CommandHelpMessage HELP_COMMAND = new CommandHelpMessage(
            COMMAND_WORD,
            COMMAND_FORMAT,
            COMMAND_EXAMPLES);

    public static final ObservableList<CommandHelpMessage> COMMANDS =
            FXCollections.observableArrayList(
            PERSON_ADD_COMMAND, PERSON_EDIT_COMMAND, PERSON_DELETE_COMMAND, PERSON_FIND_COMMAND,
            PERSON_LIST_COMMAND, JOB_ADD_COMMAND, JOB_ASSIGN_COMMAND, JOB_DELETE_COMMAND,
            JOB_FIND_COMMAND, JOB_LIST_COMMAND, JOB_MARK_COMMAND, JOB_FINALIZE_COMMAND,
            CLEAR_COMMAND, EXIT_COMMAND, EXPORT_COMMAND, HELP_COMMAND);

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
