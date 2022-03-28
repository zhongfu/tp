package peoplesoft.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import peoplesoft.model.util.ID;

public class IdTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ID(null));
    }

    @Test
    public void constructor_invalidId_throwsIllegalArgumentException() {
        String invalidId = "";
        assertThrows(IllegalArgumentException.class, () -> new ID(invalidId));
    }

    @Test
    public void isValidId() {
        // null id
        assertThrows(NullPointerException.class, () -> ID.isValidId(null));

        // invalid id
        assertFalse(ID.isValidId("")); // empty string
        assertFalse(ID.isValidId("-")); // hyphen only
        assertFalse(ID.isValidId("^")); // only non-alphanumeric characters
        assertFalse(ID.isValidId("peter-")); // ends with non-alphanumeric characters
        assertFalse(ID.isValidId("-peter")); // starts with non-alphanumeric characters
        assertFalse(ID.isValidId("pe*ter")); // contains invalid characters

        // valid id
        assertTrue(ID.isValidId("peterjack")); // alphabets only
        assertTrue(ID.isValidId("12345")); // numbers only
        assertTrue(ID.isValidId("peterthe2nd")); // alphanumeric characters
        assertTrue(ID.isValidId("CapitalTan50")); // with capital letters
        assertTrue(ID.isValidId("1234abcd-ef56-78gh-ijkl-mno9pqr0stuv")); // long ids
    }
}
