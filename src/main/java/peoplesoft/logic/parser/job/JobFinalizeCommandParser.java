package peoplesoft.logic.parser.job;

import static peoplesoft.commons.core.Messages.MSG_INVALID_CMD_FORMAT;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_CONFIRMATION;

import java.util.stream.Stream;

import peoplesoft.commons.core.index.Index;
import peoplesoft.logic.commands.job.JobFinalizeCommand;
import peoplesoft.logic.parser.ArgumentMultimap;
import peoplesoft.logic.parser.ArgumentTokenizer;
import peoplesoft.logic.parser.Parser;
import peoplesoft.logic.parser.ParserUtil;
import peoplesoft.logic.parser.Prefix;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.model.employment.Employment;

/**
 * Parses an {@code Index} of a {@code Job} to finalize.
 */
public class JobFinalizeCommandParser implements Parser<JobFinalizeCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code JobFinalizeCommand}
     * and returns a {@code JobFinalizeCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public JobFinalizeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CONFIRMATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_CONFIRMATION) || argMultimap.getPreamble().isBlank()
                || !argMultimap.getValue(PREFIX_CONFIRMATION).get().isBlank()) {
            throw new ParseException(String.format(MSG_INVALID_CMD_FORMAT,
                JobFinalizeCommand.MESSAGE_USAGE));
        }
        try {
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            return new JobFinalizeCommand(index, Employment.getInstance());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MSG_INVALID_CMD_FORMAT, JobFinalizeCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
