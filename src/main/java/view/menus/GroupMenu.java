package view.menus;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import controller.GroupController;
import controller.TableControllerUserBench;

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
    private Logger LOG;

    public GroupMenu(MitoBenchWindow mitoBenchWindow){
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());

        menuGroup = new Menu("Grouping");
        menuGroup.setId("menu_grouping");
        groupController = mitoBenchWindow.getGroupController();
        tableController = mitoBenchWindow.getTableControllerUserBench();

        groupByColumnItem = new Menu("Group by column");
        groupByColumnItem.setId("group_by_column");


        addSubMenus();
    }

    /**
     * Add all submenus to menu item
     */
    private void addSubMenus(){

        groupByColumnItem.getItems().removeAll(groupByColumnItem.getItems());
        for(String col : tableController.getCurrentColumnNames() ){
            MenuItem colItem = new MenuItem(col);
            groupByColumnItem.getItems().add(colItem);
            colItem.setOnAction(t -> {
                if(!colItem.getText().contains("(Grouping)")){
                    groupController.clearGrouping();
                    groupController.createGroupByColumn(colItem.getText(), "");
                    LOG.info("Group data on column: " + colItem.getText());
                }

            });
        }


        MenuItem delGrouping = new MenuItem("Delete grouping");
        delGrouping.setId("delGrouping");
        delGrouping.setOnAction(t -> {
            groupController.clearGrouping();
            LOG.info("Delete grouping on column '" + groupController.getColname_group() + "' with groups "
                    + Arrays.toString(groupController.getGroupnames().toArray()));
        });

        menuGroup.getItems().addAll(groupByColumnItem, new SeparatorMenuItem(), delGrouping);

    }

    public Menu getMenuGroup() {
        return menuGroup;
    }

    public void upateGroupItem(List<String> colnames, GroupController groupController){
        groupByColumnItem.getItems().removeAll(groupByColumnItem.getItems());
        for(String col : colnames ){
            MenuItem colItem = new MenuItem(col);
            groupByColumnItem.getItems().add(colItem);
            colItem.setOnAction(t -> {
                if(!colItem.getText().contains("(Grouping)")){
                    groupController.clearGrouping();
                    groupController.createGroupByColumn(colItem.getText(), "");
                    LOG.info("Create grouping on column: " + colItem.getText());
                }
            });
        }
    }


}