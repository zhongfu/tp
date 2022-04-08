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
            + "Format: " + COMMAND_WORD + " INDEX. Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EXPORT_PERSON_SUCCESS = "Exported Person Details: %1$s";

    public static final String MESSAGE_EXPORT_PERSON_FAILURE = "Failed to Export due to IO Error";

    private final Index targetIndex;

    public ExportCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToExport = lastShownList.get(targetIndex.getZeroBased());
        try {
            Exporter.getNewInstance(personToExport, model).export();
            return new CommandResult(String.format(MESSAGE_EXPORT_PERSON_SUCCESS, personToExport));
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
