package peoplesoft.ui.scenes;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.logic.commands.CommandHelpMessage;
import peoplesoft.ui.controls.PeoplesoftTablePane;
import peoplesoft.ui.regions.ResultDisplay;

public class HelpPage extends Page {

    public static final String USERGUIDE_URL =
            "https://ay2122s2-cs2103t-t11-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Open the User Guide";
    public static final String COPIED_MESSAGE = "Browser opened";

    private static ResultDisplay display;
    private static final Logger logger = LogsCenter.getLogger(HelpPage.class);
    private static final String FXML = "HelpPage.fxml";

    @FXML
    private Button openInBrowserButton;

    @FXML
    private Label helpMessage;

    /**
     * Tutorial on how to add data to a table from
     * https://medium.com/@keeptoo/adding-data-to-javafx-tableview-stepwise-df582acbae4f
     */
    @FXML
    private PeoplesoftTablePane<CommandHelpMessage> helpTablePane;

    @FXML
    private TableColumn<CommandHelpMessage, String> command;

    @FXML
    private TableColumn<CommandHelpMessage, String> format;

    @FXML
    private TableColumn<CommandHelpMessage, String> examples;

    private Callback<
            TableColumn<CommandHelpMessage, String>,
            TableCell<CommandHelpMessage, String>> cellFactory = col -> {
                TableCell<CommandHelpMessage, String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);

                text.getStyleClass().add("text");
                text.wrappingWidthProperty().bind(cell.widthProperty().subtract(20));
                text.textProperty().bind(cell.itemProperty());
                return cell;
            };

    /**
     * Creates a new {@code HelpPage}
     */
    public HelpPage(ResultDisplay rd, ObservableList<CommandHelpMessage> commandHelpMessageList) {
        super(FXML);
        logger.fine("Opening PeopleSoft's Help page.");
        helpMessage.setText(HELP_MESSAGE);
        display = rd;

        command.setCellFactory(cellFactory);
        command.setCellValueFactory(new PropertyValueFactory<>("command"));
        format.setCellFactory(cellFactory);
        format.setCellValueFactory(new PropertyValueFactory<>("format"));
        examples.setCellFactory(cellFactory);
        examples.setCellValueFactory(new PropertyValueFactory<>("examples"));

        helpTablePane.getTable().setItems(commandHelpMessageList);
    }

    /**
     * Opens the User Guide in the Browser.
     * @@author adapted from https://stackoverflow.com/a/54869038/16777554 by Dave
     */
    @FXML
    private void openInBrowser() {
        logger.fine("URL to open: " + USERGUIDE_URL); // change to fine logging
        String oS = System.getProperty("os.name").toLowerCase();
        logger.fine("Operating system detected: " + oS);

        Runtime runtime = Runtime.getRuntime();

        try {
            if (oS.contains("mac")) {
                logger.fine("Using 'open' on Mac to open webpage.");
                Process open = runtime.exec("open " + USERGUIDE_URL);
                logger.fine("Opened.");
            } else if (oS.contains("nix") || oS.contains("nux")) {
                logger.fine("Using 'xdg-open' on Linux to open webpage.");
                Process open = runtime.exec("xdg-open " + USERGUIDE_URL);
                logger.fine("Opened.");
            } else if (oS.contains("win") && Desktop.isDesktopSupported()) {
                logger.fine("Using Desktop.browse on Windows to open webpage.");
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(USERGUIDE_URL));
            } else {
                String msg = "Unable to launch browser due to the OS.";
                logger.warning(msg);
                display.setFeedbackToUser(msg);
                return;
            }
            logger.fine("User Guide successfully opened in browser.");
            display.setFeedbackToUser(COPIED_MESSAGE);
        } catch (URISyntaxException se) {
            String msg = "The URL in the application was wrong. Please contact developers.";
            logger.warning(msg);
            display.setFeedbackToUser(msg);
        } catch (IOException ie) {
            String msg = "Unable to launch OS's browser. Please contact developers.";
            logger.warning(msg);
            display.setFeedbackToUser(msg);
        }
    }
}
