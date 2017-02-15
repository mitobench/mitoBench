package view.groups;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import view.table.ATableController;

import java.util.*;

/**
 * Created by neukamm on 25.11.2016.
 */
public class GroupController {

    private HashMap<String, Group> allGroups = new HashMap<>();
    private ATableController tableController;
    private boolean groupingIsSet = false;
    private String colname_group;

    public GroupController(ATableController tableController){
        this.tableController = tableController;
    }


    private void createGroup(String groupname){
        groupingIsSet = true;
        Group g = new Group(groupname);
        allGroups.put(groupname, g);
    }

    public void createGroupByColumn(String colName, String gname){
        if(groupingIsSet)
            clearGrouping();

        TableColumn column = tableController.getTableColumnByName(colName);

        if(column!=null){
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
                    tmp.addAll((ObservableList) item);
                    group_row.put(entry, tmp);
                } else {
                    ObservableList rows = FXCollections.observableArrayList();
                    rows.addAll(item);
                    group_row.put(entry, rows);
                }
            }

            for(String groupname : columnData){
                createGroup(groupname);
                addElements(group_row.get(groupname), groupname);
            }
        } else {
            ObservableList group_data = tableController.getTable().getSelectionModel().getSelectedItems();
            //TableColumn colGroup = new TableColumn(colName + " (Grouping)");
            tableController.addColumn(colName + " (Grouping)", 0);
            colname_group = colName + " (Grouping)";
            createGroup(gname);
            addElements(group_data, gname);
        }
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
        groupingIsSet = false;
        allGroups.clear();
        // reset table column
        tableController.changeColumnName(colname_group, colname_group.split("\\(")[0]);
        //tableController.cleanColToIndex();
        tableController.cleanColnames();
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
}
