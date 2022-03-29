package peoplesoft.ui.scenes;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.person.Person;
import peoplesoft.ui.regions.PersonListPanel;

public class OverviewPage extends Page {

    private static final Logger logger = LogsCenter.getLogger(HelpPage.class);
    private static final String FXML = "OverviewPage.fxml";
    private PersonListPanel personListPanel;

    @FXML
    private StackPane personListPanelPlaceholder;


    /**
     * Creates a {@code OverPage} with the given {@code ObservableList<Person>}
    */
    public OverviewPage(ObservableList<Person> filteredPersonList) {
        super(FXML);
        logger.fine("Opening PeopleSoft's Overview page.");
        personListPanel = new PersonListPanel(filteredPersonList);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
    }

}
