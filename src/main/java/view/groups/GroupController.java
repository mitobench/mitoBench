package view.groups;

import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by neukamm on 25.11.2016.
 */
public class GroupHandler {

    HashMap<String, Group> allGroups = new HashMap<>();

    public GroupHandler(){

    }


    public void createGroup(String groupname){
        Group g = new Group(groupname);
        allGroups.put(groupname, g);
    }

    public void addElement(ObservableList element, String groupname){
        allGroups.get(groupname).addElement(element);
    }

    public void addElements(ObservableList<ObservableList> elements, String groupname){
        allGroups.get(groupname).addElements(elements);
    }

    public void removeElement(ObservableList element, String groupname){
        allGroups.get(groupname).removeElement(element);
    }

    public void removeElements(ObservableList<ObservableList> elements, String groupname){
        allGroups.get(groupname).removeElements(elements);
    }
    public Set<String> getGroupnames(){
        return allGroups.keySet();
    }
}
