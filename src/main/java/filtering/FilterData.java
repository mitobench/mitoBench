package filtering;

import Logging.LogClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import statistics.MutationStatistics;
import view.table.controller.TableControllerUserBench;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 06.03.17.
 */
public class FilterData {
    private final HashMap<String, List<String>> hgs_per_mutation;
    private final TableControllerUserBench tableControllerUB;
    private final Logger LOG;
    private ObservableList<ObservableList> selectedRows;

    public FilterData(TableControllerUserBench tableControllerUserBench, HashMap<String, List<String>> hgs_per_mutation,
                      LogClass logClass){

        tableControllerUB = tableControllerUserBench;
        selectedRows = tableControllerUserBench.getSelectedRows();
        this.hgs_per_mutation = hgs_per_mutation;
        LOG = logClass.getLogger(this.getClass());

    }

    public void filterMutation(String[] mutations, String distance) throws Exception {

        LOG.info("Filtering mutations: include only the mutation: " + Arrays.toString(mutations) +
                " and all mutations with distance d=" + distance);

        // second: filter samples from table with this mutation(s)

        ObservableList<ObservableList> filtered_data = FXCollections.observableArrayList();
        for(ObservableList row : selectedRows){
            int index_hg_col = tableControllerUB.getColIndex("Haplogroup");
            for(String mut : mutations){
                List<String> hgs = hgs_per_mutation.get(mut);
                for(String hg : hgs){
                    //String hg_without_plus = row.get(index_hg_col).toString().split("\\+")[0];
                    if(row.get(index_hg_col).equals(hg)){
                        filtered_data.add(row);
                        break;
                    }
                }
            }
        }
        tableControllerUB.updateView(filtered_data);


    }
}
