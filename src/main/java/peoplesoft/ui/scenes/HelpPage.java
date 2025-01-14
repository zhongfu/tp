package peoplesoft.ui.scenes;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import peoplesoft.commons.core.LogsCenter;
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
    private TableView<String> table;

    /**
     * Creates a new {@code HelpPage}
     */
    public HelpPage(ResultDisplay rd) {
        super(FXML);
        logger.fine("Opening PeopleSoft's Help page.");
        helpMessage.setText(HELP_MESSAGE);
        display = rd;
    }

    /**
     * Opens the User Guide in the Browser.
     * Solution referenced from https://stackoverflow.com/a/54869038/16777554
     */
    @FXML
    private void openInBrowser() {
        logger.fine("URL to open: " + USERGUIDE_URL); // change to fine logging
        String oS = System.getProperty("os.name").toLowerCase();
        logger.fine("Operating system detected: " + oS);

        try {
            if (Desktop.isDesktopSupported()) { // Windows
                logger.fine("Using Desktop.browse on Windows to open webpage.");
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(USERGUIDE_URL));

            } else {
                Runtime runtime = Runtime.getRuntime();

                if (oS.contains("mac")) {
                    logger.fine("Using 'open' on Mac to open webpage.");
                    Process open = runtime.exec("open " + USERGUIDE_URL);
                    logger.fine("Opened. Exit value is " + open.exitValue());

                } else if (oS.contains("nix") || oS.contains("nux")) {
                    logger.fine("Using 'xdg-open' on Linux to open webpage.");
                    Process open = runtime.exec("xdg-open " + USERGUIDE_URL);
                    logger.fine("Opened. Exit value is " + open.exitValue());
                } else {
                    String msg = "Unable to launch browser due to the OS.";
                    logger.warning(msg);
                    display.setFeedbackToUser(msg);
                }

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
