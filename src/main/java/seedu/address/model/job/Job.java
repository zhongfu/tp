package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * Represents a job. Immutable.
 */
public class Job {

    private final String name;
    private final Money rate;
    private final float duration;

    private final boolean hasPaid;

    private final Set<Person> persons = new HashSet<>();

    /**
     * Constructor for an immutable job.
     * All fields must not be null.
     */
    public Job(String name, Money rate, float duration, boolean hasPaid, Set<Person> persons) {
        requireAllNonNull(name, rate, duration, hasPaid, persons);
        this.name = name;
        this.rate = rate;
        this.duration = duration;
        this.hasPaid = hasPaid;
        this.persons.addAll(persons);
    }

    public String getName() {
        return name;
    }

    public Money getRate() {
        return rate;
    }

    public float getDuration() {
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
        return rate.multiply(duration);
    }

    /**
     * Returns a new instance of the job with isPaid as true;
     *
     * @return Paid job.
     */
    public Job setAsPaid() {
        return new Job(name, rate, duration, true, persons);
    }

    /**
     * Returns a new instance of the job with isPaid as false;
     *
     * @return Unpaid job.
     */
    public Job setAsNotPaid() {
        return new Job(name, rate, duration, false, persons);
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
        return otherJob.getName().equals(getName())
                && otherJob.getRate().equals(getRate())
                && otherJob.getDuration() == getDuration()
                && otherJob.hasPaid() == hasPaid()
                && otherJob.getPersons().equals(getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rate, duration, hasPaid, persons);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
