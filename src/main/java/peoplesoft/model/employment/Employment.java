package peoplesoft.model.employment;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.Model;
import peoplesoft.model.employment.exceptions.DuplicateEmploymentException;
import peoplesoft.model.employment.exceptions.EmploymentNotFoundException;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * Association class to handle assigning {@code Jobs} to {@code Persons}.
 */
@JsonSerialize(using = Employment.EmploymentSerializer.class)
@JsonDeserialize(using = Employment.EmploymentDeserializer.class)
public class Employment {
    // TODO deserializing an Employment instance with invalid IDs.
    /**
     * Singleton instance.
     */
    private static Employment instance;

    /**
     * Maps {@code JobId} to {@code PersonId}.
     */
    private Map<ID, Set<ID>> map;

    /**
     * Constructor for a new employment.
     */
    public Employment() {
        map = new HashMap<>();
    }

    /**
     * Constructor used for serdes.
     *
     * @param map Map of values
     */
    Employment(Map<ID, Set<ID>> map) {
        requireNonNull(map);
        this.map = new HashMap<>();
        for (Map.Entry<ID, Set<ID>> e : map.entrySet()) {
            this.map.put(e.getKey(), new TreeSet<>(e.getValue()));
        }
    }

    /**
     * Adds an association of a {@code Job} with a {@code Person}.
     * Throws an exception if the association already exists.
     *
     * @param job Job.
     * @param person Person.
     * @throws DuplicateEmploymentException Throws if association already exists.
     */
    public void associate(Job job, Person person) {
        requireAllNonNull(job, person);
        if (!map.containsKey(job.getJobId())) {
            map.put(job.getJobId(), new TreeSet<>());
        }
        // Guaranteed to be non-null and Set handles duplicates
        if (!map.get(job.getJobId()).add(person.getPersonId())) {
            throw new DuplicateEmploymentException();
        }
    }

    /**
     * Removes an association of a {@code Job} with a {@code Person}.
     * Throws an exception if the association is not found.
     *
     * @param job Job.
     * @param person Person.
     * @throws EmploymentNotFoundException Throws if association is not found.
     */
    public void disassociate(Job job, Person person) throws EmploymentNotFoundException {
        requireAllNonNull(job, person);
        // Short-circuit evaluation
        if (!map.containsKey(job.getJobId()) || !map.get(job.getJobId()).contains(person.getPersonId())) {
            throw new EmploymentNotFoundException();
        }
        // Guaranteed to be present
        map.get(job.getJobId()).remove(person.getPersonId());
        if (map.get(job.getJobId()).isEmpty()) {
            map.remove(job.getJobId());
        }
    }

    /**
     * Deletes all entries of a {@code Person}.
     *
     * @param person {@code Person} to delete.
     */
    public void deletePerson(Person person) {
        requireAllNonNull(person);
        for (Map.Entry<ID, Set<ID>> e : map.entrySet()) {
            e.getValue().removeIf(p -> p.equals(person.getPersonId()));
        }
        // If list of IDs for persons is empty, remove the entry from the map
        map.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    /**
     * Deletes all entries of a {@code Job}.
     *
     * @param job {@code Job} to delete.
     */
    public void deleteJob(Job job) {
        requireAllNonNull(job);
        map.remove(job.getJobId());
    }

    /**
     * Returns a list of {@code Jobs} that a {@code Person} has.
     * Also updates the FilteredJobList for UI.
     *
     * @param person Person.
     * @param model Model.
     * @return List of jobs.
     */
    public List<Job> getJobs(Person person, Model model) {
        requireAllNonNull(person, model);

        List<Job> jobs = new ArrayList<>();

        for (Map.Entry<ID, Set<ID>> entry : map.entrySet()) {
            if (entry.getValue().contains(person.getPersonId())) {
                jobs.add(model.getJob(entry.getKey()));
            }
        }

        return jobs;
    }

    /**
     * Returns a list of {@code Person}s assigned to a {@code Job}.
     * Also updates the FilteredPersonList for UI.
     *
     * @param job Job.
     * @param model Model.
     * @return List of persons.
     */
    public List<Person> getPersons(Job job, Model model) {
        requireAllNonNull(job, model);

        if (!map.containsKey(job.getJobId())) {
            return List.of();
        } else {
            return map.get(job.getJobId()).stream()
                    .map(model::getPerson)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns a list of {@code Persons} assigned to a {@code Job}.
     *
     * @return Map of jobs.
     */
    public Map<ID, Set<ID>> getAllJobs() {
        return map;
    }

    /**
     * Sets the singleton instance of {@code Employment}.
     *
     * @param employment Instance to set.
     */
    public static void setInstance(Employment employment) {
        requireNonNull(employment);
        instance = employment;
    }

    /**
     * Creates a new singleton instance of {@code Employment}.
     *
     */
    public static void newInstance() {
        instance = new Employment();
    }

    /**
     * Returns the instance of {@code Employment}.
     *
     * @return {@code Employment} instance.
     */
    public static Employment getInstance() {
        if (instance == null) {
            instance = new Employment();
        }
        return instance;
    }

    protected static class EmploymentSerializer extends StdSerializer<Employment> {
        private EmploymentSerializer(Class<Employment> val) {
            super(val);
        }

        private EmploymentSerializer() {
            this(null);
        }

        @Override
        public void serialize(Employment value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeObject(value.map);
        }
    }

    protected static class EmploymentDeserializer extends StdDeserializer<Employment> {
        private static final String MISSING_OR_INVALID_INSTANCE = "Invalid employment!";

        private EmploymentDeserializer(Class<?> vc) {
            super(vc);
        }

        private EmploymentDeserializer() {
            this(null);
        }

        @Override
        public Employment deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            // readValueAs Map is ok because we know `node` has to be a json object
            Map<ID, Set<ID>> map = node
                    .traverse(codec)
                    .readValueAs(new TypeReference<Map<ID, Set<ID>>>(){});

            return new Employment(map);
        }

        @Override
        public Employment getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
