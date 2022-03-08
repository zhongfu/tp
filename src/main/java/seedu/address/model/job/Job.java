package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * Represents a Job. Immutable.
 */
public class Job {

    private final String name;
    private final BigDecimal rate;
    private final BigDecimal duration;

    private final boolean isPaid;

    private final Set<Person> persons = new HashSet<>();

    /**
     * Constructor for an immutable job.
     * All fields must not be null.
     */
    public Job(String name, BigDecimal rate, BigDecimal duration, boolean isPaid, Set<Person> persons) {
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

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Returns the pay of the job.
     * Calculated from rate and duration.
     *
     * @return Pay.
     */
    public BigDecimal calculatePay() {
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
    public boolean equals(Object obj) {
        return super.equals(obj);
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
