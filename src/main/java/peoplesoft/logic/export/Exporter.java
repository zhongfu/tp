package peoplesoft.logic.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.money.Money;
import peoplesoft.model.money.Payment;
import peoplesoft.model.person.Person;


public class Exporter {

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss");

    private final Person personToExport;

    private final Model model;

    private final File storageFile;

    private final String targetFileName;

    private Exporter(Person personToExport, Model model) {
        this.personToExport = personToExport;
        this.model = model;
        this.targetFileName = (personToExport.getName().toString() // assume only alphanumeric + spaces
                    .replace(" ", "-")) // just in case
                + "_" + dtFormatter.format(LocalDateTime.now()) + ".csv";
        this.storageFile = Path.of("data", this.targetFileName).toFile();
    }

    public static Exporter getNewInstance(Person personToExport, Model model) {
        return new Exporter(personToExport, model);
    }

    // https://www.baeldung.com/java-csv
    private static String escape(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private static String toCsvRow(Job job, Person person) {
        Payment pymt = person.getPayments().getOrDefault(
                job.getJobId(),
                Payment.createPayment(person, job, new Money(0)));
        BigDecimal rate = pymt.getAmount().divide(BigDecimal.valueOf(job.getDuration().toHours())).getValue();

        String status = job.hasPaid()
                ? pymt.isCompleted()
                        ? "Paid"
                        : "Pending payment"
                : "Incomplete";

        // jobid, jobdesc, status, rate, duration, payment
        return String.format("%s,%s,%s,$%s/h,%dh,%s",
            job.getJobId(),
            escape(job.getDesc()),
            status,
            rate.toPlainString(),
            job.getDuration().toHours(),
            pymt.getAmount().toString());
    }

    /**
     * Exports to a file with the name of the person
     * @throws IOException
     */
    public void export() throws IOException {
        List<Job> jobsAssignedToPerson = Employment.getInstance().getJobs(personToExport, model);

        String listHeader = "Job ID,Job Description,Status,Rate,Duration,Payment";

        String incomeItemized = jobsAssignedToPerson.stream()
                .map(job -> toCsvRow(job, personToExport))
                .collect(Collectors.joining("\n"));

        String exportableMessage = listHeader
                + "\n" + incomeItemized;

        FileWriter fileWriter = new FileWriter(storageFile);
        fileWriter.write(exportableMessage);
        fileWriter.close();
    }
}
