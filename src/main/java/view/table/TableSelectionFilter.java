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

    public void filter(TableManager tablemanager, String[] haplogroups){

        // get table
        TableView tableView = tablemanager.getTable();
        ObservableList<TableDataModel> masterData_selection;
        if(tableView.getSelectionModel().getSelectedItems().size()==0){
            masterData_selection = tableView.getItems();
        } else {
            masterData_selection = tableView.getSelectionModel().getSelectedItems();
        }



        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TableDataModel> filteredData = new FilteredList<>(masterData_selection, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filteredData.setPredicate(new Predicate<TableDataModel>(){
            public boolean test(TableDataModel t){
                return Arrays.stream(haplogroups).anyMatch(t.getHaplogroup()::equals);
            }
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<TableDataModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tablemanager.updateView(sortedData);


    }

}
