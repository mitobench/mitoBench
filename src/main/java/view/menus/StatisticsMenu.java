package view.menus;

import Logging.LogClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import statistics.MutationStatistics;
import view.MitoBenchWindow;
import view.dialogues.information.GroupingWarningDialogue;
import view.dialogues.settings.HGStatisticsPopupDialogue;
import controller.GroupController;
import view.table.controller.TableControllerUserBench;
import statistics.HaploStatistics;
import controller.HaplotreeController;

import java.io.IOException;

/**
 * Created by neukamm on 16.11.16.
 */
public class StatisticsMenu {

    private final Stage stage;
    private Menu menuTools;
    private TableControllerUserBench tableController;
    private HaplotreeController treeHaploController;
    private GroupController groupController;
    private HaploStatistics haploStatistics;
    private MutationStatistics mutationStatistics;
    private Logger LOG;
    private LogClass LOGClass;

    public StatisticsMenu(MitoBenchWindow mitoBenchWindow) throws IOException {
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        LOGClass =mitoBenchWindow.getLogClass();

        menuTools = new Menu("Statistics");
        menuTools.setId("menu_statistics");
        tableController = mitoBenchWindow.getTableControllerUserBench();
        treeHaploController = mitoBenchWindow.getTreeController();
        groupController = mitoBenchWindow.getGroupController();
        stage = mitoBenchWindow.getPrimaryStage();
        addSubMenus(mitoBenchWindow.getTabpane_statistics(), mitoBenchWindow.getScene());
    }

    private void addSubMenus(TabPane statsTabpane, Scene scene) throws IOException {
        MenuItem haploStats = new MenuItem("Count Haplogroups");
        haploStats.setId("toolsMenu_stats_hg");
        haploStats.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(tableController.getGroupController().isGroupingExists()) {
                    haploStatistics = new HaploStatistics(tableController, treeHaploController, LOGClass);
                    HGStatisticsPopupDialogue hgStatisticsPopupDialogug = new HGStatisticsPopupDialogue("Statistics", LOGClass);
                    hgStatisticsPopupDialogug.init(haploStatistics, statsTabpane, scene);
                } else {
                    GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                            "No groups defined",
                            "Please define a grouping first.",
                            null,
                            "groupWarning");

                }
            }
        });

        MenuItem mutations = new MenuItem("Calculate mutation frequency");
        mutations.setId("toolsMenu_mutation_freq");
        mutations.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.info("Calculate frequency per mutation");

                mutationStatistics = new MutationStatistics(LOGClass);

                mutationStatistics.calculateMutationFrequencies(treeHaploController.getTree().getMutations_per_hg(),
                        tableController.getTableColumnByName("Haplogroup"), tableController.getTable(),
                        treeHaploController);

                mutationStatistics.writeToTable(statsTabpane);

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