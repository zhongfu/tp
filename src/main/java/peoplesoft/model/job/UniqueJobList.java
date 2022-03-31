package peoplesoft.model.job;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.job.exceptions.DuplicateJobException;
import peoplesoft.model.job.exceptions.JobNotFoundException;
import peoplesoft.model.util.ID;

/**
 * Implementation of {@code JobList}.
 */
@JsonSerialize(using = UniqueJobList.UniqueJobListSerializer.class)
@JsonDeserialize(using = UniqueJobList.UniqueJobListDeserializer.class)
public class UniqueJobList implements JobList {

    private final ObservableList<Job> internalList = FXCollections.observableArrayList();
    private final ObservableList<Job> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    @Override
    public boolean contains(ID jobId) {
        requireNonNull(jobId);
        return internalList.stream().anyMatch(job -> job.getJobId().equals(jobId));
    }

    /**
     * Returns the job with the given id.
     *
     * @throws JobNotFoundException if the person does not exist
     */
    @Override
    public Job get(ID jobId) throws JobNotFoundException {
        requireNonNull(jobId);
        return internalList.stream()
            .filter(j -> j != null && jobId.equals(j.getJobId()))
            .findAny()
            .orElseThrow(JobNotFoundException::new);
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
        if (!contains(toRemove.getJobId())) {
            throw new JobNotFoundException();
        }
        internalList.removeIf(job -> job.isSameJob(toRemove));
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

    public void setJobs(UniqueJobList replacement) {
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
                || (other instanceof UniqueJobList // instanceof handles nulls
                && internalList.equals(((UniqueJobList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public Iterator<Job> iterator() {
        return internalList.iterator();
    }

    protected static class UniqueJobListSerializer extends StdSerializer<UniqueJobList> {
        private UniqueJobListSerializer(Class<UniqueJobList> val) {
            super(val);
        }

        private UniqueJobListSerializer() {
            this(null);
        }

        @Override
        public void serialize(UniqueJobList val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(val.asUnmodifiableObservableList());
        }
    }

    protected static class UniqueJobListDeserializer extends StdDeserializer<UniqueJobList> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The job list is invalid or missing!";

        private UniqueJobListDeserializer(Class<?> vc) {
            super(vc);
        }

        private UniqueJobListDeserializer() {
            this(null);
        }

        @Override
        public UniqueJobList deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ArrayNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            List<Job> jobList = node // is ArrayNode
                    .traverse(codec)
                    .readValueAs(new TypeReference<List<Job>>(){});

            UniqueJobList ujl = new UniqueJobList();
            ujl.setJobs(jobList);

            return ujl;
        }

        @Override
        public UniqueJobList getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
