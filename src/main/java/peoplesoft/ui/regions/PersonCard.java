package peoplesoft.ui.regions;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        idx.setText(displayedIndex + "");
        name.setText(person.getName().fullName);
        amtDue.setText(person.getAmountDue().toString());
        basePay.setText(person.getRate().toString());
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
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
