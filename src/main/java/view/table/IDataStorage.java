package view.table;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 11/24/16.
 */
public interface IDataStorage {

    HashMap<String, List<String>> getData();
    void addData(String key, List<String> data);

}
