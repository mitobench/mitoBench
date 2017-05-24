package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import controller.GroupController;
import view.table.controller.TableControllerUserBench;

import java.util.Arrays;
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
    private Logger LOG;

    public GroupMenu(MitoBenchWindow mitoBenchWindow){
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());

        menuGroup = new Menu("Grouping");
        menuGroup.setId("menu_grouping");
        groupController = mitoBenchWindow.getGroupController();
        tableController = mitoBenchWindow.getTableControllerUserBench();

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
                    LOG.info("Group data on column: " + colItem.getText());
                    groupController.createGroupByColumn(colItem.getText(), "");
                }
            });
        }



        MenuItem delGrouping = new MenuItem("Delete grouping");
        delGrouping.setId("delGrouping");
        delGrouping.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.info("Delete grouping on column '" + groupController.getColname_group() + "' with groups "
                        + Arrays.toString(groupController.getGroupnames().toArray()));

                if(groupController.isOwnGroupingIsSet()){
                    String group_col = groupController.getColname_group();
                    LOG.info("Delete grouping with groups: " + Arrays.toString(groupController.getGroupnames().toArray()));
                    if(group_col.equals("Group (Grouping)")){
                        tableController.removeColumn(group_col);
                        groupController.setOwnGroupingIsSet(false);
                    }
                } else {
                    groupController.clearGrouping();
                }
            }
        });

        menuGroup.getItems().addAll(groupingItem, new SeparatorMenuItem(), delGrouping);

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
                    LOG.info("Create grouping on column: " + colItem.getText());
                    groupController.createGroupByColumn(colItem.getText(), "");

                }
            });
        }
    }


}
