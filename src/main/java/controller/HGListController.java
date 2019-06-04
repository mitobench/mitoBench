package controller;

import view.MitoBenchWindow;
import view.dialogues.settings.HGListDialogue;

import java.util.Arrays;

public class HGListController {

    private final ChartController chartcontroller;
    private final MitoBenchWindow mito;
    private HGListDialogue hgListDialogue;

    public HGListController(HGListDialogue hgListDialogue, ChartController chartController, MitoBenchWindow mito){
        this.hgListDialogue = hgListDialogue;
        this.chartcontroller = chartController;
        this.mito = mito;
        addListener();
    }

    private void addListener() {
        hgListDialogue.getButton_apply_list().setOnAction(e -> {

            String[] hglist;
            String p1 = hgListDialogue.getComboBox_hgList().getSelectionModel().getSelectedItem().toString();
            if(p1.contains("\\(") && p1.contains("\\)") ){
                String p2 = p1.split("\\(")[1];
                String p3 = p2.split("\\)")[0];
                hglist = p3.split(",");
            } else  {
                hglist = p1.split(",");
            }

            Arrays.stream(hglist).map(String::trim).toArray(unused -> hglist);
            chartcontroller.setCustomHGList(hglist);

            mito.getTabpane_statistics().getTabs().remove(hgListDialogue.getTab());
            hgListDialogue.getLOG().info("Custom HG list set.");
        });
    }


}
