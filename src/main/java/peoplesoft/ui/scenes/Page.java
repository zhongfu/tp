package peoplesoft.ui.scenes;

import javafx.scene.layout.Region;
import peoplesoft.ui.UiPart;

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
