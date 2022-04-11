package peoplesoft.ui;

import peoplesoft.ui.regions.SideBar;

/**
 * A relation class between mainwindow and sidebar which handles the page switching
 *
 * responsible for changing sidebar's colours and listening to commands from mainwindow and switching in main window
 */
public class PageSwitcher {
    private MainWindow mw;
    private SideBar sb;

    public enum PageValues {
        OVERVIEW, HELP, EXIT
    }

    /**
     * Creates the PageSwitcher association class.
     *
     * @param mainW the main window which will update the page.
     * @param sideB the sidebar which will update the indicated page.
     */
    public PageSwitcher(MainWindow mainW, SideBar sideB) {
        mw = mainW;
        sb = sideB;
    }

    /**
     * Decides which page to switch to. Only takes in enums to guard the input.
     *
     * @param p the page that will be switched to
     * @throws IllegalArgumentException when an invalid page is passed
     */
    public void switchOnCommand(PageValues p) throws IllegalArgumentException {
        assert p != null;
        switch(p) {
        case OVERVIEW:
            loadOverviewPage();
            break;
        case HELP:
            loadHelpPage();
            break;
        case EXIT:
            exitApp();
            break;
        default:
            throw new IllegalArgumentException("User attempted to switch to an invalid page.");
        }

    }

    private void loadOverviewPage() {
        sb.activateOverviewButton();
        mw.loadOverviewPage();
    }

    private void loadHelpPage() {
        sb.activateHelpButton();
        mw.loadHelpPage();
    }

    private void exitApp() {
        sb.activateExitButton();
        mw.handleExit();
    }
}
