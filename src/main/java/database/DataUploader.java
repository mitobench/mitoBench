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
import view.MitoBenchWindow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataUploader {
    private final DuplicatesChecker duplicatesChecker;
    private final DuplicatesHandler duplicatesHandler;
    private final DatabaseQueryHandler databaseQueryHandler;
    private final MitoBenchWindow mito;
    private Logger logger;
    private TableControllerUserBench tablecontroller;
    private int num_data_uploaded = 0;

    public DataUploader(MitoBenchWindow mito, DuplicatesHandler duplicatesHandler) {
        this.mito = mito;
        this.logger = mito.getLogClass().getLogger(this.getClass());
        this.tablecontroller = mito.getTableControllerUserBench();
        this.databaseQueryHandler = new DatabaseQueryHandler(mito);
        this.duplicatesChecker = new DuplicatesChecker(databaseQueryHandler);
        this.duplicatesHandler = duplicatesHandler;
        Unirest.setTimeouts(0, 0);
        // write data
        GenericWriter genericWriter = new GenericWriter(tablecontroller.getTable().getItems(), "\t", true);
        try {
            genericWriter.writeData("data_to_upload.tsv", tablecontroller);

        } catch (IOException e) {
            System.err.println("'data_to_upload.tsv' could not be created.\n" + e);
        }
    }


    /**
     * Parse meta file and check if data can be uploaded (all criteria are fulfilled)
     *
     * @param outfile
     */
    public void parseMeta(String outfile, String username, String password) {

        try {
            GenericInputParser genericInputReader = new GenericInputParser(outfile, logger, "\t", null);

            HashMap<String, List<Entry>> meta = genericInputReader.getCorrespondingData();
            String[] header = genericInputReader.getHeader();
            String[] types = genericInputReader.getTypes();

            System.out.println("Start data upload");
            System.out.println("Upload failed for sample:");
            for (String acc : meta.keySet()){
                List<Entry> row = meta.get(acc);

                // check row
                if(checkPassed(row, acc)){

                    // upload
                    if(duplicatesChecker.isDuplicate(acc)){//,tablecontroller.getColIndex("Author"),tablecontroller.getColIndex("ID"),tablecontroller.getColIndex("Labsample ID"))) {
                        // do something
                        duplicatesHandler.collectDuplicates(row, acc);


                    } else {
                        // upload
                        upload(header, types, row, acc, username, password);
                    }
                }
            }

            if(duplicatesHandler.getNumOfDuplicates() > 0) {
                duplicatesHandler.handle();
                num_data_uploaded += duplicatesHandler.getNumberOfUploadedDuplicates();
            }

            System.out.println("Upload finished. " + num_data_uploaded + "/" +
                    tablecontroller.getTable().getItems().size() + " entries added to database");

            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Files.exists(new File("data_to_upload.tsv").toPath()))
                Files.delete(new File("data_to_upload.tsv").toPath());


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

        int index_tma_latitude = -1;
        int index_tma_longitude = -1;
        int index_tma_intermediate_region = -1;
        int index_tma_country = -1;
        int index_tma_region = -1;
        int index_tma_city = -1;
        int index_tma_subregion = -1;

        int index_labsample_id = -1;
        int index_surname = -1;

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
            } else if(e.getIdentifier().equals("Labsample ID")){
                index_labsample_id = i;
            } else if(e.getIdentifier().equals("Submitter surname")){
                index_surname = i;
            }

            else if(e.getIdentifier().equals("Sample Latitude")){
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
            }

            else if(e.getIdentifier().equals("Sampling Latitude")){
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
            }

            else if(e.getIdentifier().equals("TMA inferred Latitude")){
                index_tma_latitude = i;
            } else if(e.getIdentifier().equals("TMA inferred Longitude")){
                index_tma_longitude = i;
            } else if(e.getIdentifier().equals("TMA inferred Intermediate Region")){
                index_tma_intermediate_region = i;
            } else if(e.getIdentifier().equals("TMA inferred Country")){
                index_tma_country = i;
            } else if(e.getIdentifier().equals("TMA inferred Continent")){
                index_tma_region = i;
            } else if(e.getIdentifier().equals("TMA inferred City")){
                index_tma_city = i;
            } else if(e.getIdentifier().equals("TMA inferred Subregion")){
                index_tma_subregion = i;
            }
        }

        // test mandatory attributes
        String error = "";
        if(index_completeness != -1 && !row.get(index_completeness).getData().getTableInformation().equals("")){
            double percentage_of_N = Double.parseDouble(row.get(index_completeness).getData().getTableInformation());
            double threshold = 1.0;

            if(percentage_of_N > threshold){
                error += "" + percentage_of_N + "% of missing data; ";
                passed = false;
            }
        }

        if(index_author == -1 || row.get(index_author).getData().getTableInformation().equals("")){
            error += "Author is missing; ";
            passed = false;
        }

        if(index_user_email == -1 || row.get(index_user_email).getData().getTableInformation().equals("")){
            error += "Email address of submitter is missing; ";
            passed = false;
        }

        if(index_data_type == -1 || row.get(index_data_type).getData().getTableInformation().equals("")){
            error += "data type is missing; ";
            passed = false;
        }

        if(index_publication_status == -1 || row.get(index_publication_status).getData().getTableInformation().equals("")){
            error += "publication status is missing; ";
            passed = false;
        }

        if(index_sequence == -1 || row.get(index_sequence).getData().getTableInformation().equals("")){
            error += "MT sequence is missing; ";
            passed = false;
        }

        // check geo information

        if((index_sample_origin_latitude == -1 || row.get(index_sample_origin_latitude).getData().getTableInformation().equals("")) &&
                (index_sample_origin_longitude == -1 || row.get(index_sample_origin_longitude).getData().getTableInformation().equals("")) &&
                (index_sample_origin_region == -1 || row.get(index_sample_origin_region).getData().getTableInformation().equals("")) &&
                (index_sample_origin_city == -1 || row.get(index_sample_origin_city).getData().getTableInformation().equals("")) &&
                (index_sample_origin_country == -1 || row.get(index_sample_origin_country).getData().getTableInformation().equals("")) &&
                (index_sample_origin_subregion == -1 || row.get(index_sample_origin_subregion).getData().getTableInformation().equals("")) &&
                (index_sample_origin_intermediate_region == -1 || row.get(index_sample_origin_intermediate_region).getData().getTableInformation().equals(""))
                &&
                (index_sampling_latitude == -1 || row.get(index_sampling_latitude).getData().getTableInformation().equals("")) &&
                (index_sampling_longitude == -1 || row.get(index_sampling_longitude).getData().getTableInformation().equals("")) &&
                (index_sampling_region == -1 || row.get(index_sampling_region).getData().getTableInformation().equals("")) &&
                (index_sampling_city == -1 || row.get(index_sampling_city).getData().getTableInformation().equals("")) &&
                (index_sampling_country == -1 || row.get(index_sampling_country).getData().getTableInformation().equals("")) &&
                (index_sampling_subregion == -1 || row.get(index_sampling_subregion).getData().getTableInformation().equals("")) &&
                (index_sampling_intermediate_region == -1 || row.get(index_sampling_intermediate_region).getData().getTableInformation().equals(""))
                &&
                (index_tma_latitude == -1 || row.get(index_tma_latitude).getData().getTableInformation().equals("")) &&
                (index_tma_longitude == -1 || row.get(index_tma_longitude).getData().getTableInformation().equals("")) &&
                (index_tma_region == -1 || row.get(index_tma_region).getData().getTableInformation().equals("")) &&
                (index_tma_city == -1 || row.get(index_tma_city).getData().getTableInformation().equals("")) &&
                (index_tma_country == -1 || row.get(index_tma_country).getData().getTableInformation().equals("")) &&
                (index_tma_subregion == -1 || row.get(index_tma_subregion).getData().getTableInformation().equals("")) &&
                (index_tma_intermediate_region == -1 || row.get(index_tma_intermediate_region).getData().getTableInformation().equals("")))
        {

            error += "geographical information is missing; ";
            passed = false;
        }

        if(!error.equals("")){
            System.out.print("\tAccessionID: " + acc + ";\t");
            if(index_labsample_id != -1){
                System.out.print("LabID: " + row.get(index_labsample_id).getData().getTableInformation().trim() + ";\t");
            }
            if(index_surname != -1){
                System.out.print("Submitter: " + row.get(index_surname).getData().getTableInformation().trim() + ";\t");
            }
            if(index_author != -1){
                System.out.print("Author: " + row.get(index_author).getData().getTableInformation().trim() + " et al.;\t");
            }
            System.out.println("Reason: " + error);

        }

        return passed;
    }



    public void upload(String[] header, String[] types, List<Entry> row, String acc, String username, String password) {

        Map<String, Object> fields =  buildBody(header, types, row, acc);
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

        try {

            HttpResponse<JsonNode> response_authors = Unirest
                    .post("http://mitodb.org/meta")
                    .headers(headers)
                    .fields(fields)
                    .asJson();

            if(!response_authors.getBody().toString().equals("{}")){
                System.out.println(response_authors.getBody().toString());
            } else {
                num_data_uploaded++;
            }
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

        Map<String, Object> body = new HashMap<>();
        body.put(header[0], acc);

        if(header.length == row.size()+1){

            for(int i = 1; i < header.length; i++){
                if (types[i].equals("String") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
                    body.put(header[i].trim().toLowerCase(), row.get(i-1).getData().getTableInformation().trim());

                } else if(types[i].equals("real") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
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

