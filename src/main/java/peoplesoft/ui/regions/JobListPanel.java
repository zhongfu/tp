package peoplesoft.ui.regions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import peoplesoft.commons.core.LogsCenter;
import peoplesoft.model.Model;
import peoplesoft.model.employment.Employment;
import peoplesoft.model.job.Job;
import peoplesoft.model.person.Person;
import peoplesoft.ui.UiPart;

/**
 * Panel containing the list of persons.
 */
public class JobListPanel extends UiPart<Region> {
    private static final String FXML = "JobListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobListPanel.class);
    private final Model model;
    private final ObservableList<Person> personList;

    private List<ReadOnlyDoubleProperty> colWidths;

    @FXML
    private ListView<Job> jobListView;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Job> jobList, ObservableList<Person> personList,
            Model model, List<ReadOnlyDoubleProperty> colWidths) {
        super(FXML);
        this.colWidths = colWidths;
        jobListView.setItems(jobList);
        jobListView.setCellFactory(listView -> new JobListViewCell());
        this.personList = personList;
        this.model = model;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Job} using a {@code Job}.
     */
    class JobListViewCell extends ListCell<Job> {
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);

            if (empty || job == null) {
                setGraphic(null);
                setText(null);
            } else {
                // find the people assigned to this job
                HashMap<Integer, Person> assignedIndexes = new HashMap<>();
                List<Person> peopleAssigned = Employment.getInstance().getPersons(job, model);

                peopleAssigned.stream()
                        .sorted(Comparator.comparingInt(this::getOneBased))
                        .forEach(person -> assignedIndexes.put(getOneBased(person), person));

                // add a new divider before also!
                // <StackPane fx:id="divider" layoutX="10.0" layoutY="21.0"
                // prefHeight="2.0" style="-fx-background-color: #2e2d42;" />
                setGraphic(new JobCard(job, getIndex() + 1, assignedIndexes, colWidths).getRoot());
            }
        }

        /**
         * Returns the index of the person in the displayed employees list.
         *
         * @param p the person being displayed
         * @return the transient index of that person in the current list
         */
        private int getOneBased(Person p) {
            return personList.indexOf(p) + 1;
        }
    }

}
