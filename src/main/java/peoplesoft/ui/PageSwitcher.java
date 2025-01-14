package peoplesoft.ui;

import peoplesoft.ui.regions.SideBar;

/**
 * A relation class between mainwindow and sidebar which handles the page switching
 * todo: to be completed after making PeopleCell and JobCell for the 2 lists and displaying them properly.
 *
 * responsible for changing sidebar's colours and listening to commands from mainwindow and switching in main window
 */
public class PageSwitcher {
    private MainWindow mw;
    private SideBar sb;

    private enum PageValues {
        OVERVIEW, HELP
    }

    private void switchOnCommand(PageValues p) throws IllegalArgumentException {
        assert p != null;
        switch(p) {
        case HELP:
            loadHelpPage();
            break;
        case OVERVIEW:
            loadOverviewPage();
            break;
        default:
            throw new IllegalArgumentException("User attempted to switch to an invalid page.");
        }

    }

    public void loadHelpPage() {

    }

    public void loadOverviewPage() {

    }
}
