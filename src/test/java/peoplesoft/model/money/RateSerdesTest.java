package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static peoplesoft.testutil.TestUtil.serializeObject;
import static peoplesoft.testutil.TestUtil.toNormalizedJsonString;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import peoplesoft.commons.util.JsonUtil;

public class RateSerdesTest {
    private static final Rate VALID_RATE = new Rate(
            new Money(new BigDecimal("19.25")),
            Duration.ofMinutes(90));

    private static final String VALID_AMOUNT = "\"19.250000\"";
    private static final String VALID_DURATION = "\"PT1H30M\"";

    private static final String VALID_SERIALIZATION = serializeRate(VALID_AMOUNT, VALID_DURATION);

    private static String serializeRate(String amount, String duration) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("amount", amount);
        map.put("duration", duration);

        return serializeObject(map);
    }

    @Test
    public void serialize() throws JsonProcessingException {
        assertEquals(VALID_SERIALIZATION, toNormalizedJsonString(VALID_RATE));
    }

    /**
     * Tests Person.PersonDeserializer.getNullValue().
     */
    @Test
    public void deserialize_null_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("null", Rate.class));
    }

    @Test
    public void deserialize_nonRateValue_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("385", Rate.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"string\"", Rate.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("[\"im an array lol\"]", Rate.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(
                "{\"issa\":\"object!\"}", Rate.class));
    }

    @Test
    public void deserialize_invalidAmount_throwsJsonMappingException() throws IOException {
        final String serialized = serializeRate("\" 19.25\"", VALID_DURATION);

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(serialized, Rate.class));
    }

    @Test
    public void deserialize_nullAmount_throwsJsonMappingException() {
        final String serialized = serializeRate("null", VALID_DURATION);

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(serialized, Rate.class));
    }

    @Test
    public void deserialize_invalidDuration_throwsJsonMappingException() {
        final String serialized = serializeRate(VALID_AMOUNT, "\"five hours\"");

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(serialized, Rate.class));
    }

    @Test
    public void deserialize_nullDuration_throwsJsonMappingException() {
        final String serialized = serializeRate(VALID_AMOUNT, "null");

        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString(serialized, Rate.class));
    }

    @Test
    public void deserialize_validSerialization_returnsRate() throws IOException {
        assertEquals(VALID_RATE, JsonUtil.fromJsonString(VALID_SERIALIZATION, Rate.class));
    }
}
