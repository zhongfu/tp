package peoplesoft.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import peoplesoft.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL =
            "https://ay2122s2-cs2103t-t11-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Open the User Guide";
    public static final String COPIED_MESSAGE = "Browser opened";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button openInBrowserButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.fine("Showing the link to PeopleSoft's help page.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
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
                    logger.warning("Unable to launch browser due to the OS.");
                }

            }

            logger.fine("User Guide successfully opened in browser.");

        } catch (URISyntaxException se) {
            logger.warning("The URL in the application was wrong. Please contact developers.");
        } catch (IOException ie) {
            logger.warning("Unable to launch OS's browser. Please contact developers.");
        } finally {
            helpMessage.setText(COPIED_MESSAGE);
        }
    }
}
