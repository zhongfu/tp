package peoplesoft.logic.commands;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import peoplesoft.commons.core.GuiSettings;
import peoplesoft.model.Model;
import peoplesoft.model.ReadOnlyAddressBook;
import peoplesoft.model.ReadOnlyUserPrefs;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.model.util.ID;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(ID personId) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Person getPerson(ID personId) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        // let this method be called because job assign needs to update the personlist.
    }

    @Override
    public boolean hasJob(ID jobId) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasJob(Job job) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Job getJob(ID jobId) {
        throw new AssertionError("This method should not be called.");
    }
    @Override
    public void deleteJob(Job target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addJob(Job job) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setJob(Job target, Job editedJob) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Job> getFilteredJobList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        // let this method be called because job add needs to update the joblist.
    }
}
