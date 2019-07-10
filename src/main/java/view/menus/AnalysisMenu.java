package view.menus;

import Logging.LogClass;
import controller.FstCalculationController;
import analysis.HaplotypeCaller;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import view.MitoBenchWindow;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.FstSettingsDialogue;
import controller.TableControllerUserBench;
import view.dialogues.settings.PcaPopupDialogue;


/**
 * Created by neukamm on 20.04.17.
 */
public class AnalysisMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private final TableControllerUserBench tableController;
    private final StatisticsMenu statisticsMenu;
    private Menu menuAnalysis;
    private int pcaID=1;

    public AnalysisMenu(MitoBenchWindow mitoBenchWindow, StatisticsMenu statisticsMenu){
        menuAnalysis = new Menu("Analysis");
        menuAnalysis.setId("menu_analysis");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        this.statisticsMenu = statisticsMenu;
        addSubMenus();

    }

    private void addSubMenus() {

        MenuItem pairwiseFst = new MenuItem("Calculate pairwise Fst");
        pairwiseFst.setId("menuitem_pairwiseFst");
        pairwiseFst.setOnAction(t -> {

            if(tableController.getGroupController().groupingExists()) {
                FstSettingsDialogue fstSettingsDialogue =
                            new FstSettingsDialogue("Fst Calculation Settings", logClass, mito);
                FstCalculationController fstCalculationController = new FstCalculationController(fstSettingsDialogue, mito);
                mito.getTabpane_statistics().getTabs().add(fstSettingsDialogue.getTab());
                mito.getTabpane_statistics().getSelectionModel().select(fstSettingsDialogue.getTab());

            }
            else {
                InformationDialogue groupingWarningDialogue = new InformationDialogue(
                        "No groups defined",
                        "Please define a grouping first.",
                        null,
                        "groupWarning");
            }
        });




        MenuItem assignHGs = new MenuItem("Calculate haplogroups");
        assignHGs.setId("menuitem_calculate_haplogroups");
        assignHGs.setOnAction(t -> {
            HaplotypeCaller haplotypeCaller = new HaplotypeCaller(tableController, logClass);

            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    haplotypeCaller.call("");
                    return true;
                }
            };
            mito.getProgressBarhandler().activate(task.progressProperty());
            task.setOnSucceeded((EventHandler<Event>) event -> {
                haplotypeCaller.update();
                haplotypeCaller.deleteTmpFiles();
                mito.getProgressBarhandler().stop();
            });

            new Thread(task).start();

        });

        MenuItem pcaAnalysis = new MenuItem("PCA analysis");
        pcaAnalysis.setId("menuitem_pairwiseFst");
        pcaAnalysis.setOnAction(t -> {
            // PCA needs haplotype statistics!!

            PcaPopupDialogue pcaPopupDialogue = new PcaPopupDialogue("PCA configuration", logClass, pcaID);
            pcaPopupDialogue.init(mito);
            Tab tab_stats = pcaPopupDialogue.getTab();
            mito.getTabpane_statistics().getTabs().add(tab_stats);
            mito.getTabpane_statistics().getSelectionModel().select(tab_stats);
            pcaID++;

        });


        menuAnalysis.getItems().addAll(pairwiseFst, assignHGs, pcaAnalysis);
    }

    public Menu getMenuAnalysis() {
        return menuAnalysis;
    }
}
