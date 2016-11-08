package view.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableManager {

    private TableView table;
    private Label label;
    private List<String> col_names;
    private int id_intern;

    private ObservableList<TableDataModel> data;


    public TableManager(Label label){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));

        table = new TableView();
        table.setEditable(false);

        data = FXCollections.observableArrayList();
        col_names = new ArrayList<>();
        id_intern = 1;


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

    public int getId_intern() {
        id_intern++;
        return id_intern-1;

    }
}
