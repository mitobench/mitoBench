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
            final HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();
            HashMap<String, List<Entry>> data_map = jsonDataParser.getData(response);
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
