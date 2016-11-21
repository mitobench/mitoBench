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

            table.getColumns().addAll(col);
            i++;

        }
        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.setItems(data);
    }



    private ObservableList<ObservableList> parseDataTableToObservableList(DataTable dataTable){

        ObservableList<ObservableList> parsedData = FXCollections.observableArrayList();
        HashMap<String, String[]> data_hash = dataTable.getDataTable();

        String[][] data_tmp = new String[data_hash.get("ID").length][data_hash.size()];

        //for(int i = 0; i < data_hash.get("ID").length; i++){
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

    /**
     * This method iterates over all columns and returns true,
     * if column 'col' exists.
     * @param col
     * @return
     */
    private boolean colExists(TableColumn col){

        for(int i = 0; i < table.getColumns().size(); i++){
            TableColumn c = (TableColumn) table.getColumns().get(i);
            String col_old = c.getText();
            String col_new = col.getText();
            if(c.getText().equals(col.getText())){
                return true;
            }
        }
        return false;
    }


    /**
     * This method returns true, if row with id 's' exists
     *
     * @param s
     * @return
     */
    private boolean rowExists(String s){
        for(int i = 0; i < data.size(); i++){
            ObservableList row = data.get(i);
            if(row.get(0).equals(s)){
                return true;
            }
        }
        return false;
    }


    private void updateCell(String rowID, Entry entry){


        for (Node r: table.lookupAll(".table-row-cell")){
            for (Node c: r.lookupAll(".table-cell")){
                System.out.println(c);
            }
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

}
