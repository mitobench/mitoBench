package view.menus;

import Logging.LogClass;
import analysis.FstCalculationController;
import analysis.PCA;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import view.MitoBenchWindow;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.FstSettingsDialogue;
import controller.TableControllerUserBench;


/**
 * Created by neukamm on 20.04.17.
 */
public class AnalysisMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private final TableControllerUserBench tableController;
    private final StatisticsMenu statisticsMenu;
    private Menu menuAnalysis;

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
            PCA pca2 = new PCA();
            pca2.setData(statisticsMenu.getHaploStatistics().getData());
            pca2.calculate();
            pca2.plot();

            Tab tab = new Tab();
            tab.setId("tab_pca_plot");
            tab.setContent(pca2.getPca_plot().getSc());
            mito.getTabpane_visualization().getTabs().add(tab);
//            PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();
//            pca.setup(tableController.getGroupController().getNumberOfGroups(), 20);



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
        menuAnalysis.getItems().addAll(pairwiseFst, pcaAnalysis, assignHGs);
        //menuAnalysis.getItems().add(pairwiseFst);

    }

    public Menu getMenuAnalysis() {
        return menuAnalysis;
    }
}
