package peoplesoft.ui.regions;

import java.util.Comparator;
import java.util.List;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import peoplesoft.model.person.Person;
import peoplesoft.ui.UiPart;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label idx;
    @FXML
    private Label name;
    @FXML
    private Label amtDue;
    @FXML
    private FlowPane tags;
    @FXML
    private Label phone;
    @FXML
    private Label basePay;
    @FXML
    private Label email;
    @FXML
    private Label address;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, List<ReadOnlyDoubleProperty> colWidths) {
        super(FXML);

        List<? extends Region> cols = List.of(idx, name, amtDue, tags, phone, basePay, email, address);
        if (cols.size() != colWidths.size()) {
            throw new RuntimeException("JobCard colWidths count != cols count");
        }
        for (int i = 0; i < cols.size(); i++) {
            cols.get(i).minWidthProperty().bind(colWidths.get(i));
            cols.get(i).maxWidthProperty().bind(colWidths.get(i));
        }

        this.person = person;
        idx.setText(displayedIndex + "");
        name.setText(person.getName().fullName);
        amtDue.setText(person.getAmountDue().toString());
        basePay.setText(person.getRate().toString());
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);
        ReadOnlyDoubleProperty tagPaneWidthProperty = tags.widthProperty();
        ObservableList<Node> visibleTags = tags.getChildren();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label lbl = new Label(tag.tagName);
                    lbl.setWrapText(true);
                    lbl.setTextAlignment(TextAlignment.LEFT);
                    lbl.maxWidthProperty().bind(tagPaneWidthProperty);
                    visibleTags.add(lbl);
                });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return idx.getText().equals(card.idx.getText())
                && person.equals(card.person);
    }
}
