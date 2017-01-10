package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import view.dialogues.popup.HGStatisticsPopupDialogue;
import view.table.TableController;
import statistics.HaploStatistics;
import view.tree.TreeHaploController;

import java.io.IOException;

/**
 * Created by neukamm on 16.11.16.
 */
public class ToolsMenu {

    private Menu menuTools;
    private TableController tableController;
    private TreeHaploController treeHaploController;

    public ToolsMenu(TableController tableController, TreeHaploController treeHaploController, TabPane statsTabpane,
                     Scene scene) throws IOException {

        menuTools = new Menu("Statistics");
        this.tableController = tableController;
        this.treeHaploController = treeHaploController;
        addSubMenus(statsTabpane, scene);
    }

    private void addSubMenus(TabPane statsTabpane, Scene scene) throws IOException {
        MenuItem haploStats = new MenuItem("Count Haplogroups");
        haploStats.setId("toolsMenu_stats_hg");

        haploStats.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                HaploStatistics haploStatistics = new HaploStatistics(tableController, treeHaploController);


                    // open
                    HGStatisticsPopupDialogue hgStatisticsPopupDialogug = new HGStatisticsPopupDialogue(haploStatistics, statsTabpane, scene);
                    hgStatisticsPopupDialogug.show();
                    //haploStatistics.count(new String[]{"H","HV","I","J","K","L0","L1","L2","L3","L4","M1","N","N1a","N1b","R","R0","T","T1","T2","U","W","X"});
                    //haploStatistics.printStatistics();

            }
        });

        menuTools.getItems().add(haploStats);
    }



        public Menu getMenuTools() {
        return menuTools;
    }
}
