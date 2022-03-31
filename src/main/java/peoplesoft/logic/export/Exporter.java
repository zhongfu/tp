package peoplesoft.logic.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;


public class Exporter {

    private final Person personToExport;

    private final Model model;

    private final File storageFile;

    private final String targetFileName;

    private Exporter(Person personToExport, Model model) {
        this.personToExport = personToExport;
        this.model = model;
        this.targetFileName = personToExport.getName().fullName + ".csv";
        this.storageFile = Path.of("data", this.targetFileName).toFile();
    }

    public static Exporter getNewInstance(Person personToExport, Model model) {
        return new Exporter(personToExport, model);
    }

    /**
     * Exports to a file with the name of the person
     * @throws IOException
     */
    public void export() throws IOException {
        List<Job> jobsAssignedToPerson = Employment.getInstance().getJobs(personToExport, model);

        List<Job> assignedJobsCompleted = jobsAssignedToPerson.stream().filter(
                job -> job.hasPaid()).collect(Collectors.toList());

        List<Job> assignedJobsIncomplete = jobsAssignedToPerson.stream().filter(
                job -> !job.hasPaid()).collect(Collectors.toList());

        String personalDetails = String.format("Name,%s,\nPhone, %s,\nEmail,%s,\nAddress,%s",
                personToExport.getName(),
                personToExport.getPhone(),
                personToExport.getEmail(),
                personToExport.getAddress());

        String listHeader = "Job ID, Job Description, Rate, Duration, Payment";

        String earnedIncomeItemized = assignedJobsCompleted.stream().map(
                job -> String.format("%s,%s,%s,",
                    job.getJobId(),
                    job.getDesc(),
                    job.getDuration())
        ).collect(Collectors.joining("\n"));

        String unearnedIncomeItemized = assignedJobsIncomplete.stream().map(
                job -> String.format("%s,%s,%s,",
                    job.getJobId(),
                    job.getDesc(),
                    job.getDuration())
        ).collect(Collectors.joining("\n"));

        String exportableMessage = personalDetails
                + "\n" + "\n\nEarned Income"
                + "\n" + listHeader
                + "\n" + earnedIncomeItemized
                + "\n" + "\nOutstanding Income"
                + "\n" + unearnedIncomeItemized;

        FileWriter fileWriter = new FileWriter(storageFile);
        fileWriter.write(exportableMessage);
        fileWriter.close();
    }
}
