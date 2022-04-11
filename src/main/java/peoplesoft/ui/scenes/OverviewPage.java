package peoplesoft.ui.scenes;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.Model;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.ui.regions.JobListPanel;
import peoplesoft.ui.regions.PersonListPanel;

public class OverviewPage extends Page {

    private static final Logger logger = LogsCenter.getLogger(HelpPage.class);
    private static final String FXML = "OverviewPage.fxml";
    private PersonListPanel personListPanel;
    private JobListPanel jobListPanel;

    @FXML
    private HBox personListHeader;
    @FXML
    private HBox jobListHeader;

    @FXML
    private StackPane personListPanelPlaceholder;
    @FXML
    private StackPane jobListPanelPlaceholder;

    /**
     * Creates a {@code OverPage} with the given {@code ObservableList<Person>}
    */
    public OverviewPage(ObservableList<Person> filteredPersonList,
            ObservableList<Job> filteredJobList, Model model) {
        super(FXML);
        logger.fine("Opening PeopleSoft's Overview page.");
        List<ReadOnlyDoubleProperty> personColWidths = personListHeader.getChildren().stream()
                .map(node -> ((Region) node).widthProperty())
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
        personListPanel = new PersonListPanel(filteredPersonList, personColWidths);

        List<ReadOnlyDoubleProperty> jobColWidths = jobListHeader.getChildren().stream()
                .map(node -> ((Region) node).widthProperty())
                .collect(Collectors.toList());
        jobListPanel = new JobListPanel(filteredJobList, filteredPersonList, model, jobColWidths);

        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        jobListPanelPlaceholder.getChildren().add(jobListPanel.getRoot());
    }
}
