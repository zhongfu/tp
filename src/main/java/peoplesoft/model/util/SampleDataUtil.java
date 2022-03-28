package peoplesoft.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import peoplesoft.model.AddressBook;
import peoplesoft.model.ReadOnlyAddressBook;
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
            new Person(new ID(1), new Name("Nicole Tan"), new Phone("99338558"), new Email("nicole@stffhub.org"),
                new Address("1 Tech Drive, S138572"),
                getTagSet("Intern", "Aircon")),
            new Person(new ID(2), new Name("Kavya Singh"), new Phone("96736637"), new Email("kavya@stffhub.org"),
                new Address("2 Orchard Turn, S238801"),
                getTagSet("Senior", "Electrician")),
            new Person(new ID(3), new Name("Ethan Lee"), new Phone("91031282"), new Email("ethan@stffhub.org"),
                new Address("10 Anson Road, S079903"),
                getTagSet("Appliances")),
            new Person(new ID(4), new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@stffhub.org"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("Painting")),
            new Person(new ID(5), new Name("Arjun Khatau"), new Phone("80445044"), new Email("arjun@stffhub.org"),
                new Address("50 Collyer Quay, S049321"),
                getTagSet("Contract", "Aircon"))
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
