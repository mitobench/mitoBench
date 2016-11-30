package view.table;

import io.datastructure.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;
import view.groups.AddToGroupDialog;
import view.groups.CreateGroupDialog;
import view.groups.GroupController;

import java.util.*;


/**
 * Created by neukamm on 07.11.16.
 */
public class TableController {

    private TableView<ObservableList> table;
    private List<String> col_names;

    private ObservableList<ObservableList> data;
    private ObservableList<ObservableList> data_copy;

    private DataTable dataTable;
    private HashMap<String, Integer> column_to_index;
    private TableController controller;
    private GroupController groupController;
    private TableMover tableMover;





    public TableController(Scene scene){

        table = new TableView();
        table.setEditable(false);
        //table.setColumnResizePolicy((param) -> true );

        table.prefHeightProperty().bind(scene.heightProperty());
        table.prefWidthProperty().bind(scene.widthProperty());


        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // table mover
//        tableMover = new TableMover(table);
//        tableMover.setMoving();
//        tableMover.setZooming();




        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();
        column_to_index = new HashMap<String, Integer>();

        this.controller = this;
        groupController = new GroupController();

    }





    /**
     * This method gets a hash map of new input entries, updates the data table and prepares the table view for updating.
     * The columns are created based on new data table.
     *
     * @param input
     */
    public void updateTable(HashMap<String, List<Entry>> input) {

        // add new values to existing one (DataTable)
        dataTable.update(input);

        // clean whole table
        data.clear();
        table.getColumns().removeAll(table.getColumns());

        // display updated table
        data = parseDataTableToObservableList(dataTable);

        // add columns
        int i = 0;
        for(String colName : dataTable.getDataTable().keySet()){
            int j = i;
            TableColumn col = new TableColumn(colName);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            col_names.add(colName);
            table.getColumns().addAll(col);
            i++;

        }
        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(data);

        setColumns_to_index();
        setContextMenu();



    }


    /**
     * This method parses the data table to a representation that can be displayed by the table view
     * (ObservableList<ObservableList> )
     *
     * @param dataTable
     * @return
     */
    private ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable){


        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();

        HashMap<String, String[]> data_hash = dataTable.getDataTable();
        String[][] data_tmp = new String[dataTable.getDataTable().get("ID").length][dataTable.getDataTable().keySet().size()];

        int m = 0;
        for(String col : data_hash.keySet()){
            String[] col_entry = data_hash.get(col);
            for(int j = 0; j < col_entry.length; j++){
                String e = col_entry[j];
                if(e != null){
                    data_tmp[j][m] = col_entry[j];
                } else {
                    data_tmp[j][m] = "NA";
                }
            }
            m++;
        }


        for(int i = 0 ; i < data_tmp.length; i++){
            ObservableList row = FXCollections.observableArrayList();
            for(int j = 0 ; j < data_tmp[i].length; j++){
                String value = data_tmp[i][j];
                row.add(value);
            }
            parsedData.add(row);
        }

        return parsedData;
    }



    /**
     * update table if some selections were done in tableView
     * @param newItems
     */
    public void updateView(ObservableList<ObservableList> newItems){
        copyData();

        ObservableList<ObservableList> data_selection = FXCollections.observableArrayList();
        for(ObservableList item : newItems){
            data_selection.add(item);
        }

        data.removeAll(data);
        for(ObservableList item : data_selection){
            data.add(item);
        }

        this.table.setItems(data);

    }


    /**
     * copy data to always allow resetting of table
     * to old/initial state
     */
    public void copyData(){
        if(data_copy.size()==0){
            for(ObservableList item : data){
                data_copy.add(item);
            }
        }
    }



    /**
     * create new table entry for each selected item to easily update tableview
     * @return
     */
    public HashMap<String, List<Entry>> createNewEntryList(String gName){

        HashMap<String, List<Entry>> entries = new HashMap<>();

        for(int i = 0; i < table.getSelectionModel().getSelectedItems().size(); i++){
            String rowName = table.getSelectionModel().getSelectedItems().get(i).get(getColIndex("ID")).toString();
            List<Entry> eList = new ArrayList<>();
            Entry e = new Entry("Grouping", "String", gName);
            eList.add(e);
            entries.put(rowName, eList);
        }

        return entries;
    }


    /**
     * set table to old/initial state
     *
     */
    public void resetTable() {
        data.removeAll(data);
        for(ObservableList item : data_copy){
            data.add(item);
        }
    }


    /*


                Getter



     */


    /**
     * This method counts occurrences of haplotypes within selected data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<String, Integer> getDataHist(){

        ObservableList<ObservableList> data_to_plot;
        HashMap<String, Integer> haplo_to_count = new HashMap<>();

        if(table.getSelectionModel().getSelectedItems().size() == 0 || table.getSelectionModel().getSelectedItems() == null){
            data_to_plot = this.data;
        } else {
            data_to_plot = table.getSelectionModel().getSelectedItems();
        }
        for(ObservableList item : data_to_plot){

            String haplogroup = (String)item.get(column_to_index.get("Haplogroup"));

            if(haplo_to_count.containsKey(haplogroup)){
                haplo_to_count.put(haplogroup, haplo_to_count.get(haplogroup)+1);
            } else {
                haplo_to_count.put(haplogroup,1);
            }
        }

        return  haplo_to_count;
    }


    /**
     * This method returns a table column of specific column name
     *
     * @param name
     * @return
     */
    public TableColumn getTableColumnByName(String name) {
        for (TableColumn col : table.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }


    /**
     * This method returns all column names displayed in current table view
     * @return
     */
    public List<String> getCurrentColumnNames(){
        List<String> names = new ArrayList<String>();

        for (TableColumn col : table.getColumns())
            names.add(col.getText());
        return names;
    }

    /**
     *
     * This method parses the current table view to a data - table representation (ObservableList<ObservableList<String>>)
     * which can be used for output purposes for example
     *
     * @return
     */
    public ObservableList<ObservableList<String>> getViewDataCurrent() {

        TableView tableView = this.getTable();

        ObservableList<ObservableList<String>> all = FXCollections.observableArrayList();
        ObservableList<TableColumn> columns = tableView.getColumns();

        for (Object row : tableView.getItems()) {
            ObservableList<String> values = FXCollections.observableArrayList();
            for (TableColumn column : columns) {
                String val = (String) column.getCellObservableValue(row).getValue();
                if(column.getText().equals("MTSequence")){
                    values.add(
                            dataTable.getMtStorage().getData().get(val).get(0));
                } else {
                    values.add(
                            (String) column.getCellObservableValue(row).getValue());
                }

            }
            all.add(values);
        }

        return all;


    }

    /**
     * get column index of column based on column header
     *
     * @param key
     * @return
     */

    public int getColIndex(String key){
        return column_to_index.get(key);
    }

    public TableView getTable() {
        return table;
    }

    public ObservableList<ObservableList> getData() {
        return data;
    }

    public DataTable getDataTable() { return dataTable; }

    public GroupController getGroupController() {
        return groupController;
    }

    public int getCountPerHG(String hg, String group, int colIndexHG, int colIndexGroup){

        int count = 0;
        ObservableList<ObservableList> selection = table.getItems();
        for(int i = 0; i < selection.size(); i++){
            ObservableList list = selection.get(i);
            if(list.get(colIndexGroup).equals(group) && list.get(colIndexHG).equals(hg)){
                count++;
            }
        }
        return count;
    }

    /*


                Setter



     */

    public void setTable(TableView<ObservableList> table) {
        this.table = table;
    }

    /**
     *  save to each column the index (column number)
     */

    private void setColumns_to_index(){
        int i = 0;
        for(TableColumn col : this.table.getColumns()){
            column_to_index.put(col.getText(),i);
            i++;
        }
    }

    private void setContextMenu(){

        final ContextMenu menu = new ContextMenu();

        final MenuItem addNewGropuItem = new MenuItem("Create new group");
        addNewGropuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CreateGroupDialog createGroupDialog =
                        new CreateGroupDialog(groupController, table, controller);
            }
        });

        final MenuItem addAllSelectedItem
                = new MenuItem("Add to group");
        addAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddToGroupDialog addToGroupDialog =
                        new AddToGroupDialog(groupController, table.getSelectionModel().getSelectedItems(), controller);
            }
        });


//        final MenuItem deleteAllSelectedItem
//                = new MenuItem("Remove selected item(s)");
//        deleteAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                groupController.removeElements(table.getSelectionModel().getSelectedItems(), getColIndex("Grouping"));
//            }
//        });
//
//        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem, deleteAllSelectedItem);
        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem);
        table.setContextMenu(menu);
    }


    public String[] removeDuplicates(String[] arr) {
        return new HashSet<String>(Arrays.asList(arr)).toArray(new String[0]);
    }



}
