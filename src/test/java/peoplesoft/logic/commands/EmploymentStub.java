package peoplesoft.logic.commands;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.employment.exceptions.EmploymentNotFoundException;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * A default employment stub.
 */
public class EmploymentStub extends Employment {
    private static EmploymentStub instance;
    private Map<ID, Set<ID>> map;

    public EmploymentStub() {
        super();
    }

    EmploymentStub(Map<ID, Set<ID>> map) {
        requireNonNull(map);
        this.map = new HashMap<>();
        for (Map.Entry<ID, Set<ID>> e : map.entrySet()) {
            this.map.put(e.getKey(), new TreeSet<>(e.getValue()));
        }
    }

    public void associate(Job job, Person person) {
        return;
    }

    public void disassociate(Job job, Person person) throws EmploymentNotFoundException {
        throw new AssertionError("This method should not be called.");
    }

    public void deletePerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    public void deleteJob(Job job) {
        throw new AssertionError("This method should not be called.");
    }

    public List<Job> getJobs(Person person, Model model) {
        requireAllNonNull(person, model);
        return model.getFilteredJobList();
    }

    public List<Person> getPersons(Job job, Model model) {
        requireAllNonNull(job, model);
        return model.getFilteredPersonList();
    }

    public Map<ID, Set<ID>> getAllJobs() {
        return map;
    }

    public static void setInstance(Employment employment) {
        throw new AssertionError("This method should not be called.");
    }

    public static void newInstance() {
        instance = new EmploymentStub();
    }

    public static EmploymentStub getInstance() {
        return EmploymentStub.getInstance();
    }
}
