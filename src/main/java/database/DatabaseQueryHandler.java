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


    public HashMap<String, List<Entry>> getAllData() {
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/sequences").asJson();
            HttpResponse<JsonNode> response2 = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();
            HashMap<String, List<Entry>> data_map = jsonDataParser.getData(response);
            HashMap<String, List<Entry>> data_map2 = jsonDataParser.getData(response2);
            for(String k : data_map.keySet()){
                List<Entry> entry_data_map = data_map.get(k);
                List<Entry> entry_data_map2 = data_map2.get(k);
                for(Entry e : entry_data_map2)
                    entry_data_map.add(e);

                data_map.put(k, entry_data_map);
            }

            return data_map;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Set<String> getLocationData(String attr){

        try {
            HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta?select="+attr).asJson();

            Set<String> data = jsonDataParser.getSet(response);

            return data;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, List<Entry>> getGenerellData(String query){
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/" + query).asJson();
            return jsonDataParser.getData(response);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

}
