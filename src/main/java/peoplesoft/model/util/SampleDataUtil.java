package peoplesoft.model.util;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import peoplesoft.commons.core.PersonIdFactory;
import peoplesoft.model.AddressBook;
import peoplesoft.model.ReadOnlyAddressBook;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Rate;
import peoplesoft.model.person.Address;
import peoplesoft.model.person.Email;
import peoplesoft.model.person.Name;
import peoplesoft.model.person.Person;
import peoplesoft.model.person.Phone;
import peoplesoft.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(PersonIdFactory.nextId(), new Name("Nicole Tan"), new Phone("99338558"),
                new Email("nicole@stffhub.org"), new Address("1 Tech Drive, S138572"),
                new Rate(new Money(30), Duration.ofHours(1)), getTagSet("Intern", "Aircon"),
                Map.of()),
            new Person(PersonIdFactory.nextId(), new Name("Kavya Singh"), new Phone("96736637"),
                new Email("kavya@stffhub.org"), new Address("2 Orchard Turn, S238801"),
                new Rate(new Money(40), Duration.ofHours(1)), getTagSet("Senior", "Electrician"),
                Map.of()),
            new Person(PersonIdFactory.nextId(), new Name("Ethan Lee"), new Phone("91031282"),
                new Email("ethan@stffhub.org"), new Address("10 Anson Road, S079903"),
                new Rate(new Money(20), Duration.ofHours(1)), getTagSet("Appliances"),
                Map.of()),
            new Person(PersonIdFactory.nextId(), new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Email("irfan@stffhub.org"), new Address("Blk 47 Tampines Street 20, #17-35"),
                new Rate(new Money(48), Duration.ofHours(1)), getTagSet("Painting"),
                Map.of()),
            new Person(PersonIdFactory.nextId(), new Name("Arjun Khatau"), new Phone("80445044"),
                new Email("arjun@stffhub.org"), new Address("50 Collyer Quay, S049321"),
                new Rate(new Money(33), Duration.ofHours(1)), getTagSet("Contract", "Aircon"),
                Map.of())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
