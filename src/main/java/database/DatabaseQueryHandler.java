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
            HttpResponse<JsonNode> response_sequences = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/sequences").asJson();
            HttpResponse<JsonNode> response_metadata = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();

            return  combineResults(response_metadata, response_sequences);

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

    public HashMap<String, List<Entry>> getGenerellData(Set<String> ids){
        try {

            String query = "";
            for(String acc : ids) {
                query += "accession_id.eq." + acc + ",";
            }
            query = query.substring(0, query.length()-1);

            HttpResponse<JsonNode> response_meta = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta?or=(" + query +");").asJson();
            HttpResponse<JsonNode> response_sequences = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/sequences?or=(" + query +");").asJson();

            return combineResults(response_meta, response_sequences);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, List<Entry>> getMetaData() {
        try {
            HttpResponse<JsonNode> response_metadata = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();
            HashMap<String, List<Entry>> data_map_metadata = jsonDataParser.getData(response_metadata);


            return data_map_metadata;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    private  HashMap<String, List<Entry>> combineResults(HttpResponse<JsonNode> response_meta, HttpResponse<JsonNode> response_sequences){
        HashMap<String, List<Entry>> data_map_sequences = jsonDataParser.getData(response_sequences);
        HashMap<String, List<Entry>> data_map_metadata = jsonDataParser.getData(response_meta);

        for(String k : data_map_sequences.keySet()){
            List<Entry> entry_data_map_sequences = data_map_sequences.get(k);
            List<Entry> entry_data_map_meta = data_map_metadata.get(k);
            for(Entry e : entry_data_map_meta)
                entry_data_map_sequences.add(e);

            data_map_sequences.put(k, entry_data_map_sequences);
        }

        return data_map_sequences;
    }
}
