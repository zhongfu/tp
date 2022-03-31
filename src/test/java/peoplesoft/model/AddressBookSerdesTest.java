package peoplesoft.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static peoplesoft.testutil.PersonUtil.serializePerson;
import static peoplesoft.testutil.TestUtil.serializeList;
import static peoplesoft.testutil.TestUtil.serializeObject;
import static peoplesoft.testutil.TestUtil.toNormalizedJsonString;
import static peoplesoft.testutil.TypicalPersons.ALICE;
import static peoplesoft.testutil.TypicalPersons.BENSON;
import static peoplesoft.testutil.TypicalPersons.CARL;
import static peoplesoft.testutil.TypicalPersons.DANIEL;
import static peoplesoft.testutil.TypicalPersons.ELLE;
import static peoplesoft.testutil.TypicalPersons.FIONA;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import peoplesoft.commons.core.JobIdFactory;
import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;

public class AddressBookSerdesTest {
    @Test
    public void serialize_validNonEmptyAttrs_returnsValidSerialization() throws JsonProcessingException {
        List<Person> personList = Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA);
        List<Job> jobList = List.of();

        AddressBook ab = new AddressBook();
        ab.setPersons(personList);
        ab.setJobs(jobList);

        List<String> serializedPersonList = personList.stream()
                .map((p) -> serializePerson(p))
                .collect(Collectors.toList());

        // TODO
        /*List<String> serializedJobList = jobList.stream()
            .map((j) -> serializeJob(j))
            .collect(Collectors.toList());*/

        String serializedEmployment = JsonUtil.toJsonString(Employment.getInstance().getAllJobs());

        String serializedJobIdState = String.valueOf(JobIdFactory.getId());
        String serializedPersonIdState = String.valueOf(PersonIdFactory.getId());

        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("persons", serializeList(serializedPersonList));
        entries.put("jobs", serializeList(List.of())); // TODO
        entries.put("employment", serializedEmployment);
        entries.put("jobIdState", serializedJobIdState);
        entries.put("personIdState", serializedPersonIdState);

        String serialized = serializeObject(entries);

        assertEquals(serialized, toNormalizedJsonString(ab));
    }

    @Test
    public void serialize_emptyList_returnsValidSerialization() throws JsonProcessingException {
        AddressBook ab = new AddressBook();

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("persons", serializeList(List.of()));
        map.put("jobs", serializeList(List.of()));
        map.put("employment", JsonUtil.toJsonString(Employment.getInstance().getAllJobs()));
        map.put("jobIdState", JsonUtil.toJsonString(JobIdFactory.getId()));
        map.put("personIdState", JsonUtil.toJsonString(PersonIdFactory.getId()));

        String serialized = serializeObject(map);
        // TODO not sure if these are deterministic

        assertEquals(serialized, toNormalizedJsonString(ab));
    }

    /**
     * Tests Person.PersonDeserializer.getNullValue().
     */
    @Test
    public void deserialize_null_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("null", AddressBook.class));
    }

    @Test
    public void deserialize_nonObjectValue_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("385", AddressBook.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"string\"", AddressBook.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(
                "[\"i'm\",\"an\",\"array\"]", AddressBook.class));
    }

    @Test
    public void deserialize_invalidAddressBookObject_throwsJsonMappingException() throws IOException {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(
                serializeObject(Map.of("person", "[]")), // should be "persons"
                AddressBook.class));

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(
                serializeObject(Map.of()),
                AddressBook.class));

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(
                serializeObject(Map.of("persons", "null")),
                AddressBook.class));
    }

    @Test
    public void deserialize_invalidPersonElement_throwsJsonMappingException() {
        String serializedList = serializeList(Arrays.asList(
                serializePerson(ALICE),
                serializePerson(
                        "15",
                        "R@chel",
                        BENSON.getPhone().toString(),
                        BENSON.getAddress().toString(),
                        BENSON.getEmail().toString(),
                        BENSON.getRate().getAmount().printFullValue(),
                        Collections.singleton("friend"),
                        Set.of()),
                serializePerson(ELLE)));

        String serializedObject = serializeObject(Map.of("persons", serializedList));

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(serializedObject, AddressBook.class));
    }

    @Test
    public void deserialize_validSerialization_returnsAddressBook() throws IOException {
        List<Person> personList = Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA);
        List<Job> jobList = List.of();

        AddressBook ab = new AddressBook();
        ab.setPersons(personList);
        ab.setJobs(jobList);

        List<String> serializedPersonList = personList.stream()
                .map((p) -> serializePerson(p))
                .collect(Collectors.toList());

        // TODO
        /*List<String> serializedJobList = jobList.stream()
            .map((j) -> serializeJob(j))
            .collect(Collectors.toList());*/

        String serializedEmployment = JsonUtil.toJsonString(Employment.getInstance().getAllJobs());

        int id = JobIdFactory.getId();
        String serializedJobIdState = JsonUtil.toJsonString(id);

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("persons", serializeList(serializedPersonList));
        map.put("jobs", serializeList(List.of())); // TODO
        map.put("employment", serializedEmployment);
        map.put("jobIdState", serializedJobIdState);

        String serialized = serializeObject(map);

        assertEquals(ab, JsonUtil.fromJsonString(serialized, AddressBook.class));
        // Checks if employment and jobIdState gets serialized correctly
        assertEquals(Employment.getInstance().getAllJobs(),
                JsonUtil.fromJsonString(serializedEmployment, HashMap.class));
        assertEquals(id, JsonUtil.fromJsonString(serializedJobIdState, int.class));
        // TODO if needed
    }
}
