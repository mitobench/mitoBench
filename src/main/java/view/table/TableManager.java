package view.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableManager {

    private TableView table;
    private Label label;
    private List<String> col_names;

    private ObservableList<TableDataModel> data;
    private ObservableList<TableDataModel> data_copy;


    public TableManager(Label label){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));

        table = new TableView();
        table.setEditable(false);

        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

    }


    /**
     * add column to table, attribute is column name
     * @param attribute
     */
    public void addColumn(String attribute){

        TableColumn column = new TableColumn(attribute);
        column.setCellValueFactory(new PropertyValueFactory<TableDataModel, String>(attribute));
        column.setSortType(TableColumn.SortType.DESCENDING);
        table.getColumns().add(column);
        col_names.add(attribute);
    }

    /**
     * add single table entry
     * @param entry
     */
    public void addEntry(TableDataModel entry){
        data.add(entry);
        table.setItems(data);
    }

    /**
     * add list of table entries
     * @param entryList
     */
    public void addEntryList(List<TableDataModel> entryList){
        for(TableDataModel entry : entryList){
            data.add(entry);
        }
        table.setItems(data);

    }

    /**
     * update table if some selections were done in tableView
     * @param newItems
     */
    public void updateView(ObservableList<TableDataModel> newItems){

        ObservableList<TableDataModel> data_selection = FXCollections.observableArrayList();
        for(TableDataModel item : newItems){
            data_selection.add(item);
        }

        data.removeAll(data);
        for(TableDataModel item : data_selection){
            data.add(item);
        }

        table.refresh();
    }


    /**
     * copy data to always allow resetting of table
     * to old/initial state
     */
    public void copyData(){
        if(data_copy.size()==0){
            for(TableDataModel item : data){
                data_copy.add(item);
            }
        }
    }

    /**
     * set table to old/initial state
     *
     */
    public void resetTable() {
        data.removeAll(data);
        for(TableDataModel item : data_copy){
            data.add(item);
        }
    }


    /**
     * count occurrences of haplotypes within selected data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<String, Integer> getDataHist(){

        HashMap<String, Integer> haplo_to_count = new HashMap<>();
        for(TableDataModel item : this.data){
            String haplogroup = item.getHaplogroup();

            if(haplo_to_count.containsKey(haplogroup)){
                haplo_to_count.put(haplogroup, haplo_to_count.get(haplogroup)+1);
            } else {
                haplo_to_count.put(haplogroup,1);
            }
        }

        return  haplo_to_count;
    }

    public TableView getTable() {
        return table;
    }

    public Label getLabel() {
        return label;
    }

    public ObservableList<TableDataModel> getData() {
        return data;
    }

    public List<String> getCol_names() {
        return col_names;
    }

}
