package peoplesoft.ui.regions;

// import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
// import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import peoplesoft.model.job.Job;
import peoplesoft.ui.UiPart;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobListCard.fxml";

    public final Job job;

    @FXML
    private HBox cardPane;
    @FXML
    private Label idx;
    @FXML
    private Label desc;
    @FXML
    private Label rate; // do i really need the rate?
    @FXML
    private Label duration;
    @FXML
    private Label paidFor;
    // Todo: get the associated people from employment to display in here
    // @FXML
    // Todo: implement tags when oviya merges her part
    // private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public JobCard(Job job, int displayedIndex) {
        super(FXML);
        this.job = job;
        idx.setText(displayedIndex + "");
        desc.setText(job.getDesc());
        // rate.setText(job.getRate().value);
        // duration.setText(job.getDuration().value);
        paidFor.setText(job.hasPaid() ? "✅" : "❌");
        /*
        job.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
         */
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobCard)) {
            return false;
        }

        // state check
        JobCard card = (JobCard) other;
        return idx.getText().equals(card.idx.getText())
                && job.equals(card.job);
    }
}
