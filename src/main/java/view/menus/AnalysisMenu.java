package view.menus;

import Logging.LogClass;
import analysis.FstCalculationController;
import analysis.HaplotypeSharing;
import analysis.PCA;
import controller.GroupController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import view.MitoBenchWindow;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.FstSettingsDialogue;
import controller.TableControllerUserBench;
import view.dialogues.settings.HGStatisticsPopupDialogue;
import view.dialogues.settings.PcaPopupDialogue;
import view.visualizations.HaplotypeSharingVis;
import view.visualizations.HeatMap;

import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 20.04.17.
 */
public class AnalysisMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private final TableControllerUserBench tableController;
    private final StatisticsMenu statisticsMenu;
    private final GroupController groupcontroller;
    private Menu menuAnalysis;

    public AnalysisMenu(MitoBenchWindow mitoBenchWindow, StatisticsMenu statisticsMenu){
        menuAnalysis = new Menu("Analysis");
        menuAnalysis.setId("menu_analysis");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        this.statisticsMenu = statisticsMenu;
        this.groupcontroller = mitoBenchWindow.getGroupController();
        addSubMenus();

    }

    private void addSubMenus() {

        MenuItem pairwiseFst = new MenuItem("Calculate pairwise Fst");
        pairwiseFst.setId("menuitem_pairwiseFst");
        pairwiseFst.setOnAction(t -> {

            if(tableController.getGroupController().isGroupingExists()) {
                FstSettingsDialogue fstSettingsDialogue =
                            new FstSettingsDialogue("Fst Calculation Settings", logClass, mito);
                FstCalculationController fstCalculationController = new FstCalculationController(fstSettingsDialogue);
                mito.getTabpane_statistics().getTabs().add(fstSettingsDialogue.getTab());

            }
            else {
                InformationDialogue groupingWarningDialogue = new InformationDialogue(
                        "No groups defined",
                        "Please define a grouping first.",
                        null,
                        "groupWarning");
            }
        });


        MenuItem pcaAnalysis = new MenuItem("PCA analysis");
        pcaAnalysis.setId("menuitem_pairwiseFst");
        pcaAnalysis.setOnAction(t -> {
            // PCA needs haplotype statistics!!

            PcaPopupDialogue pcaPopupDialogue = new PcaPopupDialogue("PCA configuration", logClass);
            pcaPopupDialogue.init(mito);
            Tab tab_stats = pcaPopupDialogue.getTab();
            mito.getTabpane_statistics().getTabs().add(tab_stats);

        });




        MenuItem item_haplotypeSharing = new MenuItem("Haplotype sharing");
        item_haplotypeSharing.setId("menuitem_haplotypeSharing");
        item_haplotypeSharing.setOnAction(t -> {

            HaplotypeSharing haplotypeSharing = new HaplotypeSharing(
                    mito.getTableControllerUserBench());

            HashMap<String, List<String>> res_haplotype_sharing = haplotypeSharing.generateData();

            HaplotypeSharingVis haplotypeSharingVis = new HaplotypeSharingVis(mito);
            haplotypeSharingVis.generateHeatmap(res_haplotype_sharing);
            haplotypeSharingVis.generateInfo();

            Tab tab = new Tab("Haplotype Sharing");
            tab.setId("tab_haplotypesharing");
            tab.setContent(haplotypeSharingVis.getBack());

            mito.getTabpane_visualization().getTabs().add(tab);



        });


        MenuItem assignHGs = new MenuItem("Calculate haplogroups");
        assignHGs.setId("menuitem_calculate_haplogroups");
        assignHGs.setOnAction(t -> {
            InformationDialogue HGNotSupportedDialogue = new InformationDialogue("",
                    "Please use HaploGrep2 to determine Haplogroups.\n" +
                            "The resulting hsd file can then be uploaded.", "Haplogroup calculation is not supported yet",
                    "hgCalculationDislogue");
//                try {
//                    HaplotypeCaller haplotypeCaller = new HaplotypeCaller(mito.getTableControllerUserBench(),
//                            mito.getTableControllerUserBench().getDataTable().getMtStorage());
//                    haplotypeCaller.call();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

        });
        menuAnalysis.getItems().addAll(pairwiseFst, pcaAnalysis, item_haplotypeSharing, assignHGs);
        //menuAnalysis.getItems().add(pairwiseFst);

    }

    public Menu getMenuAnalysis() {
        return menuAnalysis;
    }
}
