package peoplesoft.model.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import peoplesoft.model.job.exceptions.DuplicateJobException;
import peoplesoft.model.job.exceptions.JobNotFoundException;

/**
 * Implementation of {@code JobList}.
 */
public class JobListManager implements JobList {

    private final ObservableList<Job> internalList = FXCollections.observableArrayList();
    private final ObservableList<Job> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    @Override
    public boolean contains(String jobId) {
        requireNonNull(jobId);
        return internalList.stream().anyMatch(job -> job.getJobId().equals(jobId));
    }

    @Override
    public void add(Job toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd.getJobId())) {
            throw new DuplicateJobException();
        }
        internalList.add(toAdd);
    }

    @Override
    public void remove(Job toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new JobNotFoundException();
        }
    }

    @Override
    public void setJob(Job targetJob, Job editedJob) {
        requireAllNonNull(targetJob, editedJob);

        int index = internalList.indexOf(targetJob);
        if (index == -1) {
            throw new JobNotFoundException();
        }

        if (!targetJob.isSameJob(editedJob) && contains(editedJob.getJobId())) {
            throw new DuplicateJobException();
        }

        internalList.set(index, editedJob);
    }

    public void setJobs(JobListManager replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    @Override
    public void setJobs(List<Job> jobs) {
        requireAllNonNull(jobs);
        if (!jobsAreUnique(jobs)) {
            throw new DuplicateJobException();
        }
        internalList.setAll(jobs);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Job> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns true if {@code jobs} contains only unique persons.
     */
    @Override
    public boolean jobsAreUnique(List<Job> jobs) {
        for (int i = 0; i < jobs.size() - 1; i++) {
            for (int j = i + 1; j < jobs.size(); j++) {
                if (jobs.get(i).isSameJob(jobs.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobListManager // instanceof handles nulls
                && internalList.equals(((JobListManager) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public Iterator<Job> iterator() {
        return internalList.iterator();
    }
}
