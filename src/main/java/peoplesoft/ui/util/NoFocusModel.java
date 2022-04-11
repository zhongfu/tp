package peoplesoft.ui.util;

import javafx.scene.control.FocusModel;

/**
 * inspired by
 * https://stackoverflow.com/questions/20621752/javafx-make-listview-not-selectable-via-mouse#comment105729822_46186195
 */
public class NoFocusModel<T> extends FocusModel<T> {

    @Override
    protected int getItemCount() {
        return 0;
    }

    @Override
    protected T getModelItem(int index) {
        return null;
    }
}
