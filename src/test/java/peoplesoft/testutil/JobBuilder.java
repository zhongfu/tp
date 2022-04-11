package peoplesoft.testutil;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import peoplesoft.commons.core.JobIdFactory;
import peoplesoft.model.job.Job;
import peoplesoft.model.util.ID;

/**
 * A utility class to help with building Job objects.
 */
public class JobBuilder {

    public static final String DEFAULT_DESC = "Aircon maintanence";
    public static final int DEFAULT_DURATION = 5;
    public static final boolean DEFAULT_HASPAID = false;
    public static final boolean DEFAULT_ISFINAL = false;

    private ID jobId;
    private String desc;
    private Duration duration;
    private boolean hasPaid;
    private boolean isFinal;

    /**
     * Creates a {@code JobBuilder} with the default details.
     */
    public JobBuilder() {
        jobId = JobIdFactory.nextId();
        desc = DEFAULT_DESC;
        duration = Duration.ofHours(5);
        hasPaid = DEFAULT_HASPAID;
        isFinal = DEFAULT_ISFINAL;
    }

    /**
     * Initializes the JobBuilder with the data of {@code jobToCopy}.
     */
    public JobBuilder(Job jobToCopy) {
        jobId = jobToCopy.getJobId();
        desc = jobToCopy.getDesc();
        duration = jobToCopy.getDuration();
        hasPaid = jobToCopy.hasPaid();
        isFinal = jobToCopy.isFinal();
    }

    /**
     * Sets the {@code jobId} of the {@code job} that we are building.
     */
    public JobBuilder withId(ID jobId) {
        requireNonNull(jobId);
        this.jobId = jobId;
        return this;
    }

    /**
     * Sets the {@code jobId} of the {@code Job} that we are building to the current JobIdFactory id.
     */
    public JobBuilder withCurrentId() {
        return withId(new ID(JobIdFactory.getId()));
    }

    /**
     * Sets the {@code jobId} of the {@code Job} that we are building to the current JobIdFactory id.
     */
    public JobBuilder withNextId() {
        return withId(new ID(JobIdFactory.getId() + 1));
    }

    /**
     * Sets the {@code duration} of the {@code Job} that we are building.
     */
    public JobBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Sets the {@code hasPaid} of the {@code Job} that we are building.
     */
    public JobBuilder withHasPaid(Boolean hasPaid) {
        this.hasPaid = hasPaid;
        return this;
    }

    /**
     * Sets the {@code isFinal} of the {@code Job} that we are building.
     */
    public JobBuilder withIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Job} that we are building.
     */
    public JobBuilder withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Job build() {
        return new Job(jobId, desc, duration, hasPaid, isFinal);
    }

}
