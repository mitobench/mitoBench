package view.table;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 11/24/16.
 */
public class MTStorage implements IDataStorage {

    HashMap<String, List<String>> mtSequences;

    public MTStorage(){
        mtSequences = new HashMap<>();

    }



    @Override
    public HashMap<String, List<String>> getData() {
        return mtSequences;
    }

    @Override
    public void addData(String key, List<String> data) {
        mtSequences.put(key, data);
    }
}
