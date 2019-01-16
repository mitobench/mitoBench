package view.table;

import io.datastructure.Entry;
import javafx.scene.control.TableColumn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 20.11.2016.
 */
public class DataTable {

    // key = column name
    // Array = view.data od this col     --> has to be array to ensure the order of the entries
    HashMap<String, String[]> data;
    MTStorage mtStorage;


    public DataTable(){

        data = new HashMap<>();
        mtStorage = new MTStorage();
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
            columnName = columnName.replace("\t","");

            // if column exists:
            if(columnExists(columnName)){
                String[] columnEntries = data.get(columnName);

                if(columnName.equals("MTSequence")){
                    setSequenceMap(entry, key, columnEntries);

                } else {
                    columnEntries[rowPosition] = entry.getData().getTableInformation();
                }

            } else {

                if(rowPosition <= data.get("ID").length){ // extend length of column
                    addColumn(entry.getIdentifier(), data.get("ID").length-1);
                } else {
                    addColumn(entry.getIdentifier(), getRowPosition(key));
                }

                String[] columnEntries = data.get(columnName);

                if(columnName.equals("MTSequence")){
                    setSequenceMap(entry, key, columnEntries);
                } else {
                    columnEntries[getRowPosition(key)] = entry.getData().getTableInformation();
                }

            }
        }
    }

    private void setSequenceMap(Entry entry, String key, String[] columnEntries){
        String mtSeq = entry.getData().getTableInformation();
        String mtseq_short;
        if(mtSeq.length() > 5 ){
            mtseq_short = mtSeq.substring(0,5)+"...";
        } else {
            mtseq_short = mtSeq;
        }

        // store original mt seq
        columnEntries[getRowPosition(key)] = mtSeq;
        //mtStorage.setMTStorage(this);
        mtStorage.addEntry(key, mtSeq);
        columnEntries[getRowPosition(key)] = mtseq_short;
    }

    /**
     * This method returns true, if a certain column 'col' already exists.
     *
     * @param col
     * @return
     */
    public boolean columnExists(String col){
        return data.get(col) != null;
    }

    /**
     * This method returns the line number of a certain row.
     *
     * @param rowID
     * @return
     */
    private int getRowPosition(String rowID){
        if(data.get("ID") != null){
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
     * add new row identifier to view.data table
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
                    data.put(key, append(d, "Undefined"));
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
        String[] a = new String[size+1];
        Arrays.fill(a, "Undefined");
        data.put(key, a);
    }

    public HashMap<String, String[]> getDataTable() {
        return data;
    }

    public MTStorage getMtStorage() {
        return mtStorage;
    }

    public void updateDatatable(String oldname, String newname) {

        for(String key : data.keySet()){
            if (key.equals(oldname)){
                String[] entries = data.get(key);
                data.remove(key);
                data.put(newname.trim(), entries);
                break;
            }
        }

    }
}
