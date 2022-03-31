package peoplesoft.model.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MoneyTest {

    private static final Money ZERO = new Money(0);
    private static final Money ONE = new Money(1);
    private static final Money TWO = new Money(2);

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NumberFormatException.class, () -> new Money(Double.NaN));
        assertThrows(NumberFormatException.class, () -> new Money(Double.POSITIVE_INFINITY));
        assertThrows(NullPointerException.class, () -> new Money(null));
    }

    @Test
    public void add() {
        // wrong addition -> returns false
        assertFalse(ZERO.add(ZERO).getValue().compareTo(BigDecimal.ONE) == 0);
        assertFalse(ONE.add(ONE).getValue().compareTo(BigDecimal.ONE) == 0);

        // correct addition -> returns true
        assertTrue(ONE.add(ZERO).getValue().compareTo(BigDecimal.ONE) == 0);
        assertTrue(ONE.add(ONE).getValue().compareTo(BigDecimal.valueOf(2)) == 0);
    }

    @Test
    public void subtract() {
        // wrong subtraction -> returns false
        assertFalse(ZERO.subtract(ZERO).getValue().compareTo(BigDecimal.ONE) == 0);
        assertFalse(ONE.subtract(ONE).getValue().compareTo(BigDecimal.valueOf(2)) == 0);

        // correct subtraction -> returns true
        assertTrue(ONE.subtract(ZERO).getValue().compareTo(BigDecimal.ONE) == 0);
        assertTrue(ONE.subtract(ONE).getValue().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void multiply() {
        // wrong multiplication -> returns false
        assertFalse(ZERO.multiply(ZERO).getValue().compareTo(BigDecimal.ONE) == 0);
        assertFalse(ONE.multiply(ONE).getValue().compareTo(BigDecimal.ZERO) == 0);

        // correct multiplication -> returns true
        assertTrue(ONE.multiply(ZERO).getValue().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(ONE.multiply(ONE).getValue().compareTo(BigDecimal.ONE) == 0);
    }

    @Test
    public void divide() {
        // division by zero -> throws error
        assertThrows(ArithmeticException.class, () -> ONE.divide(ZERO));
        assertThrows(ArithmeticException.class, () -> ONE.divide(ZERO));

        // wrong division -> returns false
        assertFalse(ZERO.divide(BigDecimal.valueOf(1)).getValue().compareTo(BigDecimal.ONE) == 0);
        assertFalse(ONE.divide(ONE).getValue().compareTo(BigDecimal.ZERO) == 0);

        // correct division -> returns true
        assertTrue(ZERO.divide(BigDecimal.valueOf(1)).getValue().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(TWO.divide(TWO).getValue().compareTo(BigDecimal.ONE) == 0);
    }

    @Test
    public void printFullValue() {
        assertEquals(ZERO.printFullValue(), "0.000000");
        assertEquals(ONE.printFullValue(), "1.000000");
        assertEquals(new Money(12.38).printFullValue(), "12.380000");
        assertEquals(new Money(0.2736541).printFullValue(), "0.273654");
        assertEquals(new Money(93.7265328).printFullValue(), "93.726533");
    }

    @Test
    public void testEquals() {
        // same value -> returns true
        Money otherOne = new Money(1);
        Money yetAnotherOne = new Money(BigDecimal.ONE);
        Money theLastOne = new Money(BigDecimal.valueOf(1000, 3));
        assertTrue(ONE.equals(otherOne));
        assertTrue(ONE.equals(yetAnotherOne));
        assertTrue(ONE.equals(theLastOne));

        // same object -> returns true
        assertTrue(ONE.equals(ONE));

        // null -> returns false
        assertFalse(ONE.equals(null));

        // another type -> returns false
        assertFalse(ONE.equals(1));

        // another value -> returns false
        assertFalse(ONE.equals(TWO));
    }

    @Test
    public void testToString() {
        assertEquals(ZERO.toString(), "$0.00");
        assertEquals(ONE.toString(), "$1.00");
        assertEquals(new Money(32.50).toString(), "$32.50");

        // rounding
        assertEquals(new Money(7.472).toString(), "$7.47");
        assertEquals(new Money(19.1294).toString(), "$19.13");
    }

}
