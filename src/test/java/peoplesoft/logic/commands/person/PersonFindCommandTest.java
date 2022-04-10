package peoplesoft.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.commons.core.Messages.MSG_PERSONS_LISTED_OVERVIEW;
import static peoplesoft.logic.commands.CommandTestUtil.assertCommandSuccess;
import static peoplesoft.testutil.TypicalPersons.BENSON;
import static peoplesoft.testutil.TypicalPersons.DANIEL;
import static peoplesoft.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;
import peoplesoft.model.UserPrefs;
import peoplesoft.model.person.PersonContainsKeywordsPredicate;


/**
 * Contains integration tests (interaction with the Model) for {@code PersonFindCommand}.
 */
public class PersonFindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        PersonFindCommand findFirstCommand = new PersonFindCommand(firstPredicate);
        PersonFindCommand findSecondCommand = new PersonFindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        PersonFindCommand findFirstCommandCopy = new PersonFindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MSG_PERSONS_LISTED_OVERVIEW, 0);
        PersonContainsKeywordsPredicate predicate = preparePredicate(" ");
        PersonFindCommand command = new PersonFindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MSG_PERSONS_LISTED_OVERVIEW, 2);
        PersonContainsKeywordsPredicate predicate = preparePredicate("Meier friends");
        PersonFindCommand command = new PersonFindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate}.
     * @return
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        return new PersonContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
