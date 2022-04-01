package peoplesoft.commons.core;

/**
 * Container for messages displayed to the user through the ResultDisplay.
 */
public class Messages {

    // TODO: prefix all with MSG_ instead to make it shorter. and CMD for command, IDX for index
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_JOB_DISPLAYED_INDEX = "Invalid index for job";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "Invalid index for person";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_JOBS_LISTED_OVERVIEW = "%1$d jobs listed!";
    public static final String MESSAGE_MODIFY_FINAL_JOB = "Cannot modify a job that has finalized payment.";
    public static final String MESSAGE_ASSIGN_PERSON_TO_JOB = "Assign at least one person to this job.";
    // should add all the command explainers to here also

}
