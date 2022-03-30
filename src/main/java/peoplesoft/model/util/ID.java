package peoplesoft.model.util;

import static java.util.Objects.requireNonNull;
import static peoplesoft.commons.util.AppUtil.checkArgument;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;

/**
 * Represents an ID for some sort of entity, e.g. a {@code Person} or {@code Job}.
 * Guarantees: immutable; is valid as declared in {@link #isValidId(String)}
 */
@JsonSerialize(using = ID.IdSerializer.class)
@JsonDeserialize(using = ID.IdDeserializer.class)
public class ID implements Comparable<ID> {
    public static final String MESSAGE_CONSTRAINTS =
            "IDs should only begin and end with alphanumeric characters, "
            + "contain alphanumeric characters and hyphens, "
            + "and should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}]([\\p{Alnum}-]*[\\p{Alnum}])?";

    public final String value;

    /**
     * Constructs a {@code ID}.
     *
     * @param value A valid id.
     */
    public ID(String value) {
        requireNonNull(value);
        checkArgument(isValidId(value), MESSAGE_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Constructs a {@code ID}.
     *
     * @param value A valid id.
     */
    public ID(int value) {
        String strValue = String.valueOf(value);
        checkArgument(isValidId(strValue), MESSAGE_CONSTRAINTS);
        this.value = strValue;
    }

    /**
     * Returns true if a given string is a valid id.
     */
    public static boolean isValidId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ID // instanceof handles nulls
                && value.equals(((ID) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(ID o) {
        return value.compareTo(o.value);
    }

    protected static class IdSerializer extends StdSerializer<ID> {
        private IdSerializer(Class<ID> val) {
            super(val);
        }

        private IdSerializer() {
            this(null);
        }

        @Override
        public void serialize(ID val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(val.value);
        }
    }

    protected static class IdDeserializer extends StdDeserializer<ID> {
        private static final String MISSING_OR_INVALID_INSTANCE = "The id value is invalid or missing!";

        private IdDeserializer(Class<?> vc) {
            super(vc);
        }

        private IdDeserializer() {
            this(null);
        }

        @Override
        public ID deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException, JsonProcessingException {
            JsonNode node = p.readValueAsTree();

            if (!(node instanceof TextNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
            }

            String value = ((TextNode) node).textValue();
            if (!ID.isValidId(value)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, ID.MESSAGE_CONSTRAINTS);
            }

            return new ID(value);
        }

        @Override
        public ID getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_INSTANCE);
        }
    }
}
