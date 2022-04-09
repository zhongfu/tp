package peoplesoft.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javafx.collections.ObservableList;
import peoplesoft.commons.core.JobIdFactory;
import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.JobList;
import peoplesoft.model.job.UniqueJobList;
import peoplesoft.model.job.exceptions.JobNotFoundException;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.UniquePersonList;
import peoplesoft.model.person.exceptions.PersonNotFoundException;
import peoplesoft.model.util.ID;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
@JsonSerialize(using = AddressBook.AddressBookSerializer.class)
@JsonDeserialize(using = AddressBook.AddressBookDeserializer.class)
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private JobList jobs;

    /**
     * Creates an empty AddressBook.
     */
    public AddressBook() {
        persons = new UniquePersonList();
        jobs = new UniqueJobList();
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}.
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Creates an {@code AddressBook} using the given {@code UniquePersonList} and {@code UniqueJobList}.
     * Only used by {@code AddressBookDeserializer}.
     *
     * @param upl the {@code UniquePersonList} for the new instance
     * @param ujl the {@code UniqueJobList} for the new instance
     */
    private AddressBook(UniquePersonList upl, UniqueJobList ujl) {
        persons = upl;
        jobs = ujl;
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the job list with {@code jobs}.
     * {@code jobs} must not contain duplicate jobs.
     */
    public void setJobs(List<Job> jobs) {
        this.jobs.setJobs(jobs);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setJobs(newData.getJobList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the given data fields exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the given id exists in the address book.
     */
    public boolean hasPerson(ID personId) {
        requireNonNull(personId);
        return persons.contains(personId);
    }

    /**
     * Returns the person with the given id.
     *
     * @throws PersonNotFoundException if there is no such person
     */
    public Person getPerson(ID personId) throws PersonNotFoundException {
        requireNonNull(personId);
        return persons.get(personId);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// job-level operations

    /**
     * Returns true if a job with the same identity as {@code job} exists in the address book.
     */
    public boolean hasJob(ID jobId) {
        requireNonNull(jobId);
        return jobs.contains(jobId);
    }

    /**
     * Returns the job with the given id.
     *
     * @throws JobNotFoundException if there is no such job
     */
    public Job getJob(ID jobId) throws JobNotFoundException {
        requireNonNull(jobId);
        return jobs.get(jobId);
    }

    /**
     * Adds a job to the address book.
     * The job must not already exist in the address book.
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * Replaces the given job {@code target} in the list with {@code editedJob}.
     * {@code target} must exist in the address book.
     * The job identity of {@code editedJob} must not be the same as another existing job in the address book.
     */
    public void setJob(Job target, Job editedJob) {
        requireNonNull(editedJob);

        jobs.setJob(target, editedJob);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeJob(Job key) {
        jobs.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Job> getJobList() {
        return jobs.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons)
                && jobs.equals(((AddressBook) other).jobs));
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, jobs);
    }

    protected static class AddressBookSerializer extends StdSerializer<AddressBook> {
        private AddressBookSerializer(Class<AddressBook> val) {
            super(val);
        }

        private AddressBookSerializer() {
            this(null);
        }

        @Override
        public void serialize(AddressBook val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeObjectField("persons", val.persons);
            gen.writeObjectField("jobs", val.jobs);
            gen.writeObjectField("employment", Employment.getInstance());
            gen.writeNumberField("jobIdState", JobIdFactory.getId());
            gen.writeNumberField("personIdState", PersonIdFactory.getId());

            gen.writeEndObject();
        }
    }

    protected static class AddressBookDeserializer extends StdDeserializer<AddressBook> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The address book is invalid or missing!";
        private static final UnaryOperator<String> INVALID_VAL_FMTR =
                k -> String.format("This address book's %s value is invalid!", k);

        private AddressBookDeserializer(Class<?> vc) {
            super(vc);
        }

        private AddressBookDeserializer() {
            this(null);
        }

        private static JsonNode getNonNullNode(ObjectNode node, String key, DeserializationContext ctx)
                throws JsonMappingException {
            return JsonUtil.getNonNullNode(node, key, ctx, INVALID_VAL_FMTR);
        }

        private static <T> T getNonNullNodeWithType(ObjectNode node, String key, DeserializationContext ctx,
                Class<T> cls) throws JsonMappingException {
            return JsonUtil.getNonNullNodeWithType(node, key, ctx,
                INVALID_VAL_FMTR, cls);
        }

        @Override
        public AddressBook deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode objNode = (ObjectNode) node;

            UniquePersonList upl = getNonNullNode(objNode, "persons", ctx)
                    .traverse(codec)
                    .readValueAs(UniquePersonList.class);

            UniqueJobList ujl = getNonNullNode(objNode, "jobs", ctx)
                    .traverse(codec)
                    .readValueAs(UniqueJobList.class);

            if (objNode.has("employment")) {
                Employment emp = objNode.get("employment") // not null, we're good
                        .traverse(codec)
                        .readValueAs(Employment.class);

                Employment.setInstance(emp);
            } else {
                Employment.newInstance();
            }

            if (objNode.has("jobIdState")) {
                // note jobId cannot be negative
                int jobId = Math.max(
                        getNonNullNodeWithType(objNode, "jobIdState", ctx, IntNode.class).intValue(),
                        0);

                // just in case we get a jobId that already exists
                while (ujl.contains(new ID(jobId))) {
                    jobId++;
                }

                JobIdFactory.setId(jobId);
            } else {
                JobIdFactory.setId(0);
            }

            if (objNode.has("personIdState")) {
                // note personId cannot be negative
                int personId = Math.max(
                        getNonNullNodeWithType(objNode, "personIdState", ctx, IntNode.class).intValue(),
                        0);

                // just in case we get a personId that already exists
                while (upl.contains(new ID(personId))) {
                    personId++;
                }

                PersonIdFactory.setId(personId);
            } else {
                PersonIdFactory.setId(0);
            }

            return new AddressBook(upl, ujl);
        }

        @Override
        public AddressBook getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
