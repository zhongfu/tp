package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.stream.Stream;

import peoplesoft.logic.commands.job.JobAssignCommand;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ArgumentTokenizer;
import peoplesoft.logic.parser.Prefix;
import peoplesoft.logic.parser.exceptions.ParseException;

/**
 * Parses a {@code jobId} and an {@code index} for {@code Person}.
 */
public class JobAssignCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobAssignCommand}
     * and returns an {@code ArgumentMultimap} for {@code JobAssignCommand}.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ArgumentMultimap parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX)
                || argMultimap.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    JobAssignCommand.MESSAGE_USAGE));
        }
        return argMultimap;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
