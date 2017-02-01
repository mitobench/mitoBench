package view.table;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by neukamm on 09.11.16.
 */
public class TableSelectionFilter {


    public TableSelectionFilter(){

    }

    public void haplogroupFilter(TableControllerUserBench tableController, String[] haplogroups, int columnIndexhaplo){

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
            filteredData.setPredicate(new Predicate<ObservableList>(){
                public boolean test(ObservableList t){
                    return Arrays.stream(haplogroups).anyMatch(t.get(columnIndexhaplo)::equals);
                }
            });

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
