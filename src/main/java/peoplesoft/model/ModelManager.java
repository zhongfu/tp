package peoplesoft.model;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import peoplesoft.commons.core.GuiSettings;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.job.Job;
import peoplesoft.model.job.exceptions.JobNotFoundException;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.exceptions.PersonNotFoundException;
import peoplesoft.model.util.ID;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Job> filteredJobs;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredJobs = new FilteredList<>(this.addressBook.getJobList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    //=========== Person Operations ==========================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPerson(ID personId) {
        requireNonNull(personId);
        return addressBook.hasPerson(personId);
    }

    @Override
    public Person getPerson(ID personId) throws PersonNotFoundException {
        requireNonNull(personId);
        return addressBook.getPerson(personId);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Job Operations =============================================================================

    @Override
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return hasPerson(job.getJobId());
    }

    @Override
    public boolean hasJob(ID jobId) {
        requireNonNull(jobId);
        return addressBook.hasJob(jobId);
    }

    @Override
    public Job getJob(ID jobId) throws JobNotFoundException {
        requireNonNull(jobId);
        return addressBook.getJob(jobId);
    }

    @Override
    public void deleteJob(Job target) {
        addressBook.removeJob(target);
    }

    @Override
    public void addJob(Job job) {
        addressBook.addJob(job);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
    }

    @Override
    public void setJob(Job target, Job editedJob) {
        requireAllNonNull(target, editedJob);
        addressBook.setJob(target, editedJob);
    }

    //=========== Filtered Job List Accessors ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return filteredJobs;
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.setPredicate(predicate);
    }

    // good practice to include this when overriding equals()
    @Override
    public int hashCode() {
        return Objects.hash(addressBook, userPrefs, filteredPersons, filteredJobs);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons)
                && filteredJobs.equals(other.filteredJobs);
    }
}
