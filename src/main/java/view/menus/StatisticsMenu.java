package view.menus;

import Logging.LogClass;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import statistics.HaplotypeStatistics;
import view.MitoBenchWindow;
import view.dialogues.settings.HGStatisticsPopupDialogue;
import controller.GroupController;
import controller.TableControllerUserBench;
import statistics.HaploStatistics;
import controller.HaplotreeController;

/**
 * Created by neukamm on 16.11.16.
 */
public class StatisticsMenu {

    private final Stage stage;
    private final MitoBenchWindow mito;
    private Menu menuTools;
    private TableControllerUserBench tableController;
    private HaplotreeController treeHaploController;
    private GroupController groupController;
    private HaploStatistics haploStatistics;
    private HaplotypeStatistics mutationStatistics;
    private Logger LOG;
    private LogClass LOGClass;
    private HGStatisticsPopupDialogue hgStatisticsPopupDialogug;

    public StatisticsMenu(MitoBenchWindow mitoBenchWindow) {
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        LOGClass =mitoBenchWindow.getLogClass();
        mito = mitoBenchWindow;

        menuTools = new Menu("Statistics");
        menuTools.setId("menu_statistics");
        tableController = mitoBenchWindow.getTableControllerUserBench();
        treeHaploController = mitoBenchWindow.getTreeController();
        groupController = mitoBenchWindow.getGroupController();
        stage = mitoBenchWindow.getPrimaryStage();
        addSubMenus(mitoBenchWindow.getTabpane_statistics());
    }

    private void addSubMenus(TabPane statsTabpane){
        MenuItem haploStats = new MenuItem("Count Haplogroups");
        haploStats.setId("toolsMenu_stats_hg");
        haploStats.setOnAction(t -> {
            haploStatistics = new HaploStatistics(tableController, mito.getChartController(), LOGClass);

            hgStatisticsPopupDialogug = new HGStatisticsPopupDialogue("Statistics", LOGClass);
            hgStatisticsPopupDialogug.init(mito);

            Tab tab = hgStatisticsPopupDialogug.getTab();
            mito.getTabpane_statistics().getTabs().add(tab);
            mito.getTabpane_statistics().getSelectionModel().select(tab);
            mito.getProgressBarhandler().stop();



        });

        MenuItem mutations = new MenuItem("Calculate haplotype frequency");
        mutations.setId("toolsMenu_mutation_freq");
        mutations.setOnAction(t -> {
            LOG.info("Calculate haplotype frequency");
            mutationStatistics = new HaplotypeStatistics(LOGClass, mito.getPrimaryStage());
            Task task = createTaskHaploFreqs();
            mito.getProgressBarhandler().activate(task.progressProperty());

            task.setOnSucceeded((EventHandler<Event>) event -> {
                mutationStatistics.writeToTable(statsTabpane);
                mito.getProgressBarhandler().stop();
            });
            new Thread(task).start();

        });

       // menuTools.getItems().addAll(haploStats, mutations);
        menuTools.getItems().addAll(haploStats);
    }



    public Menu getMenuTools() {
        return menuTools;
    }

    public Task createTaskHaploFreqs(){
        return new Task() {
            @Override
            protected Object call()  {

                mutationStatistics.calculateHaplotypeFrequencies(treeHaploController.getTree().getMutations_per_hg(),
                        tableController.getTableColumnByName("Haplogroup"), tableController.getTable(),
                        treeHaploController);
                return true;
            }
        };

    }

}