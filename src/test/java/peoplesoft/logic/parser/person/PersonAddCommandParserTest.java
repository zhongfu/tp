package peoplesoft.logic.parser.person;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_RATE_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static peoplesoft.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static peoplesoft.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static peoplesoft.logic.commands.CommandTestUtil.RATE_DESC_AMY;
import static peoplesoft.logic.commands.CommandTestUtil.RATE_DESC_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static peoplesoft.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_RATE_BOB;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static peoplesoft.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseFailure;
import static peoplesoft.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static peoplesoft.testutil.TypicalPersons.AMY;
import static peoplesoft.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.model.money.Rate;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;
import peoplesoft.testutil.PersonBuilder;

public class PersonAddCommandParserTest {
    private PersonAddCommandParser parser = new PersonAddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        PersonBuilder expectedPersonBuilder = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple rates - last rate accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + RATE_DESC_AMY + RATE_DESC_BOB + TAG_DESC_FRIEND,
                new PersonAddCommand(expectedPersonBuilder.withNextId().build()));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .withNextId().build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new PersonAddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().withNextId().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + RATE_DESC_AMY, new PersonAddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MSG_INVALID_CMD_FORMAT, PersonAddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                + RATE_DESC_BOB, expectedMessage);

        // missing rate prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + VALID_RATE_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                + VALID_RATE_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid rate
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_RATE_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Rate.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RATE_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + RATE_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + RATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MSG_INVALID_CMD_FORMAT, PersonAddCommand.MESSAGE_USAGE));
    }
}
