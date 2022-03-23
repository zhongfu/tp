package peoplesoft.model;

import javafx.collections.ObservableList;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the jobs list.
     * This list will not contain any duplicate jobs.
     */
    ObservableList<Job> getJobList();
}
