package peoplesoft.commons.core;

/**
 * Class to generate unique {@code JobIds}.
 */
public class JobIdFactory {
    private static int id = 0;

    /**
     * Returns a unique {@code JobId}.
     *
     * @return JobId.
     */
    // TODO: currently missing functionality with serdes
    public static String nextId() {
        return String.valueOf(++id);
    }
}
