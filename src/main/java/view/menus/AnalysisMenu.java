package view.menus;

import Logging.LogClass;
import analysis.FstCalculationRunner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import statistics.HaplotypeCaller;
import view.MitoBenchWindow;

import java.io.IOException;


/**
 * Created by neukamm on 20.04.17.
 */
public class AnalysisMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private Menu menuAnalysis;


    public AnalysisMenu(MitoBenchWindow mitoBenchWindow){
        menuAnalysis = new Menu("Analysis");
        menuAnalysis.setId("menu_analysis");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        addSubMenus();

    }

    private void addSubMenus() {



        MenuItem pairwiseFst = new MenuItem("calculate pairwise Fst");
        pairwiseFst.setId("menuitem_pairwiseFst");
        pairwiseFst.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                try {
                    FstCalculationRunner fstCalculationPreparer = new FstCalculationRunner(mito);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        MenuItem assignHGs = new MenuItem("Calculate haplogroups");
        assignHGs.setId("menuitem_calculate_haplogroups");
        assignHGs.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

//                try {
//                    HaplotypeCaller haplotypeCaller = new HaplotypeCaller(mito.getTableControllerUserBench(),
//                            mito.getTableControllerUserBench().getDataTable().getMtStorage());
//                    haplotypeCaller.call();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        });
        //menuAnalysis.getItems().addAll(pairwiseFst, assignHGs);
        menuAnalysis.getItems().add(pairwiseFst);

    }

    public Menu getMenuAnalysis() {
        return menuAnalysis;
    }
}
