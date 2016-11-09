package view.table;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableDataModel {

    private final SimpleStringProperty ID;
    private final SimpleStringProperty MTsequence;
    private final SimpleStringProperty dating;
    private final SimpleStringProperty haplogroup;
    private int id_intern;

    public TableDataModel(String[] entry) {
        // todo: make this better !! just messie to change it here everytime!
        this.id_intern++;
        this.ID = new SimpleStringProperty(entry[0]);
        this.MTsequence = new SimpleStringProperty(entry[1]);
        this.dating = new SimpleStringProperty(entry[2]);
        this.haplogroup = new SimpleStringProperty(entry[3]);

    }

    public String getID() {
        return ID.get();
    }

    public String getMTsequence() {
        return MTsequence.get();
    }

    public String getDating() {
        return dating.get();
    }

    public String getHaplogroup() {
        return haplogroup.get();
    }

    public int getId_intern() {
        return id_intern;
    }

    public void setId_intern(int id_intern) {
        this.id_intern = id_intern;
    }
}
