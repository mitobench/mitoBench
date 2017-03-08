package statistics;

import Logging.LogClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import view.table.controller.TableControllerMutations;
import view.tree.HaplotreeController;

import java.io.File;
import java.util.*;

/**
 * Created by neukamm on 24.02.17.
 */
public class MutationStatistics {


    private final TableControllerMutations tableController;
    private Logger LOG;

    HashMap<String, List<String>> hgs_per_mutation_of_current_data;

    public MutationStatistics(LogClass LOGClass) {
            tableController = new TableControllerMutations(LOGClass);
            LOG = LOGClass.getLogger(this.getClass());
            tableController.init();
    }

    public void calculateMutationFrequencies(HashMap<String, String[]> mutations_per_hg,
                                             TableColumn haplogroupCol,
                                             TableView table, HaplotreeController treeHaploController){

        hgs_per_mutation_of_current_data = new HashMap<>();

        List<String> hgs = new ArrayList<>();
        table.getItems().stream().forEach((o)
                -> hgs.add((String)haplogroupCol.getCellData(o)));

        for (String hg : hgs){
            if(!hg.contains("+")){
                List<String> hgs_pathToRoot = treeHaploController.getPathToRoot(treeHaploController.getTreeItem(hg));
                List<String> mutations_of_hg = new ArrayList<>();

                for(String hg_in_path : hgs_pathToRoot){
                    String[] muts_on_path = mutations_per_hg.get(hg_in_path.trim());
                    if(muts_on_path!=null)
                        mutations_of_hg.addAll(Arrays.asList(muts_on_path));
                }

                String[] mutations = mutations_per_hg.get(hg);
                mutations_of_hg.addAll(Arrays.asList(mutations));

                if(mutations!=null){
                    for(String mutation : mutations_of_hg){

                        List<String> list = new ArrayList<>();
                        list.add(hg);

                        if(hgs_per_mutation_of_current_data.containsKey(mutation)){
                            list.addAll(hgs_per_mutation_of_current_data.get(mutation));
                        }

                        hgs_per_mutation_of_current_data.put(mutation, list);

                    }
                }
            }
        }
    }


    public void writeToTable(TabPane tabPane){
        Tab tab = new Tab();
        tab.setId("tab_stats_mutation_freq");
        tab.setText("Mutation frequency");

        List<String> keys = new ArrayList<>();
        keys.addAll(hgs_per_mutation_of_current_data.keySet());
        Collections.sort(keys);
        TableView<ObservableList> table = tableController.getTable();

        File f = new File("src/main/java/statistics/tableStyle.css");
        table.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        // add columns
        tableController.addColumn("Mutation", 0);
        tableController.addColumn("Total Number", 1);
        tableController.addColumn("Frequency", 2);
        tableController.addColumn("Haplogroup", 3);

        TableColumn colHG = (TableColumn)tableController.getTable().getColumns().get(3);
        colHG.getStyleClass().add("align-header-left");

        // add data (table content)
        // write mutation information to table ONLY if mutation exists in dataset
        ObservableList<ObservableList> rows = FXCollections.observableArrayList();
        for(String mut : hgs_per_mutation_of_current_data.keySet()){
            ObservableList  row = FXCollections.observableArrayList();
            double freq = (double)hgs_per_mutation_of_current_data.get(mut).size() / (double) hgs_per_mutation_of_current_data.size();
            row.addAll(mut,
                       hgs_per_mutation_of_current_data.get(mut).size(),
                       freq,
                       new HashSet<String>(hgs_per_mutation_of_current_data.get(mut)));
            rows.add(row);
        }

        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(rows);

        tab.setContent(table);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

}
