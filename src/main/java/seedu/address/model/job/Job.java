package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * Represents a job. Immutable.
 */
public class Job {

    private final String jobId;
    private final String desc;
    private final Rate rate;
    private final Duration duration;

    private final boolean hasPaid;

    private final Set<Person> persons = new HashSet<>();

    /**
     * Constructor for an immutable job.
     * All fields must not be null.
     */
    public Job(String jobId, String desc, Rate rate, Duration duration, boolean hasPaid, Set<Person> persons) {
        requireAllNonNull(jobId, desc, rate, duration, hasPaid, persons);
        this.jobId = jobId;
        this.desc = desc;
        this.rate = rate;
        this.duration = duration;
        this.hasPaid = hasPaid;
        this.persons.addAll(persons);
    }

    public String getJobId() {
        return jobId;
    }

    public String getDesc() {
        return desc;
    }

    public Rate getRate() {
        return rate;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean hasPaid() {
        return hasPaid;
    }

    /**
     * Returns an immutable person set.
     *
     * @return Immutable set of persons.
     */
    public Set<Person> getPersons() {
        return Collections.unmodifiableSet(persons);
    }

    /**
     * Returns the pay of the job.
     * Calculated from rate and duration.
     *
     * @return Pay.
     */
    public Money calculatePay() {
        return rate.calculateAmount(duration);
    }

    /**
     * Returns a new instance of the job with isPaid as true;
     *
     * @return Paid job.
     */
    public Job setAsPaid() {
        return new Job(jobId, desc, rate, duration, true, persons);
    }

    /**
     * Returns a new instance of the job with isPaid as false;
     *
     * @return Unpaid job.
     */
    public Job setAsNotPaid() {
        return new Job(jobId, desc, rate, duration, false, persons);
    }

    /**
     * Returns true if both jobs have the same {@code jobId}.
     * This defines a weaker notion of equality between two jobs.
     *
     * @param other the other Job to compare against
     * @return true if both jobs have the same {@code jobId}
     */
    public boolean isSameJob(Job other) {
        if (other == this) {
            return true;
        }

        return other != null
            && other.getJobId().equals(getJobId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Job)) {
            return false;
        }
        Job otherJob = (Job) other;
        return otherJob.getJobId().equals(getJobId())
                && otherJob.getDesc().equals(getDesc())
                && otherJob.getRate().equals(getRate())
                && otherJob.getDuration().equals(getDuration())
                && otherJob.hasPaid() == hasPaid()
                && otherJob.getPersons().equals(getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, desc, rate, duration, hasPaid, persons);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
