package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import peoplesoft.commons.util.JsonUtil;

public class MoneySerdesTest {
    public static final String VERY_BIG_NUMBER_STRING =
            "10443888814131525066917527107166243825799642490473837803842334832839539079715574"
                    + "56848826811934997558340890106714439262837987573438185793607263236087851365277945"
                    + "95697654370999834036159013438371831442807001185594622637631883939771274567233468"
                    + "43445866174968079087058037040712840487401186091144679777835980290066869389768817"
                    + "87785946905630190260940599579453432823469303026696443059025015972399867714215541"
                    + "69383555988529148631823791443449673408781187263949647510018904134900841706167509"
                    + "36683338505510329720882695507699836163694119330152137968258371880918336567512213"
                    + "18492846368125550225998300412344784862595674492194617023806505913245610825731835"
                    + "38008760862210283427019769820231316901767800667519548507992163641937028537512478"
                    + "40149071591354599827905133996115517942711068311340905842728842797915548497829543"
                    + "23534517065223269061394905987693002122963395687782878948440616007412945674919823"
                    + "05057164237715481632138063104590291613692670834285644073044789997190178146576347"
                    + "32238502672530598997959960907994692017746248177184498674556592501783290704731194"
                    + "33165550807568221846571746373296884912819520317457002440926616910874148385078411"
                    + "92980452298185733897764810312608590300130241346718972667321649151113160292078173"
                    + "8033436090243804708340403154190336.123456";
    @Test
    public void serialize() throws JsonProcessingException {
        assertEquals("\"5194817623.542413\"", JsonUtil.toJsonString(new Money(new BigDecimal("5194817623.542413"))));
        assertEquals("\"-1.000000\"", JsonUtil.toJsonString(new Money(new BigDecimal("-1"))));
        assertEquals("\"0.000000\"", JsonUtil.toJsonString(new Money(new BigDecimal("0.00"))));
        assertEquals("\"0.000000\"", JsonUtil.toJsonString(new Money(new BigDecimal("0.00"))));
        assertEquals("\"" + VERY_BIG_NUMBER_STRING + "\"",
                JsonUtil.toJsonString(new Money(new BigDecimal(VERY_BIG_NUMBER_STRING))));
    }

    /**
     * Tests Money.MoneyDeserializer.getNullValue().
     */
    @Test
    public void deserialize_null_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("null", Money.class));
    }

    @Test
    public void deserialize_nonDecimalStringValue_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("500", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("123.456", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"hello world!\"", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("[\"im an array lol\"]", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("{\"issa\":\"object!\"}", Money.class));
    }

    /**
     * Tests deserialization invalid decimal strings.
     */
    @Test
    public void deserialize_invalidMoney_throwsJsonMappingException() {
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\" 123.456\"", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\"--5 \"", Money.class));
        assertThrows(JsonMappingException.class, () -> JsonUtil.fromJsonString("\",234\"", Money.class));
    }

    @Test
    public void deserialize() throws IOException {
        assertEquals(new Money(new BigDecimal("123456.789")),
                JsonUtil.fromJsonString("\"123456.789000000\"", Money.class));
        assertEquals(new Money(new BigDecimal("-123456.789")),
                JsonUtil.fromJsonString("\"-123456.789\"", Money.class));
        assertEquals(new Money(new BigDecimal("0")),
                JsonUtil.fromJsonString("\"000000\"", Money.class));
        assertEquals(new Money(new BigDecimal("0")),
                JsonUtil.fromJsonString("\"-0\"", Money.class));
        assertEquals(
                new Money(new BigDecimal(VERY_BIG_NUMBER_STRING)),
                JsonUtil.fromJsonString("\"" + VERY_BIG_NUMBER_STRING + "\"", Money.class));

        // things that are valid BigDecimal string reprs, but might be a little weird
        // TODO should these pass?
        assertEquals(new Money(new BigDecimal("100000")), JsonUtil.fromJsonString("\"1e5\"", Money.class));
        assertEquals(new Money(new BigDecimal("1234567.890123")),
                JsonUtil.fromJsonString("\"1.234567890123e6\"", Money.class));
    }
}
