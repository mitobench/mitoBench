package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.options.Options;
import io.datastructure.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DatabaseQueryHandler {

    private JsonDataParser jsonDataParser = new JsonDataParser();
    private int number_of_samples;
    private int number_of_publications;
    private int number_of_countries_covered;
    private int number_of_continents_covered;
    private int number_of_ancient_samples;
    private int number_of_modern_samples;

    /**
     * add all data from database to mitoBench.
     *
     * @return all data from database
     */
    public HashMap<String, List<Entry>> getAllData() {
        try {

            long startTime = System.currentTimeMillis();
            String query = "http://mitodb.org/meta";

            HttpResponse<JsonNode> response_metadata = Unirest.get(query).asJson();
//
//            long currtime_post_execution = System.currentTimeMillis();
//            long diff = currtime_post_execution - startTime;
//
//            long runtime_s = diff / 1000;
//            if(runtime_s > 60) {
//                long minutes = runtime_s / 60;
//                long seconds = runtime_s % 60;
//                System.out.println("Runtime of getting data from database: " + minutes + " minutes, and " + seconds + " seconds.");
//            } else {
//                System.out.println("Runtime of getting data from database: " + runtime_s + " seconds.");
//            }
//

            HashMap<String, List<Entry>> data = jsonDataParser.getData(response_metadata);

            return data;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @return
     */
    public Set<String> getAuthorList() {
        Set<String> result = new HashSet<>();

        try {
            String query_complete = "http://mitodb.org/meta?select=author,publication_date";
            HttpResponse<JsonNode> response_authors = Unirest.get(query_complete).asJson();

            for (int i = 0; i < response_authors.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_authors.getBody().getArray().get(i);
                String author = (String) map.get("author");
                String year = map.get("publication_date") + "";
                result.add(author.trim() + "," + year.trim());
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return result;
    }



    /**
     * @return
     */
    public List getColumnSet(String column, String type) {
        List result = new ArrayList();

        try {
            String query_complete = "http://mitodb.org/meta?select="+column;

            HttpResponse<JsonNode> response_column = Unirest.get(query_complete).asJson();

            for (int i = 0; i < response_column.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_column.getBody().getArray().get(i);
                try {
                    if(type.equals("int")){
                        int value = (int) map.get(column);
                        result.add(value);
                    } else if(type.equals("String")){
                        String value = (String) map.get(column);
                        result.add(value.trim());
                    }
                } catch (Exception e) {
                    continue;
                }
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return result;
    }



    /**
     * @return
     */
    public List<String> getModernAndAncient() {
        List<String> result = new ArrayList<>();

        try {
            String query_complete = "http://mitodb.org/meta?select=ancient_modern";

            HttpResponse<JsonNode> response_column = Unirest.get(query_complete).asJson();

            for (int i = 0; i < response_column.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_column.getBody().getArray().get(i);

                try {
                    String modern_entry = (String) map.get("ancient_modern");
                    result.add(modern_entry.trim());
                } catch (Exception e) {
                    continue;
                }
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * @param query
     * @return
     */
    public HashMap<String, List<Entry>> getDataSelection(String query) {

        try {

            long startTime = System.currentTimeMillis();

            String query_complete = "http://mitodb.org/meta?" + query;
            HttpResponse<JsonNode> response_meta = Unirest.get(query_complete).asJson();

//            long currtime_post_execution = System.currentTimeMillis();
//            long diff = currtime_post_execution - startTime;
//
//            long runtime_s = diff / 1000;
//            if(runtime_s > 60) {
//                long minutes = runtime_s / 60;
//                long seconds = runtime_s % 60;
//                System.out.println("Runtime of getting data from database: " + minutes + " minutes, and " + seconds + " seconds.");
//            } else {
//                System.out.println("Runtime of getting data from database: " + runtime_s + " seconds.");
//            }
            return jsonDataParser.getData(response_meta);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void calculateDBstats() {

        number_of_samples = getColumnSet("meta_info_id", "int").size();

        if(number_of_samples == 0){
            number_of_countries_covered=0;
            number_of_continents_covered=0;
            number_of_publications=0;
            number_of_modern_samples=0;
            number_of_ancient_samples=0;
        } else {
            Set<String> set_tmp = new HashSet<>();
            set_tmp.addAll(getColumnSet("sample_origin_country", "String"));
            set_tmp.addAll(getColumnSet("sampling_country", "String"));
            number_of_countries_covered = set_tmp.size();

            set_tmp.clear();
            set_tmp.addAll(getColumnSet("sample_origin_region", "String"));
            set_tmp.addAll(getColumnSet("sampling_region", "String"));

            number_of_continents_covered = set_tmp.size();
            number_of_publications = getAuthorList().size();

            List<String> list_modern_ancient = getModernAndAncient();
            number_of_modern_samples = Collections.frequency(list_modern_ancient, "modern");
            number_of_ancient_samples = Collections.frequency(list_modern_ancient, "ancient");
        }


    }

    public int getNumber_of_samples() {
        return number_of_samples;
    }

    public int getNumber_of_publications() {
        return number_of_publications;
    }

    public int getNumber_of_countries_covered() {
        return number_of_countries_covered;
    }

    public int getNumber_of_continents() {
        return number_of_continents_covered;
    }

    public int getNumber_of_modern_samples() {
        return number_of_modern_samples;
    }

    public int getNumber_of_ancient_samples() {
        return number_of_ancient_samples;
    }


    public boolean connecting() {
        try {
            Options.refresh();
            Unirest.get("http://mitodb.org/meta?select=author").asJson();

            System.out.println("Database connected.");
            return true;
        } catch (UnirestException e) {
            System.out.println("Database connection is not possible. Please check your internet connection.");
            return false;
        }
    }
}
