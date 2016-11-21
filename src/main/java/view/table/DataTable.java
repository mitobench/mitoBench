package view.table;

import io.datastructure.Entry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.11.2016.
 */
public class DataTable {

    // key = column name
    // Array = data od this col     --> has to be array to ensure the order of the entries
    HashMap<String, String[]> data;

    public DataTable(){
        data = new HashMap<>();
    }


    /**
     * This method gets a hashmap of new input entries and updates the current table view.
     *
     * @param input
     */
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
                int rpos = getRowPosition(key);
                updateRow(rpos, input.get(key), key);
            }
        }
    }

    /**
     * This method updates a single row.
     *
     * @param rowPosition
     * @param input
     * @param key
     */
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

            } else {  // if column does NOT exist

                int rpos = getRowPosition(key);
                addColumn(entry.getIdentifier(), rpos);
                String[] columnEntries = data.get(columnName);

                if(columnName.equals("MTSequence")){
                    columnEntries[getRowPosition(key)] = (String)entry.getData().toString().substring(0,5)+"...";
                } else {
                    columnEntries[getRowPosition(key)] = (String)entry.getData();
                }

            }
        }
    }

    /**
     * This method returns true, if a certain column 'col' already exists.
     *
     * @param col
     * @return
     */
    private boolean columnExists(String col){
        if(data.get(col)!=null){
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method returns the line number of a certain row.
     *
     * @param rowID
     * @return
     */
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
     * fill all columns with dummy string value.
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
                    String[] d = data.get(key);
                    data.put(key, append(d, "placeholder"));
                }
            }
        } else {
            data.put("ID", new String[]{rowID});
            for(String key : data.keySet()){
                if(!key.equals("ID")){
                    String[] newCol = new String[data.size()];
                    Arrays.fill(newCol, "");
                    data.put(key,  newCol);
                }
            }
        }

    }


    /**
     * This method appends an element to an array.
     *
     * @param arr
     * @param element
     * @param <T>
     * @return
     */
    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N+1);
        arr[N] = element;
        return arr;
    }


    public void addColumn(String key, int size){
        data.put(key, new String[size+1]);
    }

    public HashMap<String, String[]> getDataTable() {
        return data;
    }

    public int getNumberOfColumns(){
        return data.size();
    }
}
