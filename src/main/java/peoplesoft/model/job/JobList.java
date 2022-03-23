package peoplesoft.model.job;

import java.util.List;

import javafx.collections.ObservableList;

public interface JobList extends Iterable<Job> {

    // TODO: should jobs compare by jobs or jobId, since add it seems more intuitive
    // to compare by jobs but delete seems more intuitive to compare by jobId.
    // For now it is jobId.
    boolean contains(String jobId);

    void add(Job toAdd);

    void remove(Job toRemove);

    void setJob(Job targetJob, Job editedJob);

    void setJobs(List<Job> jobs);

    ObservableList<Job> asUnmodifiableObservableList();

    boolean jobsAreUnique(List<Job> jobs);

}
