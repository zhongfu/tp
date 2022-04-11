package peoplesoft.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JobIdFactoryTest {
    private static final int VALID_ID = 5;
    private static final int NEXT_VALID_ID = 6;
    private static final int INVALID_ID = -1;

    @Test
    public void setId() {
        JobIdFactory.setId(VALID_ID);
        assertEquals(JobIdFactory.getId(), VALID_ID);

        JobIdFactory.setId(NEXT_VALID_ID);
        assertEquals(JobIdFactory.getId(), NEXT_VALID_ID);
    }

    @Test
    public void setId_negative_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> JobIdFactory.setId(INVALID_ID));
    }

    @Test
    public void nextId() {
        JobIdFactory.setId(VALID_ID);
        String nextId = JobIdFactory.nextId().value;
        JobIdFactory.setId(NEXT_VALID_ID);

        assertEquals(String.valueOf(JobIdFactory.getId()), nextId);
    }
}
