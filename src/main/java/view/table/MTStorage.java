package view.table;

import java.util.HashMap;

/**
 * Created by neukamm on 11/24/16.
 */
public class MTStorage implements IDataStorage {

    HashMap<String, String> mtSequences;

    public MTStorage(){
        mtSequences = new HashMap<>();

    }



    @Override
    public HashMap<String, String> getData() {
        return mtSequences;
    }

    public void setMTStorage(DataTable table){

        // iterate over rows
        // get ID and

        String[] ids = table.getDataTable().get("ID");
        String[] seqs = table.getDataTable().get("MTSequence");

        for(int i = 0; i < ids.length; i++){
            String seq = seqs[i];
            if(!mtSequences.containsKey(ids[i]))
                mtSequences.put(ids[i], seq);
        }



    }
}
