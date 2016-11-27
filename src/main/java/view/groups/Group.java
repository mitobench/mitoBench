package view.groups;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by neukamm on 25.11.2016.
 */
public class Group {
    private String name = "";
    private ObservableList entries = FXCollections.observableArrayList();

    public Group(String name){
        this.name = name;
    }


    public void addElement(ObservableList element) {


    }

    public void addElements(ObservableList<ObservableList> elements) {

    }

    public void removeElement(ObservableList element) {

    }

    public void removeElements(ObservableList<ObservableList> elements) {

    }
}
