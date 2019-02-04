package analysis;

import controller.TableControllerUserBench;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.*;

public class HaplotypeSharing {

    private final TableControllerUserBench tableConteroller;


    public HaplotypeSharing(TableControllerUserBench tableControllerUserBench){
        this.tableConteroller = tableControllerUserBench;

    }


    public HashMap<String, List<String>> generateData() {

        String[][] cols = prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableConteroller.getTable().getItems());
        String[] selection_haplogroups = cols[0];
        String[] selection_groups = cols[1];

        HashMap<String, HashMap<String, Integer>> group_to_haplotype_to_count_group = counthaplotypesPerGroup(selection_groups, selection_haplogroups);

        Set<String> groups = group_to_haplotype_to_count_group.keySet();
        String[] groups_array = groups.toArray(new String[groups.size()]);

        HashMap<String, List<String>> result_comparison = new HashMap<>();

        for(int i = 0; i < groups_array.length; i++){
            for(int j = i+1; j < groups_array.length; j++){
                List<String> res_tmp = compareGroups(group_to_haplotype_to_count_group, groups_array[i], groups_array[j]);
                result_comparison.put(groups_array[i]+"_"+groups_array[j], res_tmp);
            }
        }

        return result_comparison;

    }

    private List<String> compareGroups(HashMap<String, HashMap<String, Integer>> group_to_haplotype_to_count_group, String group1, String group2) {

        HashMap<String, Integer> data_group1 = group_to_haplotype_to_count_group.get(group1);
        HashMap<String, Integer> data_group2 = group_to_haplotype_to_count_group.get(group2);

        List<String> result = new ArrayList<>();

        for( String haplotype : data_group1.keySet() ){
            if(data_group2.containsKey(haplotype)){
                result.add(haplotype);
            }

        }

        return result;

    }

    private HashMap<String, HashMap<String, Integer>> counthaplotypesPerGroup(String[] selection_groups, String[] selection_haplogroups) {

        // group -> (haplotype -> count)
        HashMap<String, HashMap<String, Integer>> group_to_haplotype_to_count_group = new HashMap<>();

        for(int i = 0; i < selection_groups.length; i++) {
            String group = selection_groups[i];

            HashMap<String, Integer> haplotype_to_count_group;
            if(group_to_haplotype_to_count_group.containsKey(group)) {
                haplotype_to_count_group = group_to_haplotype_to_count_group.get(group);
            } else {
                haplotype_to_count_group = new HashMap<>();
                group_to_haplotype_to_count_group.put(group, haplotype_to_count_group);
            }

            String haplotype = selection_haplogroups[i];

            if(haplotype_to_count_group.containsKey(haplotype)){
                int count_tmp = haplotype_to_count_group.get(haplotype);
                count_tmp++;
                haplotype_to_count_group.put(haplotype, count_tmp);
            } else {
                haplotype_to_count_group.put(haplotype, 1);
            }
        }

        return group_to_haplotype_to_count_group;

    }


    /**
     * This method gets all entries of column "Haplogroups" and "Grouping" as set of unique entries.
     *
     * @param names
     * @param selectedTableItems
     * @return
     */
    public String[][] prepareColumns(String[] names, ObservableList<ObservableList> selectedTableItems){


        String[][] res = new String[names.length][];
        for(int i = 0; i < names.length; i++){
            TableColumn col = tableConteroller.getTableColumnByName(names[i]);

            List columnData = new ArrayList();
            selectedTableItems.stream().forEach((o)
                    -> columnData.add(col.getCellData(o)));

            res[i] = (String[]) columnData.toArray(new String[columnData.size()]);
        }

        return res;

//        TableColumn haplo_col = tableController.getTableColumnByName(names[0]);
//        TableColumn grouping_col = tableController.getTableColumnByName(names[1]);
//
//        Set<String> columnDataHG = new HashSet<>();
//        selectedTableItems.stream().forEach((o)
//                -> columnDataHG.add((String)haplo_col.getCellData(o)));
//
//        Set<String> columnDataGroup = new HashSet<>();
//        selectedTableItems.stream().forEach((o)
//                -> columnDataGroup.add((String)grouping_col.getCellData(o)));
//
//        return new String[][]{columnDataHG.toArray(new String[columnDataHG.size()]),
//                columnDataGroup.toArray(new String[columnDataGroup.size()])};
    }
}
