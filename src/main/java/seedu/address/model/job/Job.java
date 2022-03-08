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

    private final boolean isPaid;

    private final Set<Person> persons = new HashSet<>();

    /**
     * Constructor for an immutable job.
     * All fields must not be null.
     */
    public Job(String name, Money rate, float duration, boolean isPaid, Set<Person> persons) {
        requireAllNonNull(name, rate, duration, isPaid, persons);
        this.name = name;
        this.rate = rate;
        this.duration = duration;
        this.isPaid = isPaid;
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

    public boolean isPaid() {
        return isPaid;
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
        return new Job(name, rate, duration, true, persons);
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
                && otherJob.isPaid() == isPaid()
                && otherJob.getPersons().equals(getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rate, duration, isPaid, persons);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
