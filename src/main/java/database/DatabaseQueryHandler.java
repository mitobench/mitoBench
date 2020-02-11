package database;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.datastructure.Entry;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DatabaseQueryHandler {

    private JsonDataParser jsonDataParser = new JsonDataParser();
    private int number_of_samples;
    private int number_of_publications;
    private int number_of_countries_covered;

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
    public Set<String> getPopulationList() {
        Set<String> result = new HashSet<>();

        try {
            String query_complete = "http://mitodb.org/meta?select=population";
            HttpResponse<JsonNode> response_population = Unirest.get(query_complete).asJson();

            for (int i = 0; i < response_population.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_population.getBody().getArray().get(i);

                try {
                    String population = (String) map.get("population");
                    result.add(population.trim());
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
    public Set<String> getAccessionIDs() {
        Set<String> result = new HashSet<>();

        try {
            String query_complete = "http://mitodb.org/meta?select=accession_id";
            HttpResponse<JsonNode> response_accession_ids = Unirest.get(query_complete).asJson();

            for (int i = 0; i < response_accession_ids.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_accession_ids.getBody().getArray().get(i);

                try {
                    String accession_id = (String) map.get("accession_id");
                    result.add(accession_id.trim());
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
    public Set<String> getCountries() {
        Set<String> result = new HashSet<>();

        try {
            String query_complete_sample = "http://mitodb.org/meta?select=sample_origin_country";
            String query_complete_sampling = "http://mitodb.org/meta?select=sampling_country";

            HttpResponse<JsonNode> response_countries_sample = Unirest.get(query_complete_sample).asJson();
            HttpResponse<JsonNode> response_countries_sampling = Unirest.get(query_complete_sampling).asJson();

            for (int i = 0; i < response_countries_sample.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_countries_sample.getBody().getArray().get(i);

                try {
                    String countries = (String) map.get("sample_origin_country");
                    result.add(countries.trim());
                } catch (Exception e) {
                    continue;
                }
            }

            for (int i = 0; i < response_countries_sampling.getBody().getArray().length(); i++) {
                JSONObject map = (JSONObject) response_countries_sampling.getBody().getArray().get(i);

                try {
                    String countries = (String) map.get("sampling_country");
                    result.add(countries.trim());
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
            //System.out.println(query_complete);
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

        // get number of samples
        number_of_samples = getAccessionIDs().size();
        number_of_countries_covered = getCountries().size();
        number_of_publications = getAuthorList().size();

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
}
