package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonDataParser {

    /**
     * Get all information contained in the API response object (json).
     * @param response
     * @return
     */
    public HashMap<String, List<Entry>> getData(HttpResponse<JsonNode> response) {

        HashMap<String, List<Entry>> data_map = new HashMap();
        ColumnNameMapper mapper = new ColumnNameMapper();
        for (int i = 0; i < response.getBody().getArray().length(); i++){
            JSONObject map = (JSONObject) response.getBody().getArray().get(i);
            String accession = (String) map.get("accession_id");
            data_map.put(accession,getEntries(map, mapper));
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
            // don't publish user password
            if (!key.equals("user_password") && !key.equals("user_alias")){
                e = new Entry(mapper.mapString(key), new CategoricInputType("String"), new GenericInputData(d));
                entries.add(e);
            }
        }

        return entries;
    }
}
