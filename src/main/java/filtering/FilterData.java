package filtering;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view.table.controller.TableControllerUserBench;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 06.03.17.
 */
public class FilterData {
    private final HashMap<String, List<String>> hgs_per_mutation;
    private final TableControllerUserBench tableControllerUB;
    ObservableList<ObservableList> selectedRows;

    public FilterData(TableControllerUserBench tableControllerUserBench, HashMap<String, List<String>> hgs_per_mutation){
        tableControllerUB = tableControllerUserBench;
        selectedRows = tableControllerUserBench.getSelectedRows();
        this.hgs_per_mutation = hgs_per_mutation;

    }

    public void filterMutation(String[] mutations) throws Exception {
        for(String mut : mutations){
            List<String> hgs = hgs_per_mutation.get(mut);

            ObservableList<ObservableList> filtered_data = FXCollections.observableArrayList();

            if(hgs!=null && hgs.size()>0 ){
                for(String hg : hgs){
                    for(ObservableList row : selectedRows){
                        if(row.contains(hg)){
                            filtered_data.add(row);
                        }
                    }
                }

                tableControllerUB.updateView(filtered_data);
            }
//            else {
//                throw new Exception("Muatation "+ mut + " does not exist!");
//            }


        }

    }
}
