package view.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableManager {

    private TableView table;
    private Label label;

    private ObservableList<TableDataModel> data;


    public TableManager(Label label){

        this.label = label;
        this.label.setFont(new Font("Arial", 20));

        table = new TableView();
        table.setEditable(false);

        data = FXCollections.observableArrayList();


    }

    public void addColumn(String attribute){

        TableColumn column = new TableColumn(attribute);
        column.setCellValueFactory(new PropertyValueFactory<TableDataModel, String>(attribute));
        column.setSortType(TableColumn.SortType.DESCENDING);
        table.getColumns().add(column);
    }

    public void addEntry(TableDataModel entry){
        data.add(entry);
        table.setItems(data);
    }

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
}
