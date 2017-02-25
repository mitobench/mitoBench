package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import view.groups.GroupController;
import view.table.controller.TableControllerUserBench;

import java.util.List;

/**
 * Created by neukamm on 15.02.17.
 */
public class GroupMenu {

    private GroupController groupController;
    private TableControllerUserBench tableController;
    private Menu menuGroup;
    private Menu groupByColumnItem;
    private final Menu groupingItem;

    public GroupMenu(GroupController gc, TableControllerUserBench tb){
        menuGroup = new Menu("Grouping");
        groupController = gc;
        tableController = tb;

        groupingItem = new Menu("Grouping");
        groupingItem.setId("grouping");

        groupByColumnItem = new Menu("Group by column");
        groupByColumnItem.setId("group_by_column");

        groupingItem.getItems().add(groupByColumnItem);

        addSubMenus();
    }

    private void addSubMenus(){


        groupByColumnItem.getItems().removeAll(groupByColumnItem.getItems());
        for(String col : tableController.getCurrentColumnNames() ){
            MenuItem colItem = new MenuItem(col);
            groupByColumnItem.getItems().add(colItem);
            colItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    //
                    groupController.createGroupByColumn(colItem.getText(), "");
                }
            });
        }

        MenuItem delOwnGrouping = new MenuItem("Delete own grouping");
        delOwnGrouping.setId("delOwnGrouping");
        delOwnGrouping.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                String group_col = groupController.getColname_group();
                if(group_col.equals("Group (Grouping)")){
                    tableController.removeColumn(group_col);
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

        menuGroup.getItems().addAll(groupingItem, new SeparatorMenuItem(), delOwnGrouping, delGrouping);

    }

    public Menu getMenuGroup() {
        return menuGroup;
    }

    public void upateGroupItem(List<String> colnames, GroupController groupController){
        groupByColumnItem.getItems().removeAll(groupByColumnItem.getItems());
        for(String col : colnames ){
            MenuItem colItem = new MenuItem(col);
            groupByColumnItem.getItems().add(colItem);
            colItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    //
                    groupController.createGroupByColumn(colItem.getText(), "");

                }
            });
        }
    }


}
