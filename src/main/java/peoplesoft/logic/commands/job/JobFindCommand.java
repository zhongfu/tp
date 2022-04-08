package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import peoplesoft.commons.core.Messages;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.model.Model;
import peoplesoft.model.job.JobContainsKeywordsPredicate;

public class JobFindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all jobs whose description contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " electric aircon appliances";

    private final JobContainsKeywordsPredicate predicate;

    public JobFindCommand(JobContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobList(predicate);
        return new CommandResult(
            String.format(Messages.MESSAGE_JOBS_LISTED_OVERVIEW, model.getFilteredJobList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobFindCommand // instanceof handles nulls
            && predicate.equals(((JobFindCommand) other).predicate)); // state check
    }
}
