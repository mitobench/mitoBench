package database;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.datastructure.Entry;

import java.util.HashMap;
import java.util.List;



public class DatabaseQueryHandler {

    private JsonDataParser jsonDataParser = new JsonDataParser();


    /**
     * add all data from database to mitoBench.
     *
     * @return all data from database
     */
    public HashMap<String, List<Entry>> getAllData() {
        try {

            long startTime = System.currentTimeMillis();
            String query = "http://mitodb.org/meta";

            System.out.println(query);
            HttpResponse<JsonNode> response_metadata = Unirest.get(query).asJson();

            long currtime_post_execution = System.currentTimeMillis();
            long diff = currtime_post_execution - startTime;

            long runtime_s = diff / 1000;
            if(runtime_s > 60) {
                long minutes = runtime_s / 60;
                long seconds = runtime_s % 60;
                System.out.println("Runtime of getting data from database: " + minutes + " minutes, and " + seconds + " seconds.");
            } else {
                System.out.println("Runtime of getting data from database: " + runtime_s + " seconds.");
            }


            HashMap<String, List<Entry>> data = jsonDataParser.getData(response_metadata);

            return data;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }



    public HashMap<String, List<Entry>> getDataSelection(String query) {

        try {

            long startTime = System.currentTimeMillis();

            String query_complete = "http://mitodb.org/meta?" + query;
            System.out.println(query_complete);

            HttpResponse<JsonNode> response_meta = Unirest.get(query_complete).asJson();



            long currtime_post_execution = System.currentTimeMillis();
            long diff = currtime_post_execution - startTime;

            long runtime_s = diff / 1000;
            if(runtime_s > 60) {
                long minutes = runtime_s / 60;
                long seconds = runtime_s % 60;
                System.out.println("Runtime of getting data from database: " + minutes + " minutes, and " + seconds + " seconds.");
            } else {
                System.out.println("Runtime of getting data from database: " + runtime_s + " seconds.");
            }
            return jsonDataParser.getData(response_meta);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;

    }
}
