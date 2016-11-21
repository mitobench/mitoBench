package view.table;

import io.datastructure.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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



    public TableController(Label label){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));

        table = new TableView();
        table.setEditable(false);

        // allow multiple selection of rows in tableView
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        data = FXCollections.observableArrayList();
        data_copy = FXCollections.observableArrayList();
        col_names = new ArrayList<>();

        dataTable = new DataTable();

    }


    /**
     * This method gets a hashmap of new input entries, updates the data table and prepares the tableview for updating.
     * The columns are created based on new data table.
     *
     * @param input
     */
    public void updateTable(HashMap<String, List<Entry>> input) {

        // add new values to existing one
        // (DataTable)
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
        table.setItems(data);
    }


    /**
     * This method parses the data table to a representation that can be displayed from the table view
     * (ObservableList<ObservableList> )
     *
     * @param dataTable
     * @return
     */
    private ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable){

        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();
        HashMap<String, String[]> data_hash = dataTable.getDataTable();

        String[][] data_tmp = new String[data_hash.get("ID").length][data_hash.size()];

        int m = 0;
        for(String col : data_hash.keySet()){
            String[] col_entry = data_hash.get(col);
            for(int j = 0; j < col_entry.length; j++){
                data_tmp[j][m] = col_entry[j];
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
}
