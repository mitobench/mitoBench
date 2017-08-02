package view.dialogues.settings;

import Logging.LogClass;
import analysis.PCA;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import view.MitoBenchWindow;

import java.util.Arrays;

public class PcaPopupDialogue extends AHGDialogue{




    public PcaPopupDialogue(String title, LogClass logClass) {
        super(title, logClass);
    }


    @Override
    public void addListener(){
        okBtn.setOnAction(e -> {
            if((textField.getText().equals("") || textField.getText().startsWith("Please")) &&  !default_list_checkbox.isSelected()){
                textField.setText("Please enter list here.");

            } else {
                String[] hg_list;
                if(default_list_checkbox.isSelected()){
                    hg_list = haploStatistics.getChartController().getCoreHGs();
                } else {
                    hg_list = textField.getText().split(",");
                }
                String[] hg_list_trimmed = Arrays.stream(hg_list).map(String::trim).toArray(String[]::new);
                haploStatistics.count(hg_list_trimmed);

                TableView table = haploStatistics.writeToTable();

                statsTabPane.getTabs().remove(getTab());

                Tab tab = new Tab();
                tab.setId("tab_statistics");
                tab.setText("Count statistics");
                tab.setContent(table);
                statsTabPane.getTabs().add(tab);
                statsTabPane.getSelectionModel().select(tab);

                LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(hg_list_trimmed));


                PCA pca2 = new PCA();

                double[][] result_pca = pca2.calculate(haploStatistics.getFrequencies(), 2);
                pca2.plot(result_pca);

                Tab tab_pca = new Tab("PCA");
                tab_pca.setId("tab_pca_plot");
                tab_pca.setContent(pca2.getPca_plot().getSc());
                mito.getTabpane_visualization().getTabs().add(tab_pca);
            }

        });

        Tooltip tp = new Tooltip("Default list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        default_list_checkbox.setOnMouseEntered(event -> {
            Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());
    }


}
