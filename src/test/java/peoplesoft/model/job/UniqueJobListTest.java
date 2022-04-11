package peoplesoft.model.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static peoplesoft.testutil.Assert.assertThrows;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import peoplesoft.model.job.exceptions.DuplicateJobException;
import peoplesoft.model.job.exceptions.JobNotFoundException;
import peoplesoft.model.util.ID;
import peoplesoft.testutil.JobBuilder;

public class UniqueJobListTest {
    private static final Job EATING = new Job(new ID(1043), "Eating", Duration.ofDays(1));
    private static final Job RUNNING = new Job(new ID(3175), "Running", Duration.ofHours(8));

    private final UniqueJobList uniqueJobList = new UniqueJobList();

    @Test
    public void contains_nullJobId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.contains(null));
    }

    @Test
    public void contains_jobNotInList_returnsFalse() {
        assertFalse(uniqueJobList.contains(EATING.getJobId()));
    }

    @Test
    public void contains_jobInList_returnsTrue() {
        uniqueJobList.add(EATING);
        assertTrue(uniqueJobList.contains(EATING.getJobId()));
    }

    @Test
    public void add_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.add(null));
    }

    @Test
    public void add_duplicateJob_throwsDuplicateJobException() {
        Job jobCopy = new JobBuilder(EATING).build();
        uniqueJobList.add(EATING);

        assertThrows(DuplicateJobException.class, () -> uniqueJobList.add(jobCopy));
    }

    @Test
    public void setJob_nullTargetJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJob(null, EATING));
    }

    @Test
    public void setJob_nullEditedJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJob(EATING, null));
    }

    @Test
    public void setJob_targetJobNotInList_throwsJobNotFoundException() {
        assertThrows(JobNotFoundException.class, () -> uniqueJobList.setJob(EATING, EATING));
    }

    @Test
    public void setJob_editedJobIsSameJob_success() {
        uniqueJobList.add(EATING);
        uniqueJobList.setJob(EATING, EATING);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(EATING);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJob_editedJobHasSameIdentity_success() {
        uniqueJobList.add(EATING);
        Job jobCopy = new JobBuilder(EATING).build();
        uniqueJobList.setJob(EATING, jobCopy);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(jobCopy);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJob_editedJobHasDifferentIdentity_success() {
        uniqueJobList.add(EATING);
        uniqueJobList.setJob(EATING, RUNNING);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(RUNNING);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJob_editedJobHasNonUniqueIdentity_throwsDuplicateJobException() {
        uniqueJobList.add(EATING);
        uniqueJobList.add(RUNNING);
        assertThrows(DuplicateJobException.class, () -> uniqueJobList.setJob(EATING, RUNNING));
    }

    @Test
    public void remove_nullJob_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.remove(null));
    }

    @Test
    public void remove_jobDoesNotExist_throwsJobNotFoundException() {
        assertThrows(JobNotFoundException.class, () -> uniqueJobList.remove(EATING));
    }

    @Test
    public void remove_existingJob_removesJob() {
        uniqueJobList.add(EATING);
        uniqueJobList.remove(EATING);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_nullUniqueJobList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJobs((UniqueJobList) null));
    }

    @Test
    public void setJobs_uniqueJobList_replacesOwnListWithProvidedUniqueJobList() {
        uniqueJobList.add(EATING);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(RUNNING);
        uniqueJobList.setJobs(expectedUniqueJobList);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueJobList.setJobs((List<Job>) null));
    }

    @Test
    public void setJobs_list_replacesOwnListWithProvidedList() {
        uniqueJobList.add(EATING);
        List<Job> jobList = Collections.singletonList(RUNNING);
        uniqueJobList.setJobs(jobList);
        UniqueJobList expectedUniqueJobList = new UniqueJobList();
        expectedUniqueJobList.add(RUNNING);
        assertEquals(expectedUniqueJobList, uniqueJobList);
    }

    @Test
    public void setJobs_listWithDuplicateJobs_throwsDuplicateJobException() {
        List<Job> listWithDuplicateJobs = Arrays.asList(EATING, EATING);
        assertThrows(DuplicateJobException.class, () -> uniqueJobList.setJobs(listWithDuplicateJobs));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueJobList.asUnmodifiableObservableList().remove(0));
    }
}
