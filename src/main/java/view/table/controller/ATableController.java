package view.table.controller;

import database.ColumnNameMapper;
import io.IInputType;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import view.datatypes.IData;
import view.groups.GroupController;
import view.menus.GroupMenu;
import view.table.DataTable;

import java.util.*;

/**
 * Created by neukamm on 01.02.17.
 */
public abstract class ATableController {



    protected TableView<ObservableList> table;

    protected ObservableList<ObservableList> data;
    protected ObservableList<ObservableList> data_copy;

    protected DataTable dataTable;
    protected HashMap<String, Integer> column_to_index;
    protected HashMap<String, List<Entry>> table_content;
    protected ATableController controller;
    protected GroupController groupController;
    protected List<String> col_names;
    protected List<String> col_names_sorted;
    protected GroupMenu groupMenu;

    public ATableController(){

    }

    public void init(){

        table = new TableView();
        table.setEditable(false);
        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();
        column_to_index = new HashMap<>();
        this.controller = this;
        table_content = new HashMap<>();

    }

    /**
     * This method gets a hash map of new input entries, updates the view.data table and prepares the table view for updating.
     * The columns are created based on new view.data table.
     *
     * @param input
     */
    public void updateTable(HashMap<String, List<Entry>> input) {
        // update Entry structure
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
            if(!curr_colnames.contains(s.trim()))
                curr_colnames.add(s);
        }

        // display updated table
        data = parseDataTableToObservableList(dataTable, curr_colnames);

        // add columns
        for(int i = 0; i < col_names_sorted.size(); i++) {
            addColumn(col_names_sorted.get(i), i);
        }

        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(data);

        setColumns_to_index();

        groupMenu.upateGroupItem(col_names_sorted, groupController);

    }


    protected void updateEntryList(HashMap<String, List<Entry>> input_new) {

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
                table_content.put(key_new.trim(), input_new.get(key_new));
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
    protected ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable, List<String> curr_colnames){

        if(curr_colnames.size()==0){
            curr_colnames = new ArrayList<String>(getCurrentColumnNames());
        }

        // set column order (ID -> MT Sequence -> Others)
        col_names_sorted = new ArrayList<>();
        if(curr_colnames.contains("ID")){
            col_names_sorted.add("ID");
            curr_colnames.remove("ID");
        }
        if(curr_colnames.contains("MTSequence")){
            col_names_sorted.add("MTSequence");
            curr_colnames.remove("MTSequence");
        }
        Collections.sort(curr_colnames);
        col_names_sorted.addAll(curr_colnames);


        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();

        HashMap<String, String[]> data_hash = dataTable.getDataTable();

        String[][] data_tmp = new String[dataTable.getDataTable().get("ID").length][dataTable.getDataTable().keySet().size()];

        int m = 0;
        for(String col : col_names_sorted){
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
        data_copy = copyData();
        data.removeAll(data);
        data.addAll(newItems);
        this.table.setItems(data);
    }


    /**
     * copy view.data to always allow resetting of table
     * to old/initial state
     */
    public ObservableList<ObservableList> copyData(){
        ObservableList<ObservableList> copy = FXCollections.observableArrayList();
        if(copy.size()==0){
            for(ObservableList item : data){
                copy.add(item);
            }
        }

        return copy;
    }



    /**
     * create new table entry for each selected item to easily update tableview
     * @return
     */
    public HashMap<String, List<Entry>> createNewEntryListForGrouping(String gName, String colName){

        HashMap<String, List<Entry>> entries = new HashMap<>();

        for(int i = 0; i < table.getSelectionModel().getSelectedItems().size(); i++){
            String rowName = table.getSelectionModel().getSelectedItems().get(i).get(getColIndex("ID")).toString();
            List<Entry> eList = new ArrayList<>();
            Entry e = new Entry(colName, new CategoricInputType("String"), new GenericInputData(gName));
            eList.add(e);
            entries.put(rowName, eList);
        }

        return entries;
    }


    public HashMap<String, List<Entry>>  createNewEntryListDragAndDrop(ObservableList<ObservableList> items){

        HashMap<String, List<Entry>> entries = new HashMap<>();
        ColumnNameMapper mapper = new ColumnNameMapper();
        for(int i = 0; i < items.size(); i++) {
            ObservableList item = items.get(i);
            String rowName = items.get(i).get(getColIndex("ID")).toString();
            List<Entry> eList = new ArrayList<>();
            List<String> colnames = getCurrentColumnNames();
            for(int k = 0; k < item.size(); k++){

                Entry e = new Entry(mapper.mapString(colnames.get(k)), new CategoricInputType("String"), new GenericInputData(item.get(k).toString()));
                eList.add(e);
            }
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

    public void addColumn(String colname, int j){

        TableColumn col = new TableColumn(colname);
        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
            }
        });

        col_names.add(colname);
        table.getColumns().addAll(col);

    }


    public void addGroupmenu(GroupMenu groupMenu){
        this.groupMenu = groupMenu;
    }

    public void changeColumnName(String oldname, String newname) {
        for (TableColumn col : table.getColumns()){
            if(col.getText().equals(oldname)){
                col.setText(newname.trim());
            }
        }
        setColumns_to_index();
        dataTable.updateDatatable(oldname, newname);
    }

    public void cleanColToIndex() {
        column_to_index.clear();
    }

    public void cleanColnames() {
        col_names.removeAll(col_names);
        for(TableColumn col : table.getColumns()){
            col_names.add(col.getText());
        }
    }

    public void removeColumn(String colname_group) {
        // remove from tableview
        for(TableColumn col : table.getColumns()){
            if(col.getText().equals(colname_group)){
                table.getColumns().remove(col);
                break;
            }
        }

        // remove from data
        ObservableList<ObservableList> data_new = FXCollections.observableArrayList();
        int index = getColIndex(colname_group);
        for(ObservableList list : data){
            list.remove(index);
            data_new.add(list);
        }

        // remove from datatable
        dataTable.getDataTable().remove(colname_group);
        cleanColnames();
        col_names_sorted.remove(colname_group);
        setColumns_to_index();
        table.getItems().removeAll(table.getItems());
        cleanTableContent(colname_group);
        resetTable();
        updateTable(table_content);

    }

    public void cleanTableContent(String group_colname){
        for(String key : table_content.keySet()){
            for(Entry e : table_content.get(key)){
                if(e.getIdentifier().equals(group_colname)){
                    table_content.get(key).remove(e);
                    break;
                } else if(e.getIdentifier().contains(group_colname)){
                    String id = e.getIdentifier();
                    IData d = e.getData();
                    IInputType type = e.getType();
                    table_content.get(key).remove(e);
                    table_content.get(key).add(new Entry(id.split(" \\(")[0].trim(), type, d));
                    break;
                }
            }
        }


    }


    public void loadGroups(){
        // if "grouping" column already exists, create groups
        for(String colname : getCurrentColumnNames()){
            if(colname.contains("(Grouping)")){
                groupController.createGroupByColumn(colname, "");
                break;
            }
        }
    }




    /*


                Getter



     */



    public HashMap<String, List<Entry>> getTable_content() {
        return table_content;
    }



    /**
     * This method counts occurrences of haplotypes within selected view.data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<String, Integer> getDataHist(String[] data){
        HashMap<String, Integer> haplo_to_count = new HashMap<>();

        for(String haplogroup : data){
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
        if(name.equals("Grouping")){
            for (TableColumn col : table.getColumns()) {
                if(col.getText().contains("Grouping")){
                    return col;
                }
            }

        } else if(name.contains("Haplogroup")){
            for (TableColumn col : table.getColumns()) {
                if (col.getText().contains("Haplogroup")) {
                    return col;
                }
            }
        } else {
            for (TableColumn col : table.getColumns()) {
                if (col.getText().equals(name)) {
                    return col;
                }
            }
        }



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
     * which can be used for output purposes for example.
     *
     * @return
     */
    public ObservableList<ObservableList<String>> getViewDataCurrent() {

        TableView tableView = this.getTable();

        ObservableList<ObservableList<String>> all = FXCollections.observableArrayList();
        ObservableList<TableColumn> columns = tableView.getColumns();

        for (Object row : tableView.getItems()) {
            String id = "";
            ObservableList<String> values = FXCollections.observableArrayList();
            for (TableColumn column : columns) {
                String val = (String) column.getCellObservableValue(row).getValue();
                if(column.getText().equals("MTSequence")) {
                    values.add(dataTable.getMtStorage().getData().get(id));
                } else if(column.getText().equals("ID")){
                    id = val;
                    values.add((String) column.getCellObservableValue(row).getValue());
                } else {
                    values.add((String) column.getCellObservableValue(row).getValue());
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
        if(key.equals("Grouping")){
            for(String s : column_to_index.keySet()){
                if(s.contains("Grouping")){
                    return column_to_index.get(s);
                }
            }
        } else if(key.equals("Haplogroup")){
            for(String s : column_to_index.keySet()){
                if(s.contains("Haplogroup")){
                    return column_to_index.get(s);
                }
            }

        } else {
            return column_to_index.get(key);
        }
        // this return will never reached
        return -1;
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

    public void setColumns_to_index(){
        cleanColToIndex();
        int i = 0;
        for(TableColumn col : this.table.getColumns()){
            column_to_index.put(col.getText(),i);
            i++;
        }
        groupMenu.upateGroupItem(getCurrentColumnNames(), groupController);
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




    public void setGroupController(GroupController groupController) {
        this.groupController = groupController;
    }
}
