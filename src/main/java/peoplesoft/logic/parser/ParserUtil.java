package peoplesoft.logic.parser;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import peoplesoft.commons.core.Messages;
import peoplesoft.commons.core.index.Index;
import peoplesoft.commons.util.StringUtil;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Rate;
import peoplesoft.model.money.exceptions.NegativeMoneyValueException;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;
import peoplesoft.model.util.ID;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        requireNonNull(oneBasedIndex);
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(Index.MESSAGE_CONSTRAINTS);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Set<Index> parseIndexes(Collection<String> oneBasedIndexes) throws ParseException {
        requireNonNull(oneBasedIndexes);
        final Set<Index> indexSet = new HashSet<>();
        for (String index : oneBasedIndexes) {
            indexSet.add(parseIndex(index));
        }
        return indexSet;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code string} is invalid.
     */
    public static String parseString(String str) throws ParseException {
        requireNonNull(str);
        String res = str.trim();
        if (res.isBlank()) {
            throw new ParseException(Messages.MSG_EMPTY_STRING);
        }
        return res;
    }

    /**
     * Parses a {@code Rate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code rate} is invalid.
     */
    public static Rate parseRate(String str) throws ParseException {
        requireNonNull(str);
        String trim = str.trim();
        Rate res;
        try {
            res = new Rate(new Money(Double.parseDouble(trim)), Duration.ofHours(1));
        } catch (NumberFormatException | NegativeMoneyValueException e) {
            // TODO: add message/complex rate parsing %s/%s
            throw new ParseException(Rate.MESSAGE_CONSTRAINTS, e);
        }
        if (isRateTooLarge(res)) {
            throw new ParseException(Rate.MESSAGE_TOO_LARGE);
        }
        return res;
    }

    /**
     * Returns if rate is too large.
     */
    private static boolean isRateTooLarge(Rate rate) {
        return rate.getAmount().getValue().compareTo(BigDecimal.valueOf(1000000)) > 0;
    }

    /**
     * Parses a {@code Duration}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code duration} is invalid.
     */
    public static Duration parseDuration(String str) throws ParseException {
        requireNonNull(str);
        String trim = str.trim();
        double dur;
        try {
            dur = Double.parseDouble(trim);
        } catch (NumberFormatException e) {
            throw new ParseException(Messages.MSG_DURATION_CONSTRAINTS, e);
        }
        Duration res = Duration.ofMinutes(Math.round(dur * 60));
        if (!isDurationNonNegative(res)) {
            throw new ParseException(Messages.MSG_DURATION_CONSTRAINTS);
        }
        if (isDurationTooLarge(res)) {
            throw new ParseException(Messages.MSG_DURATION_TOO_LARGE);
        }
        return res;
    }

    /**
     * Returns if duration is greater than zero.
     */
    private static boolean isDurationNonNegative(Duration dur) {
        return dur.compareTo(Duration.ZERO) > 0;
    }

    /**
     * Returns if duration is too large.
     */
    private static boolean isDurationTooLarge(Duration dur) {
        return dur.compareTo(Duration.ofHours(100000)) > 0;
    }

    /**
     * Parses a {@code String id} into an {@code ID}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code id} is invalid.
     */
    public static ID parseID(String id) throws ParseException {
        requireNonNull(id);
        String trimmedId = id.trim();
        if (!ID.isValidId(trimmedId)) {
            throw new ParseException(ID.MESSAGE_CONSTRAINTS);
        }
        return new ID(trimmedId);
    }
}
