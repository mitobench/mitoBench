package controller;

import view.MitoBenchWindow;
import view.dialogues.settings.HGListDisalogue;

import java.util.Arrays;

public class HGListController {

    private final ChartController chartcontroller;
    private final MitoBenchWindow mito;
    private HGListDisalogue hgListDisalogue;

    public HGListController(HGListDisalogue hgListDisalogue, ChartController chartController, MitoBenchWindow mito){
        this.hgListDisalogue = hgListDisalogue;
        this.chartcontroller = chartController;
        this.mito = mito;
        addListener();
    }

    private void addListener() {
        hgListDisalogue.getButton_apply_list().setOnAction(e -> {
            //hgListDisalogue.getTextField_hgList().clear();

            String[] hglist = hgListDisalogue.getTextField_hgList().getText().split(",");
            Arrays.stream(hglist).map(String::trim).toArray(unused -> hglist);
            chartcontroller.setCustomHGList(hglist);

            mito.getTabpane_statistics().getTabs().remove(hgListDisalogue.getTab());
            hgListDisalogue.getLOG().info("Custom HG list set.");
        });
    }


}
