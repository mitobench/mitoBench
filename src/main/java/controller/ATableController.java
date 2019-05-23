package controller;

import Logging.LogClass;
import database.ColumnNameMapper;
import io.IInputType;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import io.IData;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;
import view.menus.GroupMenu;
import model.table.DataTable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by neukamm on 01.02.17.
 */
public abstract class ATableController {

    protected final Logger LOG;
    protected TableView<ObservableList> table;
    protected ObservableList<ObservableList> data;
    protected ObservableList<ObservableList> data_initial;
    protected DataTable dataTable;
    protected HashMap<String, Integer> column_to_index;
    protected HashMap<String, List<Entry>> table_content;
    protected ATableController controller;
    protected GroupController groupController;
    protected List<String> col_names;
    protected List<String> col_names_sorted;
    protected GroupMenu groupMenu;
    protected LogClass logClass;
    protected Deque<HashMap<String, List<Entry>>> data_versions = new LinkedList();
    protected String[] customColumnOrder=null;


    public ATableController(LogClass logClass){
        this.logClass = logClass;
        LOG = logClass.getLogger(this.getClass());
    }

    public void init(){

        table = new TableView();
        table.setEditable(false);
        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_initial = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();
        column_to_index = new HashMap<>();
        this.controller = this;
        table_content = new HashMap<>();
        data_versions = new LinkedList<>();
    }

    /**
     * This method gets a hash map of new input entries, updates the view.data table and prepares the table view for updating.
     * The columns are created based on new view.data table.
     *
     * @param input
     */
    public void updateTable(HashMap<String, List<Entry>> input) {

        String groupname=null;
        if(groupController.groupingExists()){
            groupname=groupController.getColname_group().replace(" (Grouping)", "");
            groupController.clearGrouping();
        }

        // update Entry structure
        updateEntryList(input);

        // add new values to existing one (DataTable)
        dataTable.update(input);

        // clean whole table
        data.clear();

        // get current col names
        List<String> curr_colnames = getCurrentColumnNames();

        table.getColumns().removeAll(table.getColumns());


        Set<String> cols = dataTable.getDataTable().keySet();
        for(String s : cols) {
            if(!curr_colnames.contains(s.trim()))
                curr_colnames.add(s);
        }

        curr_colnames = curr_colnames.stream().distinct().collect(Collectors.toList());
        setColumns_to_index();

        // display updated table

        data = parseDataTableToObservableList(dataTable, curr_colnames, input.keySet(), customColumnOrder);
        // delete duplicated columns
        col_names_sorted = col_names_sorted.stream().distinct().collect(Collectors.toList());

        // add columns
        for(int i = 0; i < col_names_sorted.size(); i++) {
            addColumn(col_names_sorted.get(i), i);
        }

        updateVersion();

        // clear Items in table
        table.setItems(FXCollections.observableArrayList());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(data);

        setColumns_to_index();

        if(groupname!=null)
            groupController.createGroupByColumn(groupname,"");

        TableFilter filter = new TableFilter(table);

    }


    private void updateVersion() {

        if(data_versions.size()>=4){
            data_versions.removeFirst();
            data_versions.add((HashMap<String, List<Entry>>) table_content.clone());

        } else {
            data_versions.add((HashMap<String, List<Entry>>) table_content.clone());
        }
    }


    protected void updateEntryList(HashMap<String, List<Entry>> input_new) {


        for(String key_new : input_new.keySet()){
            if(key_new==null){
                List<Entry> elist = input_new.get(key_new);
                for (Entry e : elist){
                    if (e.getIdentifier().equals("accession_id"))
                        key_new = e.getData().getTableInformation().replace("\"","");
                }
            }
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
     *  This method parses the view.data table to a representation that can be displayed by the table view
     * (ObservableList<ObservableList> )
     *
     * @param dataTable
     * @param curr_colnames
     * @param ids
     * @param order
     * @return
     */
    protected ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable,
                                                                            List<String> curr_colnames,
                                                                            Set<String> ids,
                                                                            String[] order){

        if(curr_colnames.size()==0){
            curr_colnames = new ArrayList<>(getCurrentColumnNames());
        }
        col_names_sorted = new ArrayList<>();
        if(order==null){
            // set default column order (ID -> Haplogroup -> Population -> Geo location (Sample origin) --> others)
            if(curr_colnames.contains("ID")){
                col_names_sorted.add("ID");
                curr_colnames.remove("ID");
            }

            if(curr_colnames.contains("Haplogroup")){
                col_names_sorted.add("Haplogroup");
                curr_colnames.remove("Haplogroup");
            }

            if(curr_colnames.contains("Population")){
                col_names_sorted.add("Population");
                curr_colnames.remove("Population");
            }

            if(curr_colnames.contains("Latitude (Sampling origin)")){
                col_names_sorted.add("Latitude (Sampling origin)");
                curr_colnames.remove("Latitude (Sampling origin)");
            }

            if(curr_colnames.contains("Longitude (Sampling origin)")){
                col_names_sorted.add("Longitude (Sampling origin)");
                curr_colnames.remove("Longitude (Sampling origin)");
            }


            if(curr_colnames.contains("Latitude (Sample origin)")){
                col_names_sorted.add("Latitude (Sample origin)");
                curr_colnames.remove("Latitude (Sample origin)");
            }

            if(curr_colnames.contains("Longitude (Sample origin)")){
                col_names_sorted.add("Longitude (Sample origin)");
                curr_colnames.remove("Longitude (Sample origin)");
            }

            boolean containsSeq = false;
            if(curr_colnames.contains("MTSequence")){
                curr_colnames.remove("MTSequence");
                containsSeq = true;
            }

            Collections.sort(curr_colnames);
            col_names_sorted.addAll(curr_colnames);
            if(containsSeq)
                col_names_sorted.add("MTSequence");

        } else {
            // set user defined order
            List<String> curr_colnames_copy = new ArrayList<>(curr_colnames);
            for(String colname : order){
                if(curr_colnames_copy.contains(colname)){
                    col_names_sorted.add(colname);
                    curr_colnames_copy.remove(colname);
                }
            }

            col_names_sorted.addAll(curr_colnames_copy);
        }




        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();

        HashMap<String, String[]> data_hash = dataTable.getDataTable();
        if(data_hash.size()!=0){
            String[][] data_tmp = new String[dataTable.getDataTable().get("ID").length][dataTable.getDataTable().keySet().size()];

            int m = 0;
            for(String col : col_names_sorted){
                String[] col_entry = data_hash.get(col);
                for(int j = 0; j < col_entry.length; j++){
                    String e = col_entry[j];
                    if(e != null && !e.equals("null")){
                        data_tmp[j][m] = col_entry[j];
                    } else {
                        data_tmp[j][m] = "";
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
        }


        return parsedData;
    }


    /**
     * update table if some selections were done in tableView
     * @param newItems
     */
    public void updateView(ObservableList<ObservableList> newItems){

        // update version
        updateVersion();

        ObservableList<ObservableList> new_items_copy;
        new_items_copy = copyData(newItems);
        if(data_initial==null){
            data_initial = copyData(data);
        }
        data.removeAll(data);
        data.addAll(new_items_copy);
        this.table.setItems(data);

    }


    /**
     * copy view.data to always allow resetting of table
     * to old/initial state
     */
    public ObservableList<ObservableList> copyData(ObservableList<ObservableList> data_to_copy){
        ObservableList<ObservableList> copy = FXCollections.observableArrayList();
        if(copy.size()==0){
            for(ObservableList item : data_to_copy){
                copy.add(item);
            }
        }

        return copy;
    }



    /**
     * create new table entry for each selected item to easily update tableview
     * @return
     */
    public HashMap<String, List<Entry>> createNewEntryList(String textfield, String colName, boolean getAllRows){

        HashMap<String, List<Entry>> entries = new HashMap<>();
        ObservableList<ObservableList> selection;
        if(getAllRows){
            selection = table.getItems();

            for(int i = 0; i < selection.size(); i++){
                String rowName = selection.get(i).get(getColIndex("ID")).toString();
                List<Entry> eList = new ArrayList<>();
                Entry e = new Entry(colName, new CategoricInputType("String"), new GenericInputData(textfield));
                eList.add(e);
                entries.put(rowName, eList);
            }

            return entries;

        } else {
            selection = getSelectedRows();

            for(int i = 0; i < selection.size(); i++){
                String rowName = selection.get(i).get(getColIndex("ID")).toString();
                List<Entry> e_list = table_content.get(rowName);
                for(Entry e : e_list){
                    if(e.getIdentifier().equals(colName)){
                        Entry e_new = new Entry(e.getIdentifier(), e.getType(), new GenericInputData(textfield));
                        table_content.remove(e);
                        e_list.remove(e);
                        e_list.add(e_new);
                        table_content.put(rowName, e_list);
                    }
                }
            }
            return table_content;
        }
    }



    /**
     * Add new column to table
     *
     * @param colname
     * @param j
     */
    public void addColumn(String colname, int j){

        if(!getCurrentColumnNames().contains(colname)){
            TableColumn col = new TableColumn(colname);
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param
                    -> new SimpleStringProperty(param.getValue().get(j).toString()));

            col_names.add(colname);
            //col.prefWidthProperty().bindBidirectional((Property<Number>) table.widthProperty().multiply(0.07));
            //col.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
            table.getColumns().addAll(col);
        }
    }


    public void addGroupmenu(GroupMenu groupMenu){
        this.groupMenu = groupMenu;
    }

    /**
     * Changes the name of the column
     *
     * @param oldname
     * @param newname
     */
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

    /**
     * Removes column from table
     *
     * @param colName
     */
    public void removeColumn(String colName) {

        updateVersion();

        // remove grouping
        if(colName.contains("(Grouping)")){
            groupController.clearGrouping();
        }
        colName = colName.split("\\(")[0].trim();

        // remove from tableview
        for(TableColumn col : table.getColumns()){
            if(col.getText().equals(colName)){
                table.getColumns().remove(col);
                break;
            }
        }

        // remove from data
        ObservableList<ObservableList> data_new = FXCollections.observableArrayList();
        int index = getColIndex(colName);
        for(ObservableList list : table.getItems()){
            list.remove(index);
            data_new.add(list);
        }

        // remove from datatable
        dataTable.getDataTable().remove(colName);
        cleanColnames();
        col_names_sorted.remove(colName);
        setColumns_to_index();
        //table.getItems().removeAll(table.getItems());

        // remove column from datable content
        updateTableContent(colName);

        data.removeAll(data);
        for(ObservableList item : data_new){
            data.add(item);
        }
        updateEntriesTableContent(getColumnData(getTableColumnByName("ID")));
        updateTable(table_content);

    }

    private void updateEntriesTableContent(List<String> ids) {

        HashMap<String, List<Entry>> newTableContent = new HashMap<>();
        for(String key : table_content.keySet()){
            if(ids.contains(key)){
                newTableContent.put(key, table_content.get(key));
            }
        }

        table_content.clear();
        table_content = newTableContent;
    }


    public void updateTableContent(String group_colname){
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
                    table_content.get(key).add(new Entry(id.replace(" (Grouping)",""), type, d));
                    break;
                }
            }
        }
    }


    public void loadGroups(){

        // if "grouping" column already exists, create groups
        if(groupController.groupingExists()){
            groupController.clearGrouping();
        }
        for(String colname : getCurrentColumnNames()){
            if(colname.contains("(Grouping)")){
                groupController.createGroupByColumn(colname, "");
                groupController.setGroupingExists(true);
                break;
            }
        }
    }



    /**
     * Test whether table has entries or not.
     * @return
     */

    public boolean isTableEmpty(){
        if(table.getItems()!=null && table.getItems().size()>0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method removes all entries and deletes the grouping.
     */
    public void cleartable(){
        column_to_index.clear();
        col_names.clear();
        table_content.clear();
        if(col_names_sorted!=null)
            col_names_sorted.clear();
        // clean view.data model
        data.removeAll(data);
        // clean table view
        table.setItems(null);//getItems().clear();//removeAll(table.getItems());
        dataTable.getMtStorage().getData().clear();
        dataTable.getDataTable().clear();
        table.getColumns().removeAll(table.getColumns());

        if(groupController!=null){
            groupController.clear();
            groupController.clearGrouping();
        }
    }




    /*
                Getter
     */



    public HashMap<String, List<Entry>> getTable_content() {
        return table_content;
    }


    public HashMap<String, List<Entry>> getTable_content(ObservableList<ObservableList> data) {
        HashMap<String, List<Entry>> content_tmp = new HashMap<>();
        for(ObservableList entry : data){
            if(table_content.keySet().contains(entry.get(0))){
                content_tmp.put((String)entry.get(0), table_content.get(entry.get(0)));
            }
        }
        return content_tmp;
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
     * This method counts occurrences of haplotypes within selected view.data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<Integer, List<String>> getDataHist2(String[] data){
        HashMap<String, Integer> haplo_to_count = new HashMap<>();
        HashMap<Integer, List<String>> haplo_occurrences = new HashMap<>();

        for(String haplogroup : data){
            if(haplo_to_count.containsKey(haplogroup)){
                haplo_to_count.put(haplogroup, haplo_to_count.get(haplogroup)+1);
            } else {
                haplo_to_count.put(haplogroup,1);
            }
        }


        for(String key_hg : haplo_to_count.keySet()){
            int count = haplo_to_count.get(key_hg);

            if(haplo_occurrences.keySet().contains(count)){
                List<String> hgs = haplo_occurrences.get(count);
                hgs.add(key_hg);
                haplo_occurrences.put(count, hgs);
            } else {
                List<String> hgs = new ArrayList<>();
                hgs.add(key_hg);
                haplo_occurrences.put(count, hgs);
            }
        }




        return  haplo_occurrences;
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
     * get column index of column based on column header
     *
     * @param k
     * @return
     */

    public int getColIndex(String k){

        if(k.equals("Haplogroup")) {
            for (String s : column_to_index.keySet()) {
                if (s.equals("Haplogroup")) {
                    return column_to_index.get(s);
                }
            }
        } else if(k.equals("Grouping")){
            for(String s : column_to_index.keySet()){
                if(s.contains("Grouping")){
                    return column_to_index.get(s);
                }
            }
        } else if(column_to_index.keySet().contains(k)) {
            return column_to_index.get(k);
        }

        // column not contained
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

    public List<String> getCountPerHG(String hg, String group, int colIndexHG, int colIndexGroup){

        List<String> hgs = new ArrayList<>();
        ObservableList<ObservableList> selection = getSelectedRows();
        if(colIndexGroup == -1){
            for(int i = 0; i < selection.size(); i++){
                ObservableList list = selection.get(i);
                if(list.get(colIndexHG).equals(hg)){
                    hgs.add(hg);
                }
            }
        } else {
            for(int i = 0; i < selection.size(); i++){
                ObservableList list = selection.get(i);
                if(list.get(colIndexGroup).equals(group) && list.get(colIndexHG).equals(hg)){
                    hgs.add(hg);
                }
            }
        }

        return hgs;
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


    public void setGroupController(GroupController groupController) {
        this.groupController = groupController;
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


    public void resetToUnfilteredData(){
        if(data_versions.size()>1){
            //data_versions.removeLast();
            HashMap<String, List<Entry>> data_tmp = data_versions.getLast();
            if (data_tmp.equals(table.getItems())){
                data_versions.removeLast();
                data_tmp = data_versions.getLast();
            }
            data_versions.removeLast();
            updateTable(data_tmp);

        }
    }

    private List<String> getColumnData(TableColumn column){

        List<String> columnData = new ArrayList<>();
        for (ObservableList item : table.getItems()) {
            columnData.add((String) column.getCellObservableValue(item).getValue());
        }

        return columnData;
    }

    public HashMap<String, String> getHeadertypes(){
        HashMap<String, String> res = new HashMap<>();
        for(String key : table_content.keySet()){
            List<Entry> e_list = table_content.get(key);
            for(Entry e : e_list){
                res.put(e.getIdentifier(), e.getType().getTypeInformation());
            }
        }

        res.put("ID", "String");
        return res;
    }


    public void cleanVersions() {

        data_versions = new LinkedList<>();
    }

    public void copyColumn(String s, String newColname) {
        for(String key : table_content.keySet()){
            List<Entry> e_list = table_content.get(key);
            Entry e_copy=null;
            for(Entry e : e_list){
                if(e.getIdentifier().equals(s)){
                     e_copy = new Entry(newColname, e.getType(), e.getData());
                }
            }
            e_list.add(e_copy);
        }

        updateTable(table_content);

    }


    public void setCustomColumnOrder(String[] customColumnOrder) {
        this.customColumnOrder = customColumnOrder;
        //updateTable(null);
    }
}

