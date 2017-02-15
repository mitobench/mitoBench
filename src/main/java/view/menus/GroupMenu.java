package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import view.groups.GroupController;
import view.table.TableControllerUserBench;

/**
 * Created by neukamm on 15.02.17.
 */
public class GroupMenu {

    private Menu menuGroup;
    private GroupController groupController;
    private TableControllerUserBench tableController;

    public GroupMenu(GroupController gc, TableControllerUserBench tb){
        menuGroup = new Menu("Grouping");
        groupController = gc;
        tableController = tb;
        addSubMenus();
    }

    private void addSubMenus(){
        Menu groupingItem = new Menu("Grouping");
        groupingItem.setId("grouping");


        MenuItem delOwnGrouping = new MenuItem("Delete own grouping");
        delOwnGrouping.setId("delOwnGrouping");
        delOwnGrouping.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                String group_col = groupController.getColname_group();
                if(group_col.equals("Group (Grouping)")){
                    tableController.removeColumn(group_col);
                    //groupController.clearGrouping();
                    //tableController.cleanColnames();
                }
            }
        });

        MenuItem delGrouping = new MenuItem("Delete grouping");
        delGrouping.setId("delGrouping");
        delGrouping.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                groupController.clearGrouping();
            }
        });

        menuGroup.getItems().addAll( new SeparatorMenuItem(), delOwnGrouping, delGrouping);

    }

    public Menu getMenuGroup() {
        return menuGroup;
    }
}
