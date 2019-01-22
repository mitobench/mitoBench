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


    public void addEntry(String key, String mtSeq) {
        if(!mtSequences.containsKey(key))
            mtSequences.put(key,mtSeq);
    }
}
