package view.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GenericTable<S> extends TableView {
    public GenericTable(ObservableList data, String col) {
        super(data);
        TableColumn<S, String> tc = new TableColumn<>(col);
        tc.setCellValueFactory(new PropertyValueFactory<>(col));
        getColumns().add(tc);
    }
}