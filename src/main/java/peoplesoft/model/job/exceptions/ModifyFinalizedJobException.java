package peoplesoft.model.job.exceptions;

/**
 * Signifies that a finalized Job was attempted to be modified.
 */
public class ModifyFinalizedJobException extends RuntimeException {
    public ModifyFinalizedJobException() {
        super("Cannot modify a job that has finalized payment.");
    }
}
