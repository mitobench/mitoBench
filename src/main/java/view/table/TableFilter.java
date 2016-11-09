package view.table;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

/**
 * Created by neukamm on 09.11.16.
 */
public class TableFilter {

    public TableFilter(){

    }

    public void filter(TableView table, String[] haplogroups){
        ArrayList<String> values = new ArrayList<>();
        ObservableList<TableColumn> columns = table.getColumns();

        for (Object row : table.getItems()) {
            for (TableColumn column : columns) {
                System.out.println(column.getId());
                //values.add((String) column.getCellObservableValue(row).getValue());
                //System.out.println((String) column.getCellObservableValue(row).getValue());
            }
        }

    }
}
