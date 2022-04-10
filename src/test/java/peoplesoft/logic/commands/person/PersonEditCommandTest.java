package peoplesoft.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.logic.commands.CommandTestUtil.DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandFailure;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.logic.commands.CommandTestUtil.showPersonAtIndex;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static peoplesoft.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.ClearCommand;
import peoplesoft.logic.commands.person.PersonEditCommand.EditPersonDescriptor;
import peoplesoft.model.AddressBook;
import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.person.Person;
import peoplesoft.testutil.EditPersonDescriptorBuilder;
import peoplesoft.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for PersonEditCommand.
 */
public class PersonEditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder()
                .withId(model.getFilteredPersonList().get(0).getPersonId()) // preserve current ID
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        PersonEditCommand personEditCommand = new PersonEditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(PersonEditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(personEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        PersonEditCommand personEditCommand = new PersonEditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(PersonEditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(personEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        PersonEditCommand personEditCommand = new PersonEditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(PersonEditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(personEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        PersonEditCommand personEditCommand = new PersonEditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(PersonEditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(personEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        PersonEditCommand personEditCommand = new PersonEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(personEditCommand, model, Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of database
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of database list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        PersonEditCommand personEditCommand = new PersonEditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(personEditCommand, model, Messages.MSG_INVALID_PERSON_DISPLAYED_IDX);
    }

    @Test
    public void equals() {
        final PersonEditCommand standardCommand = new PersonEditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        PersonEditCommand commandWithSameValues = new PersonEditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new PersonEditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new PersonEditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

}
