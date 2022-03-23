package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_DURATION;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_RATE;

import java.time.Duration;
import java.util.stream.Stream;

import peoplesoft.commons.core.JobIdFactory;
import peoplesoft.logic.commands.job.JobAddCommand;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ArgumentTokenizer;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.Prefix;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.Rate;

/**
 * Parses input parameters and returns a {@code Job}.
 */
public class JobAddCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobAddCommand}
     * and returns a {@code Job} object for {@code JobAddCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Job parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME,
                PREFIX_RATE, PREFIX_DURATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_RATE, PREFIX_DURATION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    JobAddCommand.MESSAGE_USAGE));
        }
        String name = ParserUtil.parseString(argMultimap.getValue(PREFIX_NAME).get());
        Rate rate = ParserUtil.parseRate(argMultimap.getValue(PREFIX_RATE).get());
        Duration duration = ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION).get());
        String id = !argMultimap.getPreamble().isBlank()
                ? ParserUtil.parseString(argMultimap.getPreamble())
                : JobIdFactory.nextId(); // Short circuit does not increment

        return new Job(id, name, rate, duration, false);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
