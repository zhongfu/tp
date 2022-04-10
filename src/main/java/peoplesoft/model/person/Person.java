package peoplesoft.model.person;

import static peoplesoft.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Payment;
import peoplesoft.model.money.Rate;
import peoplesoft.model.tag.Tag;
import peoplesoft.model.util.ID;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
@JsonSerialize(using = Person.PersonSerializer.class)
@JsonDeserialize(using = Person.PersonDeserializer.class)
public class Person {
    private final ID id;

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Rate rate;
    private final Set<Tag> tags = new HashSet<>();
    private final Map<ID, Payment> payments = new HashMap<>();

    /**
     * Every field must be present and not null.
     */
    public Person(ID id, Name name, Phone phone, Email email, Address address, Rate rate, Set<Tag> tags,
            Map<ID, Payment> payments) {
        requireAllNonNull(id, name, phone, email, address, rate, tags);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rate = rate;
        this.tags.addAll(tags);
        this.payments.putAll(payments);
    }

    public ID getPersonId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Rate getRate() {
        return rate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Map<ID, Payment> getPayments() {
        return Collections.unmodifiableMap(payments);
    }

    /**
     * Calculates the Money of all the {@code PendingPayment}s on this {@code Person} at a point in time
     *
     * @return Amount due.
     */
    public Money getAmountDue() {
        Money sum = new Money(0);
        for (Payment p : payments.values()) {
            if (!p.isCompleted()) {
                sum = sum.add(p.getAmount());
            }
        }
        return sum;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
            && getPersonId().equals(otherPerson.getPersonId());
    }

    /**
     * Returns true if both persons have the data fields.
     * This defines a weak notion of equality between two persons.
     */
    public boolean isDuplicate(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getRate().equals(getRate())
                && otherPerson.getTags().equals(getTags())
                && otherPerson.getPayments().equals(getPayments());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return isSamePerson(otherPerson)
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getRate().equals(getRate())
                && otherPerson.getTags().equals(getTags())
                && otherPerson.getPayments().equals(getPayments());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, phone, email, address, rate, tags, payments);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ID: ")
                .append(getPersonId())
                .append("; Name: ")
                .append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress())
                .append("; Base Pay Rate: ")
                .append(getRate());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }

        Map<ID, Payment> payments = getPayments();
        if (!payments.isEmpty()) {
            builder.append("; Payments: [");
            builder.append(payments.values().stream().map(String::valueOf).collect(Collectors.joining(", ")));
            builder.append("]");
        }
        return builder.toString();
    }

    protected static class PersonSerializer extends StdSerializer<Person> {
        private PersonSerializer(Class<Person> val) {
            super(val);
        }

        private PersonSerializer() {
            this(null);
        }

        @Override
        public void serialize(Person val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeObjectField("id", val.getPersonId());
            gen.writeObjectField("name", val.getName());
            gen.writeObjectField("phone", val.getPhone());
            gen.writeObjectField("email", val.getEmail());
            gen.writeObjectField("address", val.getAddress());
            gen.writeObjectField("rate", val.getRate());
            gen.writeObjectField("tagged", val.getTags());

            gen.writeArrayFieldStart("payments");
            for (Payment pymt : val.getPayments().values()) {
                gen.writeObject(pymt);
            }
            gen.writeEndArray();

            gen.writeEndObject();
        }
    }

    protected static class PersonDeserializer extends StdDeserializer<Person> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The person instance is invalid or missing!";
        private static final UnaryOperator<String> INVALID_VAL_FMTR =
                k -> String.format("This person's %s value is invalid!", k);

        private PersonDeserializer(Class<?> vc) {
            super(vc);
        }

        private PersonDeserializer() {
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
        public Person deserialize(JsonParser p, DeserializationContext ctx)
                    throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            ObjectNode person = (ObjectNode) node;

            ID id = getNonNullNode(person, "id", ctx)
                    .traverse(codec)
                    .readValueAs(ID.class);

            Name name = getNonNullNode(person, "name", ctx)
                    .traverse(codec)
                    .readValueAs(Name.class);

            Phone phone = getNonNullNode(person, "phone", ctx)
                    .traverse(codec)
                    .readValueAs(Phone.class);

            Email email = getNonNullNode(person, "email", ctx)
                    .traverse(codec)
                    .readValueAs(Email.class);

            Address address = getNonNullNode(person, "address", ctx)
                    .traverse(codec)
                    .readValueAs(Address.class);

            Rate rate = getNonNullNode(person, "rate", ctx)
                    .traverse(codec)
                    .readValueAs(Rate.class);

            Set<Tag> tags = getNonNullNodeWithType(person, "tagged", ctx, ArrayNode.class)
                    .traverse(codec)
                    .readValueAs(new TypeReference<Set<Tag>>(){});

            // we deserialize the Payment objects one by one
            // because ctx.readValue() doesn't take TypeReferences
            // and we need to use the ctx to pass the current person ID down
            ctx.setAttribute("personId", id);
            Map<ID, Payment> payments = new HashMap<>();
            ArrayNode paymentsNode = getNonNullNodeWithType(person, "payments", ctx, ArrayNode.class);
            for (JsonNode paymentNode : paymentsNode) {
                Payment pymt = ctx.readValue(paymentNode.traverse(codec), Payment.class);
                if (payments.put(pymt.getJobId(), pymt) != null) { // check if jobId already exists in the map
                    throw JsonUtil.getWrappedIllegalValueException(ctx, INVALID_VAL_FMTR.apply("payments"));
                }
            }

            return new Person(id, name, phone, email, address, rate, tags, payments);
        }

        @Override
        public Person getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
