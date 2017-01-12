package view.table;

import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private MTStorage mtStorage;

    private HashMap<String, List<Entry>> table_content;



    public TableController(Scene scene) {

        table = new TableView();
        table.setEditable(false);

        table.prefHeightProperty().bind(scene.heightProperty());
        table.prefWidthProperty().bind(scene.widthProperty());

        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();
        column_to_index = new HashMap<>();

        this.controller = this;
        groupController = new GroupController();
        mtStorage = new MTStorage();

        table_content = new HashMap<>();

    }





    /**
     * This method gets a hash map of new input entries, updates the view.data table and prepares the table view for updating.
     * The columns are created based on new view.data table.
     *
     * @param input
     */
    public void updateTable(HashMap<String, List<Entry>> input) {

        // update Enrty structure
        updateEntryList(input);

        // add new values to existing one (DataTable)
        dataTable.update(input);

        // clean whole table
        data.clear();

        // get current col names
        List<String> curr_colnames = getCurrentColumnNames();

        table.getColumns().removeAll(table.getColumns());

        // define column order
        Set<String> cols = dataTable.getDataTable().keySet();
        for(String s : cols) {
            if(!curr_colnames.contains(s))
                curr_colnames.add(s);
        }

        // display updated table
        data = parseDataTableToObservableList(dataTable, curr_colnames);


        // add columns
        int i = 0;
        for(String colName : curr_colnames) {
            int j = i;
            TableColumn col = new TableColumn(colName);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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


    private void updateEntryList(HashMap<String, List<Entry>> input_new) {

        for(String key_new : input_new.keySet()){
            if(table_content.containsKey(key_new)){
                // update entry
                List<Entry> entries = table_content.get(key_new);
                List<Entry> entries_new = input_new.get(key_new);

                boolean hit;
                for (Entry e_new : entries_new){
                    hit = false;
                    for(Entry e : entries){
                        if(e_new.getIdentifier().equals(e.getIdentifier())){
                            e = e_new;
                            hit=true;
                        }
                    }
                    if(!hit)
                        entries.add(e_new);


                }


            }  else {
                //  create new Entry
                table_content.put(key_new, input_new.get(key_new));
            }

        }

    }

    /**
     * This method parses the view.data table to a representation that can be displayed by the table view
     * (ObservableList<ObservableList> )
     *
     * @param dataTable
     * @return
     */
    private ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable, List<String> curr_colnames){

        if(curr_colnames.size()==0){
            curr_colnames = new ArrayList<String>(dataTable.getDataTable().keySet());
        }


        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();

        HashMap<String, String[]> data_hash = dataTable.getDataTable();

        String[][] data_tmp = new String[dataTable.getDataTable().get("ID").length][dataTable.getDataTable().keySet().size()];

        int m = 0;
        for(String col : curr_colnames){
            String[] col_entry = data_hash.get(col);
            for(int j = 0; j < col_entry.length; j++){
                String e = col_entry[j];
                if(e != null){
                    data_tmp[j][m] = col_entry[j];
                } else {
                    data_tmp[j][m] = "Undefined";
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
     * copy view.data to always allow resetting of table
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
            Entry e = new Entry("Grouping", new CategoricInputType("String"), new GenericInputData(gName));
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
     * This method counts occurrences of haplotypes within selected view.data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<String, Integer> getDataHist(String name_to_filter){

        ObservableList<ObservableList> data_to_plot;
        HashMap<String, Integer> haplo_to_count = new HashMap<>();

        if(table.getSelectionModel().getSelectedItems().size() == 0 || table.getSelectionModel().getSelectedItems() == null){
            data_to_plot = this.data;
        } else {
            data_to_plot = table.getSelectionModel().getSelectedItems();
        }
        for(ObservableList item : data_to_plot){

            String haplogroup = (String)item.get(column_to_index.get(name_to_filter));

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
     * This method parses the current table view to a view.data - table representation (ObservableList<ObservableList<String>>)
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
                            mtStorage.getData().get(val));
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

    public double getCountPerHG(String hg, String group, int colIndexHG, int colIndexGroup){

        double count = 0.0;
        ObservableList<ObservableList> selection = getSelectedRows();
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

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem);
        table.setContextMenu(menu);
    }



    /**
     * This method returns all selected rows. If no row is selected, all rows are returned.
     * @return
     */
    public ObservableList<ObservableList> getSelectedRows(){

        ObservableList<ObservableList> selectedTableItems;
        if(table.getSelectionModel().getSelectedItems().size() != 0){
            selectedTableItems = table.getSelectionModel().getSelectedItems();
        } else {
            selectedTableItems = table.getItems();
        }

        return selectedTableItems;

    }

    public void loadGroups(){

        // if "grouping" exists, create groups
        if (getCurrentColumnNames().contains("Grouping")){
            String groupname;
            //iterate over rows
            for (Object row : table.getItems()) {
                ObservableList row_parsed = (ObservableList) row;

                groupname = (String) getTableColumnByName("Grouping").getCellObservableValue(row).getValue();

                if(!groupController.getAllGroups().keySet().contains(groupname)) {
                    groupController.createGroup(groupname);
                }
                groupController.addElement(row_parsed, groupname);
            }
        }
    }


    public HashMap<String, List<Entry>> getTable_content() {
        return table_content;
    }
}
