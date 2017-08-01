package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.Group;

import java.util.*;

/**
 * Created by neukamm on 25.11.2016.
 */
public class GroupController {

    private HashMap<String, Group> allGroups = new HashMap<>();
    private TableControllerUserBench tableController;
    private boolean groupingExists = false;
    private String colname_group;

    public GroupController(TableControllerUserBench tableController){
        this.tableController = tableController;
    }


    public void createGroupByColumn(String colName, String gname){

        if(groupingExists){
            clearGrouping();
        }
        groupingExists = true;

        TableColumn column = tableController.getTableColumnByName(colName);

        if(column==null){
            ObservableList group_data = tableController.getSelectedRows();
            tableController.addColumn(colName + " (Grouping)", 0);
            colname_group = colName + " (Grouping)";
            createGroup(gname);
            addElements(group_data, gname);
        } else {
            if(!colName.contains("(Grouping)")){
                tableController.changeColumnName(colName, colName+" (Grouping)");
                colname_group = colName+" (Grouping)";
            } else {
                colname_group = colName;
            }

            HashMap<String, ObservableList<ObservableList>> group_row = new HashMap();
            // get elements if columns as list with only unique entries
            Set<String> columnData = new HashSet<>();
            for (Object item : tableController.getTable().getItems()) {
                String entry = (String) column.getCellObservableValue(item).getValue();
                columnData.add(entry);
                if(group_row.containsKey(entry)){
                    ObservableList<ObservableList> tmp = group_row.get(entry);
                    tmp.add((ObservableList) item);
                    group_row.put(entry, tmp);
                } else {
                    ObservableList rows = FXCollections.observableArrayList();
                    rows.add(item);
                    group_row.put(entry, rows);
                }
            }

            for(String groupname : columnData){
                createGroup(groupname);
                addElements(group_row.get(groupname), groupname);
            }
        }

        updateGrouping();

    }

    private void updateGrouping() {

        ObservableList<ObservableList> toRemove = FXCollections.observableArrayList();
        ObservableList<ObservableList> toAdd = FXCollections.observableArrayList();
        int index_grouping = tableController.getColIndex("Grouping");
        if(index_grouping!=-1){

            for(String key :allGroups.keySet()){
                Group g = allGroups.get(key);
                for (ObservableList entry : g.getEntries()){
                    int index_ID = tableController.getColIndex("ID");
                    String id = (String)entry.get(index_ID);
                    ObservableList<ObservableList> tableEntries =  tableController.getTable().getItems();
                    for(ObservableList entry_curr : tableEntries){
                        if(entry_curr.get(index_ID).equals(id)){
                            toRemove.add(entry);
                            toAdd.add(entry_curr);
                        }
                    }
                }
                g.removeElements(toRemove);
                g.addElements(toAdd);
                toRemove.clear();
                toAdd.clear();
            }
        }

    }


    private void createGroup(String groupname){
        groupingExists = true;
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

    public void removeElements(ObservableList<ObservableList> elements, int groupingColIndex){

        for(int i = 0; i < elements.size(); i++){
            allGroups.get(elements.get(i).get(groupingColIndex)).removeElement(elements.get(i));
        }

    }


    public void clearGrouping(){
        if(groupingExists){
            groupingExists = false;
            allGroups.clear();
            tableController.changeColumnName(colname_group, colname_group.split("\\(")[0]);
            tableController.cleanColnames();
            tableController.cleanTableContent("(Grouping)");
            colname_group=null;
            if(tableController.getTableColumnByName("All data")!=null)
                tableController.removeColumn("All data");
        }
    }

    public Set<String> getGroupnames(){
        return allGroups.keySet();
    }

    public void clear(){
        allGroups.clear();
    }

    public String getColname_group() {
        return colname_group;
    }

    public boolean isGroupingExists() {
        return groupingExists;
    }

    public void setGroupingExists(boolean groupingExists) {
        this.groupingExists = groupingExists;
    }

    public HashMap<String, Group> getAllGroups() {
        return allGroups;
    }

    public HashMap<String, Group> getGroupsWithoutUndefined() {
        HashMap<String, Group> allGroups_new = new HashMap<>();

        for(String key : allGroups.keySet()){
            if(!key.equals("Undefined")){
                allGroups_new.put(key, allGroups.get(key));
            }
        }

        return allGroups_new;
    }

    public int getNumberOfGroups(){
        return allGroups.size();
    }
}