package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import controller.TableControllerUserBench;
import io.datastructure.Entry;
import io.reader.GenericInputParser;
import io.writer.GenericWriter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUploader {
    private Logger logger;
    private TableControllerUserBench tablecontroller;

    public DataUploader(TableControllerUserBench tablecontroller, Logger logger) {
        this.logger = logger.getLogger(this.getClass());
        this.tablecontroller = tablecontroller;
        // write data
        GenericWriter genericWriter = new GenericWriter(tablecontroller.getTable().getItems(), ",", true);
        try {
            genericWriter.writeData("data_to_upload.csv", tablecontroller);

        } catch (IOException e) {
            System.err.println("'data_to_upload.csv' could not be created.\n" + e);
        }
    }


    /**
     * Parse meta file and check if data can be uploaded (all criteria are fulfilled)
     *
     * @param outfile
     */
    public void parseMeta(String outfile) {

        try {
            GenericInputParser genericInputReader = new GenericInputParser(outfile, logger, ",");

            HashMap<String, List<Entry>> meta = genericInputReader.getCorrespondingData();
            String[] header = genericInputReader.getHeader();
            String[] types = genericInputReader.getTypes();

            for (String acc : meta.keySet()){
                List<Entry> row = meta.get(acc);

                // check row
                if(checkPassed(row, acc)){
                    // upload
                   // upload(header, types, row, acc);
                }

            }


            if (Files.exists(new File("data_to_upload.csv").toPath()))
                Files.delete(new File("data_to_upload.csv").toPath());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPassed(List<Entry> row, String acc) {
        boolean passed = true;

        int index_completeness = -1;
        int index_user_email = -1;
        int index_publication_status = -1;
        int index_author = -1;
        int index_data_type = -1;
        int index_sequence = -1;
        int index_sample_origin_latitude = -1;
        int index_sample_origin_longitude = -1;
        int index_sample_origin_intermediate_region = -1;
        int index_sample_origin_country = -1;
        int index_sample_origin_region = -1;
        int index_sample_origin_city = -1;
        int index_sample_origin_subregion = -1;
        int index_sampling_latitude = -1;
        int index_sampling_longitude = -1;
        int index_sampling_intermediate_region = -1;
        int index_sampling_country = -1;
        int index_sampling_region = -1;
        int index_sampling_city = -1;
        int index_sampling_subregion = -1;
        int index_modern_ancient = -1;



        for (int i = 0; i < row.size(); i++){
            Entry e = row.get(i);

            if (e.getIdentifier().equals("Percentage of N")){
                index_completeness = i;
            } else if(e.getIdentifier().equals("Submitter Email")){
                index_user_email = i;
            } else if(e.getIdentifier().equals("Publication Status")){
                index_publication_status = i;
            } else if(e.getIdentifier().equals("Author")){
                index_author = i;
            } else if(e.getIdentifier().equals("Data Type")){
                index_data_type = i;
            } else if(e.getIdentifier().equals("MTSequence")){
                index_sequence = i;
            } else if(e.getIdentifier().equals("Sample Latitude")){
                index_sample_origin_latitude = i;
            } else if(e.getIdentifier().equals("Sample Longitude")){
                index_sample_origin_longitude = i;
            } else if(e.getIdentifier().equals("Sample Intermediate Region")){
                index_sample_origin_intermediate_region = i;
            } else if(e.getIdentifier().equals("Sample Country")){
                index_sample_origin_country = i;
            } else if(e.getIdentifier().equals("Sample Continent")){
                index_sample_origin_region = i;
            } else if(e.getIdentifier().equals("Sample City")){
                index_sample_origin_city = i;
            } else if(e.getIdentifier().equals("Sample Subregion")){
                index_sample_origin_subregion = i;
            } else if(e.getIdentifier().equals("Sampling Latitude")){
                index_sampling_latitude = i;
            } else if(e.getIdentifier().equals("Sampling Longitude")){
                index_sampling_longitude = i;
            } else if(e.getIdentifier().equals("Sampling Intermediate Region")){
                index_sampling_intermediate_region = i;
            } else if(e.getIdentifier().equals("Sampling Country")){
                index_sampling_country = i;
            } else if(e.getIdentifier().equals("Sampling Continent")){
                index_sampling_region = i;
            } else if(e.getIdentifier().equals("Sampling City")){
                index_sampling_city = i;
            } else if(e.getIdentifier().equals("Sampling Subregion")){
                index_sampling_subregion = i;
            } else if(e.getIdentifier().equals("Modern/Ancient Data")){
                index_modern_ancient = i;
          }
        }

        // test mandatory attributes

        if(index_completeness != -1 && !row.get(index_completeness).getData().getTableInformation().equals("")){
            double percentage_of_N = Double.parseDouble(row.get(index_completeness).getData().getTableInformation());
            double threshold = 0.1;

            if(index_modern_ancient != -1 && !row.get(index_modern_ancient).getData().getTableInformation().equals("")){

                if (row.get(index_modern_ancient).getData().getTableInformation().equals("modern")){
                    threshold = 0.01;
                } else if (row.get(index_modern_ancient).getData().getTableInformation().equals("ancient")){
                    threshold = 0.02;
                }
            }

            if(percentage_of_N > threshold){
                System.out.println("Sequence: " + acc + ": " + percentage_of_N*100 + "% of missing data");
                passed = false;
            }
        }

        if(index_author == -1 || row.get(index_completeness).getData().getTableInformation().equals("")){
            System.out.println("Sequence: " + acc + ": Author is missing.");
            passed = false;
        }

        if(index_user_email == -1 || row.get(index_user_email).getData().getTableInformation().equals("")){
            System.out.println("Sequence: " + acc + ": Email address of submitter is missing.");
            passed = false;
        }

        if(index_data_type == -1 || row.get(index_data_type).getData().getTableInformation().equals("")){
            System.out.println("Sequence: " + acc + ": data type is missing.");
            passed = false;
        }

        if(index_publication_status == -1 || row.get(index_publication_status).getData().getTableInformation().equals("")){
            System.out.println("Sequence: " + acc + ": publication status is missing.");
            passed = false;
        }

        if(index_sequence == -1 || row.get(index_sequence).getData().getTableInformation().equals("")){
            System.out.println("Sequence: " + acc + ": MT sequence is missing.");
            passed = false;
        }

        // check geo information
        boolean geo_set = false;

        if(index_sample_origin_latitude != -1 && index_sample_origin_longitude != -1 &&
                !row.get(index_sample_origin_latitude).getData().getTableInformation().equals("") && !row.get(index_sample_origin_longitude).getData().getTableInformation().equals("")){
            geo_set = true;
        } else if (index_sample_origin_region != -1 && !row.get(index_sample_origin_region).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sample_origin_city != -1 && !row.get(index_sample_origin_city).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sample_origin_country != -1 && !row.get(index_sample_origin_country).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sample_origin_subregion != -1 && !row.get(index_sample_origin_subregion).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sample_origin_intermediate_region != -1 && !row.get(index_sample_origin_intermediate_region).getData().getTableInformation().equals("")) {
            geo_set = true;


        } else if(index_sampling_latitude != -1 && index_sampling_longitude != -1 &&
                !row.get(index_sampling_latitude).getData().getTableInformation().equals("") && !row.get(index_sampling_longitude).getData().getTableInformation().equals("")){
            geo_set = true;
        } else if (index_sampling_region != -1 && !row.get(index_sampling_region).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sampling_city != -1 && !row.get(index_sampling_city).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sampling_country != -1 && !row.get(index_sampling_country).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sampling_subregion != -1 && !row.get(index_sampling_subregion).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else if (index_sampling_intermediate_region != -1 && !row.get(index_sampling_intermediate_region).getData().getTableInformation().equals("")) {
            geo_set = true;
        } else {
            geo_set = false;
            passed = geo_set;
            System.out.println("Sequence: " + acc + ": geographical information is missing.");
        }


        return passed;
    }

    public void upload(String[] header, String[] types, List<Entry> row, String acc) {

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

        Map<String, Object> fields =  buildBody(header, types, row, acc);

        try {
            HttpResponse<JsonNode> response_authors = Unirest
                    .post("http://mitodb.org/meta")
                    .basicAuth("mitodbreader_nonpublic", "$20MitoWrite17")
                    .headers(headers)
                    .fields(fields)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return
     * @param header
     * @param types
     * @param row
     * @param acc
     */
    private Map<String, Object>  buildBody(String[] header, String[] types, List<Entry> row, String acc){

        Map<String, Object>  body = new HashMap<>();
        body.put(header[0], acc);

        if(header.length == row.size()+1){

            for(int i = 1; i < header.length; i++){
                if (types[i].equals("String") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
                    body.put(header[i].trim().toLowerCase(), row.get(i-1).getData().getTableInformation().trim());

                } else if(types[i].equals("float") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
                    body.put(header[i].trim().toLowerCase(), Double.parseDouble(row.get(i - 1).getData().getTableInformation().trim()));

                } else if (types[i].equals("int") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
                    body.put(header[i].trim().toLowerCase(), Integer.parseInt(row.get(i-1).getData().getTableInformation().trim()));
                }
            }

            return body;
        } else {
            System.err.println("Header and row are of different length. Upload not possible.");
            return null;
        }

    }


}
