package peoplesoft.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_RATE_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import peoplesoft.model.util.ID;
import peoplesoft.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same id, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withRate(VALID_RATE_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different id, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withId(new ID(500)).build();
        assertFalse(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different rate -> returns false
        editedAlice = new PersonBuilder(ALICE).withRate(VALID_RATE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void isDuplicate() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.isDuplicate(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.isDuplicate(ALICE));

        // different person -> returns false
        assertFalse(ALICE.isDuplicate(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different rate -> returns false
        editedAlice = new PersonBuilder(ALICE).withRate(VALID_RATE_BOB).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.isDuplicate(editedAlice));

        // different ids -> returns true
        editedAlice = new PersonBuilder(ALICE).withId(new ID(1000)).build();
        assertTrue(ALICE.isDuplicate(editedAlice));
    }
}
