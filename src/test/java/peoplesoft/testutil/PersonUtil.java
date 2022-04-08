package peoplesoft.testutil;

import static peoplesoft.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_EMAIL;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_NAME;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_PHONE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_RATE;
import static peoplesoft.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import peoplesoft.logic.commands.person.PersonAddCommand;
import peoplesoft.logic.commands.person.PersonEditCommand.EditPersonDescriptor;
import peoplesoft.model.money.Payment;
import peoplesoft.model.person.Person;
import peoplesoft.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return PersonAddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        // TODO might need more elegance
        sb.append(PREFIX_RATE + person.getRate().getAmount().printFullValue() + " ");
        person.getTags().stream().forEach(
                s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getRate().ifPresent(rate -> sb.append(PREFIX_RATE).append(rate.getAmount().printFullValue())
                .append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }

    /**
     * Generate a rudimentary JSON serialization of a {@code Person}.
     *
     * @param person the {@code Person} to be serialized
     * @return a JSON serialization of the given {@code Person}
     */
    public static String serializePerson(Person person) {
        return serializePerson(
            person.getPersonId().toString(),
            person.getName().toString(),
            person.getPhone().toString(),
            person.getAddress().toString(),
            person.getEmail().toString(),
            person.getRate().getAmount().printFullValue(), // TODO
            person.getTags().stream().map((tag) -> tag.tagName).collect(Collectors.toSet()),
            person.getPayments().values().stream().map(PersonUtil::serializePayment).collect(Collectors.toSet()));
    }

    /**
     * Generates a rudimentary JSON serialization of a {@code Person} with the given details.
     *
     * @param name the string representation of the {@code Person}'s name
     * @param phone the string representation of the {@code Person}'s phone number
     * @param address the string representation of the {@code Person}'s address
     * @param email the string representation of the {@code Person}'s email address
     * @param rate the string representation of the {@code Person}'s rate
     * @param tags a {@code Set} of the tags assigned to the {@code Person}
     * @param payments a {@code Set} of serialized representations of each {@code Payment}
     * @return a JSON serialization of the given {@code Person}
     */
    public static String serializePerson(String personId, String name, String phone, String address, String email,
                String rate, Set<String> tags, Set<String> payments) {
        return serializePerson(
                personId,
                name,
                phone,
                address,
                email,
                rate,
                TestUtil.serializeList(
                        tags.stream()
                                .map((tag) -> tag == null ? "null" : "\"" + tag + "\"")
                                .collect(Collectors.toList())),
                TestUtil.serializeList(
                        payments.stream()
                                .map(String::valueOf) // assuming we already have strnig reprs
                                .collect(Collectors.toList())));
    }

    /**
     * Generates a rudimentary JSON serialization of a {@code Person} with the given details.
     *
     * @param name the string representation of the {@code Person}'s name
     * @param phone the string representation of the {@code Person}'s phone number
     * @param address the string representation of the {@code Person}'s address
     * @param email the string representation of the {@code Person}'s email address
     * @param rate the string representation of the {@code Person}'s rate
     * @param tags the string representation of the tags assigned to the {@code Person}
     * @return a JSON serialization of the given {@code Person}
     */
    public static String serializePerson(String personId, String name, String phone, String address, String email,
                String rate, String tags, String payments) {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("id", personId == null ? "null" : "\"" + personId + "\"");
        map.put("name", name == null ? "null" : "\"" + name + "\"");
        map.put("phone", phone == null ? "null" : "\"" + phone + "\"");
        map.put("email", email == null ? "null" : "\"" + email + "\"");
        map.put("address", address == null ? "null" : "\"" + address + "\"");
        map.put("rate", rate == null ? "null" : "{\n  \"amount\" : \"" + rate + "\",\n  \"duration\""
                + " : \"PT1H\"\n}");
        map.put("tagged", tags);
        map.put("payments", payments);

        return TestUtil.serializeObject(map);
    }

    /**
     * Serializes a {@code Payment} object into a rudimentary JSON representation.
     */
    public static String serializePayment(Payment pymt) {
        return serializePayment(
                pymt.isCompleted() ? "COMPLETED" : "PENDING",
                pymt.getPersonId().toString(),
                pymt.getJobId().toString(),
                pymt.getAmount().toString());
    }

    /**
     * Creates a JSON representation of a {@code Payment} object using the given fields.
     */
    public static String serializePayment(String state, String personId, String jobId, String amount) {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("state", state == null ? "null" : "\"" + state + "\"");
        map.put("personId", state == null ? "null" : "\"" + personId + "\"");
        map.put("jobId", state == null ? "null" : "\"" + jobId + "\"");
        map.put("amount", state == null ? "null" : "\"" + amount + "\"");

        return TestUtil.serializeObject(map);
    }
}
