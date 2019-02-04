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
            //hgListDialogue.getTextField_hgList().clear();

            String[] hglist = hgListDialogue.getTextField_hgList().getText().split(",");
            Arrays.stream(hglist).map(String::trim).toArray(unused -> hglist);
            chartcontroller.setCustomHGList(hglist);

            mito.getTabpane_statistics().getTabs().remove(hgListDialogue.getTab());
            hgListDialogue.getLOG().info("Custom HG list set.");
        });
    }


}
