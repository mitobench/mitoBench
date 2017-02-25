package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import statistics.MutationStatistics;
import view.dialogues.settings.HGStatisticsPopupDialogue;
import view.table.controller.TableControllerUserBench;
import statistics.HaploStatistics;
import view.tree.HaplotreeController;

import java.io.IOException;

/**
 * Created by neukamm on 16.11.16.
 */
public class StatisticsMenu {

    private final Stage stage;
    private Menu menuTools;
    private TableControllerUserBench tableController;
    private HaplotreeController treeHaploController;
    private HaploStatistics haploStatistics;
    private MutationStatistics mutationStatistics;

    public StatisticsMenu(TableControllerUserBench tableController, HaplotreeController treeHaploController, TabPane statsTabpane,
                          Scene scene, Stage pstage) throws IOException {

        menuTools = new Menu("Statistics");
        menuTools.setId("menu_statistics");
        this.tableController = tableController;
        this.treeHaploController = treeHaploController;
        stage = pstage;
        addSubMenus(statsTabpane, scene);
    }

    private void addSubMenus(TabPane statsTabpane, Scene scene) throws IOException {
        MenuItem haploStats = new MenuItem("Count Haplogroups");
        haploStats.setId("toolsMenu_stats_hg");
        haploStats.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                haploStatistics = new HaploStatistics(tableController, treeHaploController);
                stage.toBack();
                HGStatisticsPopupDialogue hgStatisticsPopupDialogug = new HGStatisticsPopupDialogue("Statistics");
                hgStatisticsPopupDialogug.init(haploStatistics, statsTabpane, scene);
            }
        });

        MenuItem mutations = new MenuItem("Calculate mutation frequency");
        mutations.setId("toolsMenu_mutation_freq");
        mutations.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mutationStatistics = new MutationStatistics();
                mutationStatistics.writeToTable(treeHaploController.getTree().getHgs_per_mutation(), statsTabpane);

            }
        });

        menuTools.getItems().addAll(haploStats, mutations);
    }



    public Menu getMenuTools() {
        return menuTools;
    }

    public HaploStatistics getHaploStatistics() {
        return haploStatistics;
    }
}
