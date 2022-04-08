package peoplesoft.logic.parser.person;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_EMAIL;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_PHONE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_RATE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ArgumentTokenizer;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.Prefix;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.money.Payment;
import peoplesoft.model.money.Rate;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;
import peoplesoft.model.util.ID;

/**
 * Parses input arguments and creates a new PersonAddCommand object
 */
public class PersonAddCommandParser implements Parser<PersonAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PersonAddCommand
     * and returns an PersonAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PersonAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_RATE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_RATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MSG_INVALID_CMD_FORMAT, PersonAddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Rate rate = ParserUtil.parseRate(argMultimap.getValue(PREFIX_RATE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Map<ID, Payment> payments = Map.of();

        Person person = new Person(PersonIdFactory.nextId(), name, phone, email, address, rate, tagList, payments);

        return new PersonAddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
