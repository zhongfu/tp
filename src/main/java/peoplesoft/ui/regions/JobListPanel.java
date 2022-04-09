package peoplesoft.ui.regions;

import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.job.Job;
import peoplesoft.ui.UiPart;

/**
 * Panel containing the list of persons.
 */
public class JobListPanel extends UiPart<Region> {
    private static final String FXML = "JobListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobListPanel.class);

    private List<ReadOnlyDoubleProperty> colWidths;

    @FXML
    private ListView<Job> jobListView;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Job> jobList, List<ReadOnlyDoubleProperty> colWidths) {
        super(FXML);
        this.colWidths = colWidths;
        jobListView.setItems(jobList);
        jobListView.setCellFactory(listView -> new JobListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Job} using a {@code Job}.
     */
    class JobListViewCell extends ListCell<Job> {
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);

            if (empty || job == null) {
                setGraphic(null);
                setText(null);
            } else {
                // add a new divider before also!
                // <StackPane fx:id="divider" layoutX="10.0" layoutY="21.0"
                // prefHeight="2.0" style="-fx-background-color: #33344B;" />
                setGraphic(new JobCard(job, getIndex() + 1, colWidths).getRoot());
            }
        }
    }

}
