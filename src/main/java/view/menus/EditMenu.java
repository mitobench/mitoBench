package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import view.groups.GroupController;

import java.util.List;

/**
 * Created by neukamm on 16.11.16.
 */
public class EditMenu {

    private Menu menuEdit;
    private Menu groupByColumnItem;

    public EditMenu(){
        menuEdit = new Menu("Edit");
        addSubMenus();
    }

    private void addSubMenus(){
        Menu groupingItem = new Menu("Grouping");
        groupingItem.setId("grouping");

        groupByColumnItem = new Menu("Group by column");
        groupByColumnItem.setId("group_by_column");

        groupingItem.getItems().addAll(groupByColumnItem);
        menuEdit.getItems().add(groupingItem);

    }

    public void upateGroupItem(List<String> colnames, GroupController groupController){
        groupByColumnItem.getItems().removeAll(groupByColumnItem.getItems());
        for(String col : colnames ){
            MenuItem colItem = new MenuItem(col);
            groupByColumnItem.getItems().add(colItem);
            colItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    //
                    groupController.createGroupByColumn(colItem.getText());

                }
            });
        }
    }


    public Menu getMenuEdit() {
        return menuEdit;
    }
}
