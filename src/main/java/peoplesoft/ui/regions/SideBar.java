package peoplesoft.ui.regions;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import peoplesoft.ui.MainWindow;
import peoplesoft.ui.UiPart;

public class SideBar extends UiPart<Region> implements Initializable {
    private static final String FXML = "SideBar.fxml";

    private static final String ACTIVE_COLOR = "#6076FF";
    private static final String INACTIVE_COLOR = "transparent";

    private Button activePage;

    @FXML
    private Button bOverview;

    @FXML
    private Button bHelp;

    @FXML
    private Button bExit;

    private MainWindow mainWindow;

    /**
     * Creates a sidebar panel.
     *
     * @param mw the main window used to change the page.
     */
    public SideBar(MainWindow mw) {
        super(FXML);
        mainWindow = mw;
    }

    /**
     * Assigns parameters upon GUI initialisation.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activePage = bOverview;
    }

    @FXML
    private void switchToOverview(MouseEvent event) {
        mainWindow.loadOverviewPage();
        switchButtonColor(bOverview);
    }

    @FXML
    private void switchToHelp(MouseEvent event) {
        mainWindow.loadHelpPage();
        switchButtonColor(bHelp);
    }

    @FXML
    private void exitApp(MouseEvent event) {
        mainWindow.handleExit();
        switchButtonColor(bExit);
    }

    private void switchButtonColor(Button b) {
        activePage.setStyle("-fx-background-color: " + INACTIVE_COLOR);
        b.setStyle("-fx-background-color: " + ACTIVE_COLOR + "; -fx-background-radius: 10;");
        activePage = b;
    }
}
