package peoplesoft.ui;

import javafx.scene.layout.Region;

/**
 * Models a page in the PeopleSoft application.
 * All pages inherit from this class.
 */
public abstract class Page extends UiPart<Region> {

    /**
     * Creates a {@code Page} from the {@code fxmlFileName}
     */
    public Page(String fxmlFileName) {
        super(fxmlFileName);
    }
}
