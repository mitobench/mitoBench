package view.table;

import io.datastructure.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableController {

    private GenericTable<TableDataModel> table;
    private Label label;
    private List<String> col_names;

    private ObservableList<TableDataModel> data;
    private ObservableList<TableDataModel> data_copy;

    private HashMap<String, List<Entry>> data_map;


    public TableController(Label label){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));
        table.setEditable(false);

        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        table = new GenericTable<>(data, "ID");

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
     * add entry based on file input
     * @param entry
     */
    public void addEntry( Entry entry, String id){

        String colname = entry.getIdentifier();
        String type = entry.getType();
        Object data = entry.getData();

        TableColumn col = getTableColumnByName(table, colname);
        if(col!=null){
            // just update
        } else { // create new column
            TableColumn newCOl = new TableColumn<>(colname);
            addColumn(colname);

            // add entry ID

            //table.getColumns().add(0, newCOl);

        }





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
//    public HashMap<String, Integer> getDataHist(){
//
//        HashMap<String, Integer> haplo_to_count = new HashMap<>();
//        for(TableDataModel item : this.data){
//            String haplogroup = item.getHaplogroup();
//
//            if(haplo_to_count.containsKey(haplogroup)){
//                haplo_to_count.put(haplogroup, haplo_to_count.get(haplogroup)+1);
//            } else {
//                haplo_to_count.put(haplogroup,1);
//            }
//        }
//
//        return  haplo_to_count;
//    }



    public void updateTable(HashMap<String, List<Entry>> input){

        // check if entry exists
        for(String id : input.keySet()){
            if(idExists(id, table)){
                // todo: update row
            } else { // create new row
                for(int i = 0; i < input.get(id).size(); i++){
                    addEntry(input.get(id).get(i), id);
                }

            }

        }

        System.out.println("table updated ...");
    }


    private boolean idExists(String id, TableView<TableDataModel> table){
        boolean tmp = false;

        for (Object r : table.getItems()) {
            for (Object c : table.getColumns()) {
                javafx.scene.control.TableColumn column = (javafx.scene.control.TableColumn) c;

                if (column.getCellData(r) != null && column.getCellData(r).equals(id)) {
                     tmp = true;
                }
            }
        }
        return tmp;
    }



//    public void populateTable(Class<?> T) {
//
//
//        int num_of_cols = getNUmberOfColumns();
//        try{
//
//            for(String s : data_map.keySet()){
//
//                for(int i = 0; i < data_map.get(s).size(); i++){
//                    Entry e = data_map.get(s).get(i);
//                    Object data = e.getData();
//                    String colname = (String)e.getIdentifier();
//                    TableColumn col = new TableColumn (colname.toUpperCase());
//                    col.prefWidthProperty().bind(table.widthProperty().divide(num_of_cols));
//                    col.setCellValueFactory(new PropertyValueFactory<**T,Integer**>(colname));
//                    table.getColumns().add(col);
//
//                }
//
//                // add ID value
//                TableDataModel tableDataModel = new TableDataModel();
//
//            }
//
//            table.setPlaceholder(new Label("Loading..."));
//            for (int column = 0; column < headerValues.length; column++) {
//                addColumn(tableManager.getCol_names().get(column));
//
//            }
//
//
//            // Data:
//            String dataLine;
//            while ((dataLine = in.readLine()) != null) {
//                final String[] dataValues = dataLine.split(",");
//                // Add additional columns if necessary:
//                for (int columnIndex = table.getColumns().size(); columnIndex < dataValues.length; columnIndex++) {
//                    tableManager.addColumn(tableManager.getCol_names().get(columnIndex));
//                }
//                // Add data to table:
//                TableDataModel entry = new TableDataModel(dataValues); // data values
//                tableManager.addEntry(entry);
//            }
//
//            tableManager.copyData();
//
//
//        } catch (IOException e){
//            System.err.println("IOException: " + e.getMessage());
//        }
//
//    }


    private int getNUmberOfColumns(){

        int size_pre = table.getColumns().size();

        for(String s : data_map.keySet()){
            for(int i = 0; i < data_map.get(s).size(); i++){
                size_pre++;
            }
        }
        return size_pre;
    }

    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
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

    public HashMap<String, List<Entry>> getData_map() {
        return data_map;
    }

    public void setData_map(HashMap<String, List<Entry>> data_map) {
        this.data_map = data_map;
    }
}
