package peoplesoft.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class SideBar extends UiPart<Region> implements Initializable {
    private static final int FONT_SIZE_SIDEBAR = 16;
    private static final String ACTIVE_COLOR = "#6076FF";
    private static final String FXML = "SideBar.fxml";
    private static final String INACTIVE_COLOR = "transparent";

    private Button activePage;

    @FXML
    private Button bEmployees;

    @FXML
    private Button bExit;

    @FXML
    private Button bHelp;

    @FXML
    private Button bJobs;

    @FXML
    private Button bOverview;

    @FXML
    private Button bSettings;

    private MainWindow mainWindow;

    @FXML
    private final Font interMedium = Font.loadFont(this.getClass()
            .getResourceAsStream("/fonts/Inter-Medium.otf"), FONT_SIZE_SIDEBAR);

    @FXML
    private final Font interRegular = Font.loadFont(this.getClass()
            .getResourceAsStream("/fonts/Inter-Regular.otf"), FONT_SIZE_SIDEBAR);

    @FXML
    private final Font interBold = Font.loadFont(this.getClass()
            .getResourceAsStream("/fonts/Inter-Bold.otf"), FONT_SIZE_SIDEBAR);

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
     * The types of pages that can be called.
     */
    public static enum ActivePage {
        OVERVIEW, JOBS, EMPLOYEES, SETTINGS, HELP
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activePage = bOverview;
        // switchToOverviewRunner(); // start with overview page
    }

    private void switchToOverviewRunner() {
        mainWindow.loadPage(ActivePage.OVERVIEW);
        switchButtonColor(bOverview);
    }

    @FXML
    private void switchToOverview(MouseEvent event) {
        switchToOverviewRunner();
    }

    @FXML
    private void switchToJobs(MouseEvent event) {
        mainWindow.loadPage(ActivePage.JOBS);
        switchButtonColor(bJobs);
    }

    @FXML
    private void switchToEmployees(MouseEvent event) {
        mainWindow.loadPage(ActivePage.EMPLOYEES);
        switchButtonColor(bEmployees);
    }

    @FXML
    private void switchToSettings(MouseEvent event) {
        mainWindow.loadPage(ActivePage.SETTINGS);
        switchButtonColor(bSettings);
    }

    @FXML
    private void switchToHelp(MouseEvent event) {
        mainWindow.loadPage(ActivePage.HELP);
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
