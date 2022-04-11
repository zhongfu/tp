package peoplesoft.ui.controls;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewFocusModel;
import javafx.scene.layout.VBox;
import peoplesoft.ui.util.TableNoSelectionModel;

public class PeoplesoftTablePane<S> extends VBox {
    @FXML
    private TableView<S> table;

    @FXML
    private Label label;
    /**
     * Instantiates a new instance of {@code PeoplesoftTable}.
     */
    public PeoplesoftTablePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(PeoplesoftTablePane.class.getResource("/view/PeoplesoftTablePane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        table.setSelectionModel(new TableNoSelectionModel<>(table));
        table.setFocusModel(new TableViewFocusModel<>(table));
    }

    public final void setItems(ObservableList<S> items) {
        table.setItems(items);
    }

    public final TableView<S> getTable() {
        return table;
    }

    public final void setLabel(String text) {
        label.setText(text);
    }

    public final String getLabel() {
        return label.getText();
    }
}
