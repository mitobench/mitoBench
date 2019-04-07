package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.datastructure.Entry;

import java.util.HashMap;
import java.util.List;
import java.util.Set;



public class DatabaseQueryHandler {

    private JsonDataParser jsonDataParser = new JsonDataParser();


    /**
     * add all data from database to mitoBench.
     *
     * @return all data from database
     */
    public HashMap<String, List<Entry>> getAllData() {
        try {
            HttpResponse<JsonNode> response_metadata = Unirest.get("http://mitodb.org/meta").asJson();

            return jsonDataParser.getData(response_metadata);

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Get data based on user-filtering.
     *
     * @param ids
     * @return
     */
    public HashMap<String, List<Entry>> getFilteredData(Set<String> ids){
        try {

            String query = "";
            for(String acc : ids) {
                query += "accession_id.eq." + acc + ",";
            }
            query = query.substring(0, query.length()-1);

            HttpResponse<JsonNode> response_meta = Unirest.get("http://mitodb.org/meta?or=(" + query +");").asJson();
            return jsonDataParser.getData(response_meta);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get only meta data to show in filtering window.
     * @return
     */
    public HashMap<String, List<Entry>> getMetaData() {
        try {
            HttpResponse<JsonNode> response_metadata = Unirest.get("http://mitodb.org/meta").asJson();
            HashMap<String, List<Entry>> data_map_metadata = jsonDataParser.getData(response_metadata);


            return data_map_metadata;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

}
