package view.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableDataModel<T> {

    private SimpleStringProperty ID;
    private ArrayList<T> data;


    public TableDataModel(List<T> args) {
        this.ID = new SimpleStringProperty((String)args.get(0));
        data = new ArrayList<T>(args.subList(1,args.size()));
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) { this.ID = new SimpleStringProperty(ID); }


}
