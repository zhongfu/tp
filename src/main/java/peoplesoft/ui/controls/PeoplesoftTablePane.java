package peoplesoft.ui.controls;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

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
