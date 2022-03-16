package seedu.address.model.job;

import java.util.List;

import javafx.collections.ObservableList;

public interface JobList extends Iterable<Job> {

    boolean contains(Job toCheck);

    void add(Job toAdd);

    void remove(Job toRemove);

    void setJob(Job targetJob, Job editedJob);

    void setJobs(JobList replacement);

    void setJobs(List<Job> jobList);

    ObservableList<Job> asUnmodifiableObservableList();

    boolean jobsAreUnique();

}
