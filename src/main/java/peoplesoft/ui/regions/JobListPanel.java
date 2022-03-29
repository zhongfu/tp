package peoplesoft.ui.regions;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.person.Person;
import peoplesoft.ui.UiPart;

/**
 * Panel containing the list of persons.
 */
public class JobListPanel extends UiPart<Region> {
    private static final String FXML = "JobListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobListPanel.class);

    @FXML
    private ListView<Person> jobListView;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Person> jobList) {
        super(FXML);
        jobListView.setItems(jobList);
        jobListView.setCellFactory(listView -> new JobListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class JobListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                // add a new divider before also!
                // <StackPane fx:id="divider" layoutX="10.0" layoutY="21.0"
                // prefHeight="2.0" style="-fx-background-color: #33344B;" />
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
