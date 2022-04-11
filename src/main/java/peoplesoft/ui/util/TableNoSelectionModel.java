package peoplesoft.ui.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

/**
 * inspired by https://stackoverflow.com/a/46186195
 */
public class TableNoSelectionModel<T> extends TableViewSelectionModel<T> {

    public TableNoSelectionModel(TableView<T> tableView) {
        super(tableView);
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<TablePosition> getSelectedCells() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public void selectIndices(int index, int... indices) {
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void selectPrevious() {
    }

    @Override
    public void selectNext() {
    }

    @Override
    public void selectFirst() {
    }

    @Override
    public void selectLast() {
    }

    @Override
    public void selectLeftCell() {
    }

    @Override
    public void selectRightCell() {
    }

    @Override
    public void selectAboveCell() {
    }

    @Override
    public void selectBelowCell() {
    }

    @Override
    public void clearAndSelect(int index) {
    }

    @Override
    public void clearAndSelect(int row, TableColumn<T, ?> column) {
    }

    @Override
    public void select(int index) {
    }

    @Override
    public void select(T obj) {
    }

    @Override
    public void select(int row, TableColumn<T, ?> column) {
    }

    @Override
    public void clearSelection(int index) {
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void clearSelection(int row, TableColumn<T, ?> column) {
    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isSelected(int row, TableColumn<T, ?> column) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
