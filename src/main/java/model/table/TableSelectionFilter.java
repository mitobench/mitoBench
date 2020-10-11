package model.table;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;
import controller.ATableController;

import java.util.Arrays;

/**
 * Created by neukamm on 09.11.16.
 */
public class TableSelectionFilter {


    public TableSelectionFilter(){

    }

    public void haplogroupFilter(ATableController tableController, String[] haplogroups,
                                 int columnIndexhaplo, Logger LOG){

        if(columnIndexhaplo >=0){

            // get table
            TableView tableView = tableController.getTable();
            ObservableList<ObservableList> masterData_selection;

            if(tableView.getSelectionModel().getSelectedItems().size()==0){
                masterData_selection = tableView.getItems();
            } else {
                masterData_selection = tableView.getSelectionModel().getSelectedItems();
            }

            // 1. Wrap the ObservableList in a FilteredList (initially display all view.data).
            FilteredList<ObservableList> filteredData = new FilteredList<>(masterData_selection, p -> true);

            // 2. Set the haplogroupFilter Predicate whenever the haplogroupFilter changes.
            filteredData.setPredicate(t -> Arrays.stream(haplogroups).anyMatch(t.get(columnIndexhaplo)::equals));

            // 3. Wrap the FilteredList in a SortedList.
            SortedList<ObservableList> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            // 5. Add sorted (and filtered) view.data to the table.
            tableController.updateView(sortedData);

        } else {
            throw new IllegalArgumentException("Column 'Haplogroup' does not exist.");
        }

    }

}
