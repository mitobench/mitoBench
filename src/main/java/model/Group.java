package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by neukamm on 25.11.2016.
 */
public class Group {
    private String name = "";
    private ObservableList<ObservableList> entries = FXCollections.observableArrayList();

    public Group(String name){
        this.name = name;
    }


    public void addElement(ObservableList element) {
        entries.add(element);

    }

    public void addElements(ObservableList<ObservableList> elements) {
        entries.addAll(elements);
    }

    public void removeElement(ObservableList element) {
        entries.removeAll(element);
    }

    public void removeElements(ObservableList<ObservableList> elements) {
        entries.removeAll(elements);
    }

    public ObservableList<ObservableList> getEntries() {
        return entries;
    }
}
