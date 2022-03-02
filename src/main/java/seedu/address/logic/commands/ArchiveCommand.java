package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Archives the address book data to the given {@code Path}.
 */
public class ArchiveCommand extends Command {
    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Archives the address book data to a file (if a filename is provided), or to data/archive-YEAR-MONTH-DATE.json.\n"
            + "Parameters: [FILENAME]\n"
            + "Example: " + COMMAND_WORD + " backup-2021-12.json";

    public static final String MESSAGE_ARCHIVE_PERSON_SUCCESS = "Archived data file to: %1$s";
    public static final String MESSAGE_ARCHIVE_FILE_EXISTS = "File %1$s already exists!";
    public static final String MESSAGE_ARCHIVE_PERSON_ERROR = "Unable to archive data file!";

    private final Path targetPath;

    public ArchiveCommand(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // ehhhhh, might be better to just use the Storage instance in LogicManager
        // and do something like storage.saveAddressBook(addressBook, path)
        Path actualPath = model.getAddressBookFilePath().getParent().resolve(targetPath);
        AddressBookStorage storage = new JsonAddressBookStorage(actualPath);

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (FileAlreadyExistsException e) {
            throw new CommandException(String.format(MESSAGE_ARCHIVE_FILE_EXISTS, actualPath), e);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_ARCHIVE_PERSON_ERROR, e);
        }

        return new CommandResult(String.format(MESSAGE_ARCHIVE_PERSON_SUCCESS, actualPath));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ArchiveCommand // instanceof handles nulls
                && targetPath.equals(((ArchiveCommand) other).targetPath)); // state check
    }
}
