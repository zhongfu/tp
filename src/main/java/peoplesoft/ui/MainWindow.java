package peoplesoft.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import peoplesoft.commons.core.GuiSettings;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.logic.Logic;
import peoplesoft.logic.commands.CommandResult;
import peoplesoft.logic.commands.exceptions.CommandException;
import peoplesoft.logic.parser.exceptions.ParseException;
import peoplesoft.ui.regions.CommandBox;
import peoplesoft.ui.regions.ResultDisplay;
import peoplesoft.ui.regions.SideBar;
import peoplesoft.ui.scenes.HelpPage;
import peoplesoft.ui.scenes.OverviewPage;
import peoplesoft.ui.scenes.Page;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private SideBar sideBar;
    private ResultDisplay resultDisplay;
    private OverviewPage overviewPage;
    private HelpPage helpPage;

    @FXML
    private BorderPane bp;

    @FXML
    private StackPane pagePlaceholder;

    @FXML
    private StackPane sideBarPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private Button helpButton;

    @FXML
    private StackPane resultDisplayPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
    }

    /**
     * Gets the primary stage. Used to show fatal errors.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        sideBar = new SideBar(this);
        sideBarPlaceholder.getChildren().add(sideBar.getRoot());

        helpPage = new HelpPage(resultDisplay, logic.getCommandHelpMessageList());
        overviewPage = new OverviewPage(logic.getFilteredPersonList(), logic.getFilteredJobList());
        loadOverviewPage();
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Loads the page on the right side of the app
     *
     * @param page to be loaded
     */
    private void loadPage(Page page) {
        bp.setCenter(page.getRoot());
    }

    /**
     * Swaps the currently displayed page with the Overview page
     */
    public void loadOverviewPage() {
        loadPage(overviewPage);
    }

    /**
     * Swaps the currently displayed page with the Help page
     */
    public void loadHelpPage() {
        loadPage(helpPage);
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        primaryStage.hide();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see peoplesoft.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                loadHelpPage();
            } else if (commandResult.isExit()) {
                handleExit();
            } else {
                loadOverviewPage();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
