package peoplesoft.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandFailure;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.logic.commands.CommandTestUtil.showPersonAtIndex;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static peoplesoft.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code PersonDeleteCommand}.
 */
public class PersonDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PersonDeleteCommand personDeleteCommand = new PersonDeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(PersonDeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(personDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PersonDeleteCommand personDeleteCommand = new PersonDeleteCommand(outOfBoundIndex);

        assertCommandFailure(personDeleteCommand, model, Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PersonDeleteCommand personDeleteCommand = new PersonDeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(PersonDeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(personDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of database list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        PersonDeleteCommand personDeleteCommand = new PersonDeleteCommand(outOfBoundIndex);

        assertCommandFailure(personDeleteCommand, model, Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
    }

    @Test
    public void equals() {
        PersonDeleteCommand deleteFirstCommand = new PersonDeleteCommand(INDEX_FIRST_PERSON);
        PersonDeleteCommand deleteSecondCommand = new PersonDeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        PersonDeleteCommand deleteFirstCommandCopy = new PersonDeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
