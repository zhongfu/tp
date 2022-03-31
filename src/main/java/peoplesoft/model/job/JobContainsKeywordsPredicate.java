package peoplesoft.model.job;

import java.util.List;
import java.util.function.Predicate;

import peoplesoft.commons.util.StringUtil;

public class JobContainsKeywordsPredicate implements Predicate<Job> {

    private final List<String> keywords;

    public JobContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Job job) {
        return !keywords.isEmpty() && keywords.stream()
            .allMatch(keyword -> StringUtil.containsWordIgnoreCase(job.getDesc(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof JobContainsKeywordsPredicate // instanceof handles nulls
            && keywords.equals(((JobContainsKeywordsPredicate) other).keywords)); // state check
    }
}
