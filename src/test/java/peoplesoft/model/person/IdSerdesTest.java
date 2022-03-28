package peoplesoft.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import peoplesoft.commons.util.JsonUtil;
import peoplesoft.model.util.ID;

public class IdSerdesTest {
    @Test
    public void serialize() throws JsonProcessingException {
        assertEquals("\"Test-id\"", JsonUtil.toJsonString(new ID("Test-id")));
    }

    /**
     * Tests ID.IDDeserializer.getNullValue().
     */
    @Test
    public void deserialize_null_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("null", ID.class));
    }

    @Test
    public void deserialize_nonStringValue_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("5", ID.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("[\"im an array lol\"]", ID.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("{\"issa\":\"object!\"}", ID.class));
    }

    /**
     * Tests deserialization invalid strings, i.e. those where the first character is not alphanumeric.
     */
    @Test
    public void deserialize_invalidID_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"\"", ID.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"-e10\"", ID.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"%\"", ID.class));
    }

    @Test
    public void deserialize() throws IOException {
        assertEquals(new ID("3"), JsonUtil.fromJsonString("\"3\"", ID.class));
        assertEquals(new ID("TestId-1234"), JsonUtil.fromJsonString("\"TestId-1234\"", ID.class));
    }
}
