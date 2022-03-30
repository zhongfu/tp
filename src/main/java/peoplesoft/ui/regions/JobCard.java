package peoplesoft.ui.regions;

// import java.util.Comparator;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
// import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import peoplesoft.model.job.Job;
import peoplesoft.ui.UiPart;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobListCard.fxml";
    private final Image TICK = new Image(Objects.requireNonNull(this.getClass()
            .getResourceAsStream("/images/apple-tick-emoji.png")));
    private final Image CROSS = new Image(Objects.requireNonNull(this.getClass()
            .getResourceAsStream("/images/apple-cross-emoji.png")));

    public final Job job;

    // what is shown is for proof of concept for now until jobs and payments are finalised
    @FXML
    private HBox cardPane;
    @FXML
    private Label idx; // job ID
    @FXML
    private Label desc; // string
    @FXML
    private Label rate; // $1.00 / 1H
    @FXML
    private Label duration; // 1H0M
    @FXML
    private ImageView paidForIcon; // false
    // @FXML
    // private FlowPane involved; // Todo: get the associated people from employment to display in here
    // @FXML
    // private FlowPane tags; // Todo: implement tags when oviya merges her part

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public JobCard(Job job, int displayedIndex) {
        super(FXML);
        this.job = job;
        idx.setText(displayedIndex + "");
        desc.setText(job.getDesc());
        rate.setText(job.getRate().toString());
        duration.setText(job.getDuration().toString());
        paidForIcon.setImage(job.hasPaid() ? TICK : CROSS);
        /*
        for when jobtags are a thing
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
