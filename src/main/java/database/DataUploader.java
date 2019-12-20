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
        GenericWriter genericWriter = new GenericWriter(tablecontroller.getSelectedRows(), ",", true);
        try {
            genericWriter.writeData("data_to_upload.csv", tablecontroller);

        } catch (IOException e) {
            System.err.println("'data_to_upload.csv' could not be created.\n" + e);
        }
    }


    /**
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
                upload(header, types, row, acc);
            }


            if (Files.exists(new File("data_to_upload.csv").toPath()))
                Files.delete(new File("data_to_upload.csv").toPath());


        } catch (IOException e) {
            e.printStackTrace();
        }

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

            System.out.println(response_authors.getBody().toString());
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
                    body. put(header[i].trim().toLowerCase(), row.get(i-1).getData().getTableInformation().trim());

                } else if(types[i].equals("float") && !row.get(i-1).getData().getTableInformation().trim().equals("")){
                    body.put(header[i].trim().toLowerCase(), Double.parseDouble(row.get(i - 1).getData().getTableInformation().trim()));

                } else if (types[i].equals("int") && row.get(i-1).getData().getTableInformation().trim().equals("")){
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
