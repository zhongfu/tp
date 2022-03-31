package peoplesoft.testutil;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Payment;
import peoplesoft.model.money.Rate;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;
import peoplesoft.model.util.ID;
import peoplesoft.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final double DEFAULT_RATE = 1.50;

    private ID personId;
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Rate rate;
    private Set<Tag> tags;
    private Map<ID, Payment> payments;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        personId = PersonIdFactory.nextId();
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        rate = new Rate(new Money(DEFAULT_RATE), Duration.ofHours(1));
        tags = new HashSet<>();
        payments = new HashMap<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        personId = personToCopy.getPersonId();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        rate = personToCopy.getRate();
        tags = new HashSet<>(personToCopy.getTags());
        payments = new HashMap<>(personToCopy.getPayments());
    }

    /**
     * Sets the {@code personId} of the {@code Person} that we are building.
     */
    public PersonBuilder withId(ID personId) {
        requireNonNull(personId);
        this.personId = personId;
        return this;
    }

    /**
     * Sets the {@code personId} of the {@code Person} that we are building to the current PersonIdFactory id.
     */
    public PersonBuilder withCurrentId() {
        return withId(new ID(PersonIdFactory.getId()));
    }

    /**
     * Sets the {@code personId} of the {@code Person} that we are building to the current PersonIdFactory id.
     */
    public PersonBuilder withNextId() {
        return withId(new ID(PersonIdFactory.getId() + 1));
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Rate} of the {@code Person} that we are building.
     */
    public PersonBuilder withRate(double rate) {
        this.rate = new Rate(new Money(rate), Duration.ofHours(1));
        return this;
    }

    /**
     * Sets the {@code payments} field of the {@code Person} that we are building, using an {@code Iterable}
     * of {@code Payment}s
     */
    public PersonBuilder withPayments(Iterable<Payment> payments) {
        this.payments = new HashMap<>();
        for (Payment pymt : payments) {
            requireNonNull(pymt);
            this.payments.put(pymt.getJobId(), pymt);
        }
        return this;
    }

    public Person build() {
        return new Person(personId, name, phone, email, address, rate, tags, payments);
    }

}
