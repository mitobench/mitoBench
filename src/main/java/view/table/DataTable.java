package view.table;

import io.datastructure.Entry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.11.2016.
 */
public class DataTable {

    // key = colname
    // Array = data od this col     --> has to be array to ensure the order of the entries
    HashMap<String, String[]> data;

    public DataTable(){
        data = new HashMap<>();
    }


    public void update(HashMap<String, List<Entry>> input){

        for(String key : input.keySet()){
            int rowPosition = getRowPosition(key);

            if(rowPosition >= 0){
                // row already exists
                // update row
                updateRow(rowPosition, input.get(key), key);
            } else {
                // create new Row with dummy entries
                addRow(key);
                updateRow(getRowPosition(key), input.get(key), key);
            }

        }


    }

    private void updateRow(int rowPosition, List<Entry> input, String key){

        for(Entry entry : input){

            String columnName = entry.getIdentifier();
            // if column exists:
            if(columnExists(columnName)){
                String[] columnEntries = data.get(columnName);
                if(columnName.equals("MTSequence")){
                    columnEntries[rowPosition] = (String)entry.getData().toString().substring(0,5)+"...";
                } else {
                    columnEntries[rowPosition] = (String)entry.getData();
                }

            } else {
                addColumn(entry.getIdentifier(), new String[data.size()]);
                String[] columnEntries = data.get(columnName);

                if(columnName.equals("MTSequence")){
                    columnEntries[getRowPosition(key)] = (String)entry.getData().toString().substring(0,5)+"...";
                } else {
                    columnEntries[getRowPosition(key)] = (String)entry.getData();
                }

            }
        }
    }

    private boolean columnExists(String col){
        if(data.get(col)!=null){
            return true;
        } else {
            return false;
        }

    }

    private int getRowPosition(String rowID){

        if(data.get("ID")!=null){
            String[] id_col = data.get("ID");
            for(int i = 0; i < id_col.length; i++){
                if(id_col[i].equals(rowID)){
                    return i;
                }
            }
        }

        return -1;
    }


    /**
     * add new row identifier to data table
     * fill all columns with dummy string value
     *
     * @param rowID
     */
    private void addRow(String rowID){
        // add ID
        String[] id_col = data.get("ID");
        if (id_col != null) {
            data.put("ID", append(id_col, rowID));

            for(String key : data.keySet()){
                if(!key.equals("ID")){
                    data.put(key, append(data.get(key), ""));
                }
            }
        } else {
            data.put("ID", new String[]{rowID});
            for(String key : data.keySet()){
                if(!key.equals("ID")){
                    data.put(key, new String[data.size()]);
                }
            }
        }

    }


    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }


    public void addColumn(String key , String[] data_col){
        data.put(key, data_col);
    }

    public HashMap<String, String[]> getDataTable() {
        return data;
    }

    public int getNumberOfColumns(){
        return data.size();
    }
}
