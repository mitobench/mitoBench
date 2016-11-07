package view.table;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableDataModel {

    private final SimpleStringProperty ID;
    private final SimpleStringProperty MTsequence;
    private final SimpleStringProperty dating;

    public TableDataModel(String id, String mtsequence, String dating) {
        this.ID = new SimpleStringProperty(id);
        this.MTsequence = new SimpleStringProperty(mtsequence);
        this.dating = new SimpleStringProperty(dating);
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getMTsequence() {
        return MTsequence.get();
    }

    public void setMTsequence(String MTsequence) {
        this.MTsequence.set(MTsequence);
    }

    public String getDating() {
        return dating.get();
    }

    public void setDating(String dating) {
        this.dating.set(dating);
    }
}
