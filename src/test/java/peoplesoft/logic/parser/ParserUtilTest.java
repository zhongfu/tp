package peoplesoft.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;
import static peoplesoft.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Rate;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;
import peoplesoft.model.util.ID;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_RATE_1 = "hello";
    private static final String INVALID_RATE_2 = "-5";
    private static final String INVALID_RATE_3 = "1000001";
    private static final String INVALID_DURATION_1 = "world";
    private static final String INVALID_DURATION_2 = "-5";
    private static final String INVALID_DURATION_3 = "0";
    private static final String INVALID_DURATION_4 = "1000001";
    private static final String INVALID_ID = "@special_characters";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_STRING = "hello";
    private static final String VALID_RATE_1 = "3.0";
    private static final String VALID_RATE_2 = "0";
    private static final String VALID_DURATION = "1.5";
    private static final String VALID_ID = "a123-12ab3";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, Index.MESSAGE_CONSTRAINTS, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseIndexes_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseIndexes(null));
    }

    @Test
    public void parseIndexes_collectionWithInvalidIndexes_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndexes(Arrays.asList("1", "ahh")));
    }

    @Test
    public void parseIndexes_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseIndexes(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseIndexes_collectionWithValidIndexes_returnsIndexSet() throws Exception {
        Set<Index> actualIndexSet = ParserUtil.parseIndexes(Arrays.asList("4", "3"));
        Set<Index> expectedIndexSet = new HashSet<Index>(Arrays.asList(Index.fromOneBased(4),
                Index.fromOneBased(3)));

        assertEquals(expectedIndexSet, actualIndexSet);
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseString(null));
    }

    @Test
    public void parseString_whitespace_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseString(WHITESPACE));
    }

    @Test
    public void parseString_validValue_returnsString() throws Exception {
        assertEquals(VALID_STRING, ParserUtil.parseString(VALID_STRING));

        // With whitespace
        assertEquals(VALID_STRING, ParserUtil.parseString(WHITESPACE + VALID_STRING + WHITESPACE));
    }

    @Test
    public void parseRate_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseRate(null));
    }

    @Test
    public void parseRate_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRate(INVALID_RATE_1));
        assertThrows(ParseException.class, () -> ParserUtil.parseRate(INVALID_RATE_2));
        assertThrows(ParseException.class, () -> ParserUtil.parseRate(INVALID_RATE_3));
    }

    @Test
    public void parseRate_validValue_returnsRate() throws Exception {
        Rate expectedRate = new Rate(new Money(3.0), Duration.ofHours(1));
        assertEquals(expectedRate, ParserUtil.parseRate(VALID_RATE_1));

        // With whitespace
        assertEquals(expectedRate, ParserUtil.parseRate(WHITESPACE + VALID_RATE_1 + WHITESPACE));

        // Rate of zero
        expectedRate = new Rate(new Money(0), Duration.ofHours(1));
        assertEquals(expectedRate, ParserUtil.parseRate(VALID_RATE_2));
    }

    @Test
    public void parseDuration_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDuration(null));
    }

    @Test
    public void parseDuration_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration(INVALID_DURATION_1));
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration(INVALID_DURATION_2));
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration(INVALID_DURATION_3));
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration(INVALID_DURATION_4));
    }

    @Test
    public void parseDuration_validValue_returnsDuration() throws Exception {
        Duration expectedDuration = Duration.ofMinutes(90);
        assertEquals(expectedDuration, ParserUtil.parseDuration(VALID_DURATION));

        // With whitespace
        assertEquals(expectedDuration, ParserUtil.parseDuration(WHITESPACE + VALID_DURATION + WHITESPACE));
    }

    @Test
    public void parseID_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseID(null));
    }

    @Test
    public void parseID_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseID(INVALID_ID));
    }

    @Test
    public void parseID_validValue_returnsString() throws Exception {
        ID expectedId = new ID(VALID_ID);
        assertEquals(expectedId, ParserUtil.parseID(VALID_ID));

        // With whitespace
        assertEquals(expectedId, ParserUtil.parseID(WHITESPACE + VALID_ID + WHITESPACE));
    }
}
