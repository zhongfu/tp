package peoplesoft.ui.regions;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import peoplesoft.ui.PageSwitcher;
import peoplesoft.ui.UiPart;

public class SideBar extends UiPart<Region> implements Initializable {
    private static final String FXML = "SideBar.fxml";

    private static final String ACTIVE_COLOR = "#6368ff";
    private static final String INACTIVE_COLOR = "transparent";

    private Button activePage;

    @FXML
    private Button bOverview;

    @FXML
    private Button bHelp;

    @FXML
    private Button bExit;

    private PageSwitcher pageSwitcher;

    /**
     * Creates a sidebar panel.
     */
    public SideBar() {
        super(FXML);
    }

    /**
     * Binds the page switcher to execute the page change in MainWindow.
     * Exists because MainWindow cannot simultaneously create
     * this SideBar and a PageSwitcher.
     *
     * @param ps the PageSwitcher object.
     */
    public void setPageSwitcher(PageSwitcher ps) {
        pageSwitcher = ps;
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

    // --- mouse input detection ---

    @FXML
    private void switchToOverview(MouseEvent event) {
        pageSwitcher.switchOnCommand(PageSwitcher.PageValues.OVERVIEW);
    }

    @FXML
    private void switchToHelp(MouseEvent event) {
        pageSwitcher.switchOnCommand(PageSwitcher.PageValues.HELP);
    }

    @FXML
    private void exitApp(MouseEvent event) {
        pageSwitcher.switchOnCommand(PageSwitcher.PageValues.EXIT);
    }

    // --- update view ---

    /**
     * Makes the overview button light up and deactivates the current button.
     */
    public void activateOverviewButton() {
        switchButtonColor(bOverview);
    }

    /**
     * Makes the help button light up and deactivates the current button.
     */
    public void activateHelpButton() {
        switchButtonColor(bHelp);
    }

    /**
     * Makes the exit button light up and deactivates the current button.
     */
    public void activateExitButton() {
        switchButtonColor(bExit);
    }

    private void switchButtonColor(Button b) {
        activePage.setStyle("-fx-background-color: " + INACTIVE_COLOR);
        b.setStyle("-fx-background-color: " + ACTIVE_COLOR + "; -fx-background-radius: 10;");
        activePage = b;
    }
}
