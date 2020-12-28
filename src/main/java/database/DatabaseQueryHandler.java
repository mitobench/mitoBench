package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.options.Options;
import io.datastructure.Entry;
import org.json.JSONObject;
import view.MitoBenchWindow;
import view.dialogues.settings.DuplicateDecisionMaker;

import java.util.*;

public class DatabaseQueryHandler {

    private final MitoBenchWindow mito;
    private JsonDataParser jsonDataParser = new JsonDataParser(this);
    private int number_of_samples;
    private int number_of_publications;
    private int number_of_countries_covered;
    private int number_of_continents_covered;
    private int number_of_ancient_samples;
    private int number_of_modern_samples;

    public DatabaseQueryHandler(MitoBenchWindow mito){
        this.mito = mito;

    }

    /**
     * add all data from database to mitoBench.
     *
     * @return all data from database
     */
    public HashMap<String, List<Entry>> getAllData() {
        try {

            String query = "http://mitodb.org/meta";

            HttpResponse<JsonNode> response_metadata = Unirest.get(query).asJson();
            HashMap<String, List<Entry>> data = jsonDataParser.getData(response_metadata);

            return data;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Get all publications (author and publication date) from database
     *
     * @return list of publications (unique entries)
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
     * Get all data from a specific column.
     *
     * @param column name
     * @param type of column
     * @return List with column entries
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
     * Returns all entries in database with the accession ID passed as argument.
     *
     * @param query containing the accession DI
     * @return
     */
    public HashMap getDuplicatesFromDatabase(String query) {

        try {
            String query_complete = "http://mitodb.org/meta?" + query;
            HttpResponse<JsonNode> response = Unirest.get(query_complete).asJson();

            HashMap data_map = jsonDataParser.getDataList(response);

            return data_map;

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Method that returns data from database. The query is passed as argument.
     *
     * @param query for getting data from database
     * @return requested data
     */
    public HashMap<String, List<Entry>> getDataSelection(String query) {

        try {
            String query_complete = "http://mitodb.org/meta?" + query;
            HttpResponse<JsonNode> response_meta = Unirest.get(query_complete).asJson();

            return jsonDataParser.getData(response_meta);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Methods that calculates database statistics:
     *
     *  - number of samples (all, modern, ancient)
     *  - which continents/countries are covered
     *  - how many publications are represented
     */
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

            List<String> list_modern_ancient = getColumnSet("ancient_modern","String");
            number_of_modern_samples = Collections.frequency(list_modern_ancient, "modern");
            number_of_ancient_samples = Collections.frequency(list_modern_ancient, "ancient");
        }
    }


    /**
     * method to check ist database is connected.
     * @return
     */
    public boolean connecting() {
        try {
            Options.refresh();
            // test query to check it database is connected
            Unirest.get("http://mitodb.org/meta?select=author").asJson();
            System.out.println("Database connected.");
            return true;
        } catch (UnirestException e) {
            System.out.println("Database connection is not possible. Please check your internet connection.");
            return false;
        }
    }

    public void openDuplicateDecisionMaker(List<Entry> database_entrylist, List<Entry> new_entrylist, String accession) {
        DuplicateDecisionMaker duplicateDecisionMaker = new DuplicateDecisionMaker(mito);
        duplicateDecisionMaker.addEntries(database_entrylist, new_entrylist, accession);
        duplicateDecisionMaker.show();
    }

    /*
            Getter
     */

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


}
