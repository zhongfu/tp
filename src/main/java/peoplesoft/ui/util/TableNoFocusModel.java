package peoplesoft.ui.util;

import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewFocusModel;

/**
 * inspired by
 * https://stackoverflow.com/questions/20621752/javafx-make-listview-not-selectable-via-mouse#comment105729822_46186195
 */
public class TableNoFocusModel<T> extends TableViewFocusModel<T> {

    public TableNoFocusModel(TableView<T> tableView) {
        super(tableView);
    }

    @Override
    protected int getItemCount() {
        return 0;
    }

    @Override
    protected T getModelItem(int index) {
        return null;
    }
}
