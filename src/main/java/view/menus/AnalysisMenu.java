package view.menus;

import Logging.LogClass;
import analysis.FstCalculationRunner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import statistics.HaploStatistics;
import statistics.HaplotypeCaller;
import view.MitoBenchWindow;
import view.dialogues.information.GroupingWarningDialogue;
import view.dialogues.settings.FstSettingsDialogue;
import view.dialogues.settings.HGStatisticsPopupDialogue;
import view.table.controller.TableControllerUserBench;

import java.io.IOException;


/**
 * Created by neukamm on 20.04.17.
 */
public class AnalysisMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private final TableControllerUserBench tableController;
    private Menu menuAnalysis;

    public AnalysisMenu(MitoBenchWindow mitoBenchWindow){
        menuAnalysis = new Menu("Analysis");
        menuAnalysis.setId("menu_analysis");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        addSubMenus();

    }

    private void addSubMenus() {



        MenuItem pairwiseFst = new MenuItem("Calculate pairwise Fst");
        pairwiseFst.setId("menuitem_pairwiseFst");
        pairwiseFst.setOnAction(t -> {

            if(tableController.getGroupController().isGroupingExists()) {
                FstSettingsDialogue fstSettingsDialogue =
                            new FstSettingsDialogue("Fst Calculation Settings", logClass);
                fstSettingsDialogue.init(mito);

            }
            else {
                GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                        "No groups defined",
                        "Please define a grouping first.",
                        null,
                        "groupWarning");
            }
        });


        MenuItem assignHGs = new MenuItem("Calculate haplogroups");
        assignHGs.setId("menuitem_calculate_haplogroups");
        assignHGs.setOnAction(t -> {

//                try {
//                    HaplotypeCaller haplotypeCaller = new HaplotypeCaller(mito.getTableControllerUserBench(),
//                            mito.getTableControllerUserBench().getDataTable().getMtStorage());
//                    haplotypeCaller.call();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

        });
        menuAnalysis.getItems().addAll(pairwiseFst, assignHGs);
        //menuAnalysis.getItems().add(pairwiseFst);

    }

    public Menu getMenuAnalysis() {
        return menuAnalysis;
    }
}
