package view.visualizations;

import controller.GroupController;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import view.MitoBenchWindow;

import java.util.HashMap;
import java.util.List;

public class HaplotypeSharingVis {

    private final BorderPane back;
    private final MitoBenchWindow mito;
    private final GroupController groupcontroller;
    private String[] labels;
    private double[][] data;
    private HashMap<String, List<String>> haplotype_sharing
            ;

    public HaplotypeSharingVis(MitoBenchWindow mito){
        this.back = new BorderPane();
        this.mito = mito;
        this.groupcontroller = mito.getGroupController();
    }


    public void generateHeatmap(HashMap<String, List<String>> res_haplotype_sharing){
        haplotype_sharing = res_haplotype_sharing;

        HashMap<String, Integer> group_index = new HashMap<>();
        labels = new String[groupcontroller.getNumberOfGroups()];
        data = new double[labels.length][labels.length];
        int i = 0;

        for(String s : haplotype_sharing.keySet()){

            if(!group_index.containsKey(s.split("_")[0])){
                group_index.put(s.split("_")[0], i);
                i++;
            }
            if(!group_index.containsKey(s.split("_")[1])){
                group_index.put(s.split("_")[1], i);
                i++;
            }

        }

        for(String s : haplotype_sharing.keySet()){
            String g1 = s.split("_")[0];
            String g2 = s.split("_")[1];

            List<String> tmp = haplotype_sharing.get(s);

            int index1 = group_index.get(g1);
            int index2 = group_index.get(g2);
            if(index1 < index2)
                data[index1][index2] = tmp.size();
            else
                data[index2][index1] = tmp.size();

        }

        for(String k : group_index.keySet()){
            labels[group_index.get(k)] = k;
        }

        HeatMap heatMap_haplotypeSharing = new HeatMap("","", mito.getLogClass());
        heatMap_haplotypeSharing.setContextMenu(mito.getTabpane_visualization());
        heatMap_haplotypeSharing.createHeatMap(data, labels, "Shared Haplotypes", haplotype_sharing);
        back.setCenter(heatMap_haplotypeSharing.heatMap);



    }

    public void generateInfo(){

        TitledPane[] tps = new TitledPane[ haplotype_sharing.keySet().size()];
        Accordion accordion = new Accordion();

        int i = 0;
        for(String s : haplotype_sharing.keySet()){
            List<String> tmp = haplotype_sharing.get(s);
            java.util.Collections.sort(tmp);
            Label info = new Label((tmp).toString());
            tps[i] = new TitledPane(s + "(" + tmp.size() + ")", info);
            i++;
        }

        accordion.getPanes().addAll(tps);

        //back.setRight(accordion);

    }

    public BorderPane getBack() {
        return back;
    }
}
