package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.json.JSONObject;

import java.util.*;

public class JsonDataParser {

    private ColumnNameMapper mapper = new ColumnNameMapper();
    private boolean duplicates_detected;
    private int counter;

    public JsonDataParser(){
    }

    /**
     * Get all information contained in the API response object (json).
     * @param response
     * @return
     */
    public HashMap<String, List<Entry>> getData( HttpResponse<JsonNode> response) {
        counter = 1;

        HashMap<String, List<Entry>> data_map = new HashMap();
        for (int i = 0; i < response.getBody().getArray().length(); i++){
            JSONObject map = (JSONObject) response.getBody().getArray().get(i);
            String accession = (String) map.get("accession_id");

            if (data_map.containsKey(accession)){
                // store both entries, let user decide later which one to take
                String acc_new = accession + "_XXX" + counter;
                duplicates_detected = true;
                List<Entry> entryy_new = getEntries(map, mapper);
                data_map.put(acc_new, entryy_new);
                duplicates_detected = false;
                counter++;
            } else {
                data_map.put(accession, getEntries(map, mapper));
            }
        }

        return data_map;
    }


    /**
     * Get data from database with duplicated entries.
     * @param response from database
     * @return list with entries (including duplicates)
     */
    public HashMap<Integer, List<Entry>> getDataList(HttpResponse<JsonNode> response) {

        HashMap<Integer, List<Entry>> data_map = new HashMap();
        for (int i = 0; i < response.getBody().getArray().length(); i++){
            JSONObject map = (JSONObject) response.getBody().getArray().get(i);
            data_map.put(i, getEntries(map, mapper));
        }

        return data_map;
    }

    /**
     * Parse json array entry. Each entry is one sample entry including all available information on the database.
     *
     * @param json_data_map
     * @param mapper
     * @return
     */
    private List<Entry> getEntries(JSONObject json_data_map, ColumnNameMapper mapper) {

        List<Entry> entries = new ArrayList<>();
        for (String key : json_data_map.keySet()){
            String d = json_data_map.get(key).toString();
            Entry e;

            if(key.equals("accession_id") && duplicates_detected){
                e = new Entry(mapper.mapString(key), new CategoricInputType("String"), new GenericInputData(d+"_XXX"+counter));
                entries.add(e);
            }
            // don't publish user password and alias
            else if (!key.equals("user_password") && !key.equals("user_alias")){

                e = new Entry(mapper.mapString(key), new CategoricInputType("String"), new GenericInputData(d));
                entries.add(e);
            }
        }

        return entries;
    }


}
