package peoplesoft.logic.commands.job;

import static java.util.Objects.requireNonNull;

import peoplesoft.commons.core.Messages;
import peoplesoft.logic.commands.Command;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.model.Model;
import peoplesoft.model.job.JobContainsKeywordsPredicate;

public class JobFindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String COMMAND_EXAMPLES = COMMAND_WORD + " Painting";

    public static final String COMMAND_FORMAT = COMMAND_WORD + " KEYWORD";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all jobs whose description contains ALL of "
            + "the specified keywords (case-insensitive) and displays them in the list.\n"
            + "Format: "
            + COMMAND_WORD + " "
            + "KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " \"electric aircon appliances\" "
            + "finds all jobs which have \"electric\", \"aircon\" and \"appliances\" in them.";

    private final JobContainsKeywordsPredicate predicate;

    public JobFindCommand(JobContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobList(predicate);
        return new CommandResult(
            String.format(Messages.MSG_JOBS_LISTED_OVERVIEW, model.getFilteredJobList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobFindCommand // instanceof handles nulls
            && predicate.equals(((JobFindCommand) other).predicate)); // state check
    }
}
