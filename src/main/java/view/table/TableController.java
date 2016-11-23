package view.table;

import io.datastructure.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 07.11.16.
 */
public class TableController {

    private TableView<ObservableList> table;
    private Label label;
    private List<String> col_names;

    private ObservableList<ObservableList> data;
    private ObservableList<ObservableList> data_copy;

    private DataTable dataTable;
    private HashMap<String, Integer> column_to_index;



    public TableController(Label label, Scene scene){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));

        table = new TableView();
        table.setEditable(false);
        //table.setColumnResizePolicy((param) -> true );

        table.prefHeightProperty().bind(scene.heightProperty());
        table.prefWidthProperty().bind(scene.widthProperty());


        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();
        column_to_index = new HashMap<String, Integer>();

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
     * set table to old/initial state
     *
     */
    public void resetTable() {
        data.removeAll(data);
        for(ObservableList item : data_copy){
            data.add(item);
        }
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

//        table.refresh();
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
     * count occurrences of haplotypes within selected data
     * return as hash map to plot it easily
     *
     * @return
     */
    public HashMap<String, Integer> getDataHist(){

        HashMap<String, Integer> haplo_to_count = new HashMap<>();
        for(ObservableList item : this.data){


            String haplogroup = (String)item.get(column_to_index.get("Haplogroup"));

            if(haplo_to_count.containsKey(haplogroup)){
                haplo_to_count.put(haplogroup, haplo_to_count.get(haplogroup)+1);
            } else {
                haplo_to_count.put(haplogroup,1);
            }
        }

        return  haplo_to_count;
    }



    public TableColumn getTableColumnByName(TableView<ObservableList> tableView, String name) {
        for (TableColumn col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }

    public TableView getTable() {
        return table;
    }

    public Label getLabel() {
        return label;
    }

    public ObservableList<ObservableList> getData() {
        return data;
    }

    public List<String> getCol_names() {
        return col_names;
    }

    private void setColumns_to_index(){
        int i = 0;
        for(TableColumn col : this.table.getColumns()){
            column_to_index.put(col.getText(),i);
            i++;
        }
    }

    public int getHaploColIndex(){

        return column_to_index.get("Haplogroup");
//        for(int i = 0; i < col_names.size(); i++){
//            if (col_names.equals("Haplogroup")){
//                return i;
//            }
//        }
//        return -1;
    }



    public List<String> getCurrentColumnNames(){
        List<String> names = new ArrayList<String>();

        for (TableColumn col : table.getColumns())
            names.add(col.getText());
        return names;
    }
}
