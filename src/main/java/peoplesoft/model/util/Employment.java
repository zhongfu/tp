package peoplesoft.model.util;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.HashMap;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;

/**
 * Association class to handle assigning {@code Jobs} to {@code Persons}.
 */
@JsonSerialize(using = Employment.EmploymentSerializer.class)
@JsonDeserialize(using = Employment.EmploymentDeserializer.class)
public class Employment {
    // TODO: Refactor class name/package if necessary
    // TODO: Feel free to change implementation
    /**
     * Singleton instance.
     */
    private static Employment instance;

    /**
     * Maps {@code JobId} to {@code Name}.
     */
    private HashMap<ID, Name> map;

    /**
     * Constructor for {@code getInstance}.
     */
    private Employment() {
        map = new HashMap<>();
    }

    public Employment(HashMap<ID, Name> map) {
        this.map = map;
    }

    /**
     * Adds an association of a {@code Job} with a {@code Person}.
     *
     * @param job Job.
     * @param person Person.
     */
    // TODO: There is an issue where if a person gets edited/deleted, the
    // association would not update. Also currently does not handle serdes.
    public void associate(Job job, Person person) {
        requireAllNonNull(job, person);
        // The nature of put assigns 1 job to 1 person
        map.put(job.getJobId(), person.getName());
    }

    /**
     * Deletes all entries of a {@code Person}.
     *
     * @param person {@code Person} to delete.
     */
    public void deletePerson(Person person) {
        requireAllNonNull(person);
        map.entrySet().removeIf(entry -> entry.getValue().equals(person.getName()));
    }

    /**
     * Edits all entries of a {@code Person}.
     *
     * @param toEdit {@code Person} to edit.
     * @param editedPerson {@code Person} that replaces.
     */
    public void editPerson(Person toEdit, Person editedPerson) {
        requireAllNonNull(toEdit, editedPerson);
        map.replaceAll((jobId, name) -> name.equals(toEdit.getName()) ? editedPerson.getName() : name);
    }

    /**
     * Deletes the entry of a {@code Job}.
     *
     * @param job {@code Job} to delete.
     */
    public void deleteJob(Job job) {
        requireAllNonNull(job);
        map.remove(job.getJobId());
    }

    /**
     * Returns a list of {@code Jobs} that a {@code Person} has.
     *
     * @param person Person.
     * @param model Model.
     * @return List of jobs.
     */
    public List<Job> getJobs(Person person, Model model) {
        requireAllNonNull(person, model);
        // TODO: Scuffed but workable, change if needed.
        model.updateFilteredJobList(job -> person.getName().equals(map.get(job.getJobId())));
        return model.getFilteredJobList();
    }

    /**
     * Returns a list of {@code Jobs} that a {@code Person} has.
     *
     * @return Map of jobs.
     */
    public HashMap<ID, Name> getAllJobs() {
        return map;
    }

    /**
     * Sets the singleton instance of {@code Employment}.
     *
     * @param employment Instance to set.
     */
    public static void setInstance(Employment employment) {
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
            HashMap<ID, Name> map = node
                .traverse(codec)
                .readValueAs(new TypeReference<HashMap<ID, Name>>(){});

            return new Employment(map);
        }

        @Override
        public Employment getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
