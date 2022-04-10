package peoplesoft.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.export.Exporter;
import peoplesoft.model.Model;
import peoplesoft.model.person.Person;


/**
 * Exports a person identified using it's displayed index from the database.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " 1";
    public static final String COMMAND_FORMAT = COMMAND_WORD + " INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports a .csv file with the jobs the person worked on, "
            + "including how much pay they should expect to receive.\n"
            + "All the jobs the person worked on will be displayed under the Jobs list.\n"
            + "Format: " + COMMAND_WORD + " INDEX. \n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EXPORT_PERSON_SUCCESS = "%s's details were "
            + "exported to a .CSV in your data folder using their name. \n"
            + "Now displaying the jobs that they were assigned to.\n"
            + "Use the \"list\" command to see all jobs again.";

    public static final String MESSAGE_EXPORT_PERSON_FAILURE = "Failed to export "
            + "due to a problem with saving.";

    private final Index targetIndex;

    public ExportCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
        }

        Person personToExport = lastShownList.get(targetIndex.getZeroBased());
        try {
            Exporter.getNewInstance(personToExport, model).export();
            return new CommandResult(String.format(MESSAGE_EXPORT_PERSON_SUCCESS, personToExport.getName()));
        } catch (IOException ioException) {
            return new CommandResult(String.format(MESSAGE_EXPORT_PERSON_FAILURE));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportCommand // instanceof handles nulls
                && targetIndex.equals(((ExportCommand) other).targetIndex)); // state check
    }
}
