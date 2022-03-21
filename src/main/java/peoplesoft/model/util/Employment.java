package peoplesoft.model.util;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.List;

import peoplesoft.model.Model;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;

/**
 * Association class to handle assigning {@code Jobs} to {@code Persons}.
 */
// TODO: Refactor class name/package if necessary
public class Employment {
    // TODO: Feel free to change implementation
    // Made it a singleton because did not know where to put it.
    /**
     * Maps {@code JobId} to {@code Name}.
     */
    private static HashMap<String, Name> map = new HashMap<>();

    /**
     * Adds an association of a {@code Job} with a {@code Person}.
     *
     * @param job Job.
     * @param person Person.
     */
    // TODO: There is an issue where if a person gets edited/deleted, the
    // association would not update. Also currently does not handle serdes.
    public static void associate(Job job, Person person) {
        requireAllNonNull(job, person);
        // The nature of put assigns 1 job to 1 person
        map.put(job.getJobId(), person.getName());
    }

    /**
     * Returns a list of {@code Jobs} that a {@code Person} has.
     *
     * @param person Person.
     * @param model Model.
     * @return List of jobs.
     */
    public static List<Job> getJobs(Person person, Model model) {
        requireAllNonNull(person, model);
        // TODO: Scuffed but workable, change if needed.
        model.updateFilteredJobList(job -> map.get(job.getJobId()).equals(person.getName()));
        return model.getFilteredJobList();
    }
}
