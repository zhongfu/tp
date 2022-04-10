package peoplesoft.commons.core;

/**
 * Container for messages displayed to the user through the ResultDisplay.
 */
public class Messages {

    // -- General Commands --
    public static final String MSG_UNKNOWN_CMD = "Unknown command.\n"
            + "Type \"help\" to see a list of available commands.";
    public static final String MSG_INVALID_CMD_FORMAT = "Invalid command format. \n%1$s";
    public static final String MSG_EMPTY_STRING = "A field has been left empty.\nEnter the missing information.";
    public static final String MSG_DURATION_CONSTRAINTS = "The duration should be a positive number (in hours).";
    public static final String MSG_DURATION_TOO_LARGE = "The input duration is too long.\n"
            + "Consider adding the job as two jobs and splitting the duration between them.";

    // -- Person Commands --
    public static final String MSG_INVALID_PERSON_DISPLAYED_IDX = "Invalid index for person";
    public static final String MSG_PERSONS_LISTED_OVERVIEW = "%1$d persons listed.";

    // -- Job Commands --
    public static final String MSG_INVALID_JOB_DISPLAYED_IDX = "The specified job number "
            + "is not in the displayed list. \nConsider using the \"list\" command to list all jobs.";
    public static final String MSG_JOBS_LISTED_OVERVIEW = "%1$d jobs listed.";
    public static final String MSG_MODIFY_FINAL_JOB = "Cannot modify a job that has been paid.";
    public static final String MSG_ASSIGN_PERSON_TO_JOB = "Assign at least one person to this job.";
    public static final String MSG_DUPLICATE_JOB = "This job already exists in the database";
    public static final String MSG_DUPLICATE_EMPLOYMENT =
            "%s person(s) have already been assigned to this job.\n"
            + "Assignments cannot be edited. Please delete this job and create a new one.";
    public static final String MSG_ASSIGN_MARKED_JOB = "Unmark the job before assigning it.";
    public static final String MSG_JOB_NOT_PAID_FAILURE =
            "Payments cannot be finalized if the job is not marked as paid yet.\n"
            + "Please refer to the user guide for the expected order of actions.";

}
