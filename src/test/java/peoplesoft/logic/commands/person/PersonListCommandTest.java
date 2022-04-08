package peoplesoft.logic.commands.person;

import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.logic.commands.CommandTestUtil.showPersonAtIndex;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for PersonListCommand.
 */
public class PersonListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new PersonListCommand(), model, PersonListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new PersonListCommand(), model, PersonListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
