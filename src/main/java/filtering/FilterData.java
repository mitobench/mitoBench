package filtering;

import Logging.LogClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import view.table.controller.TableControllerUserBench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 06.03.17.
 */
public class FilterData {
    private final HashMap<String, List<String>> hgs_per_mutation;
    private final HashMap<String, String[]> mutations_per_hg;
    private final TableControllerUserBench tableControllerUB;
    private final Logger LOG;
    private ObservableList<ObservableList> selectedRows;

    public FilterData(HashMap<String, String[]> mutations_per_hg,
                      TableControllerUserBench tableControllerUserBench,
                      HashMap<String, List<String>> hgs_per_mutation,
                      LogClass logClass){

        tableControllerUB = tableControllerUserBench;
        selectedRows = tableControllerUserBench.getSelectedRows();
        this.hgs_per_mutation = hgs_per_mutation;
        LOG = logClass.getLogger(this.getClass());
        this.mutations_per_hg = mutations_per_hg;

    }

    public void filterHaplotype(String[] haplotypes, String distance) throws Exception {

        LOG.info("Filter haplotypes: data set includes only the haplotype(s): " + Arrays.toString(haplotypes) +
                " and all haplotype with distance d=" + distance);

        // filtered hgs
        List<String> filtered_hgs = new ArrayList<>();
        ObservableList<ObservableList> filtered_data = FXCollections.observableArrayList();

        // first: filter samples from table with this mutation(s)

        //for(ObservableList row : selectedRows){
            //int index_hg_col = tableControllerUB.getColIndex("Haplogroup");
            for(String mut : haplotypes) {
                List<String> hgs = hgs_per_mutation.get(mut);
                if(hgs!=null){
                    filtered_hgs.addAll(hgs);
                }
            }

        // second: get all Haplogroups with 'n' mutations distance from mut(s) above
        for(String mut : haplotypes){
            List<String> hgs = hgs_per_mutation.get(mut);
            if(hgs!=null) {
                for (String hg : hgs) {
                    String[] muts_of_hg = mutations_per_hg.get(hg);
                    if(muts_of_hg!=null) {
                        for (String key_HG : mutations_per_hg.keySet()) {
                            String[] muts_key = mutations_per_hg.get(key_HG);
                            int hamming = calculateHammingDistance(Arrays.asList(muts_of_hg), Arrays.asList(muts_key));
                            if (hamming <= Integer.parseInt(distance)) {
                                filtered_hgs.add(key_HG);
                            }
                        }
                    }
                }
            }
        }

        // update table with all filtered data
        if(filtered_hgs.size()!=0) {
            for (ObservableList row : selectedRows) {
                int index_hg_col = tableControllerUB.getColIndex("Haplogroup");
                for (String hg : filtered_hgs) {
                    if (row.get(index_hg_col).equals(hg)) {
                        filtered_data.add(row);
                        break;
                    }
                }
            }
            tableControllerUB.updateView(filtered_data);
        }
    }

    private int calculateHammingDistance(List<String> muts1, List<String> muts2){

        int hammingDist = 0;
        int longer = Math.max(muts1.size(), muts2.size());
        int shorter = Math.min(muts1.size(), muts2.size());

        hammingDist += longer-shorter;

        for(int i = 0; i < shorter; i++){
            if(!muts2.contains(muts1.get(i)))
                hammingDist++;
        }

        return hammingDist;
    }
}