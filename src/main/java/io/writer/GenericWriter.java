package io.writer;

import database.ColumnNameMapper;
import io.IOutputData;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import controller.TableControllerUserBench;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class GenericWriter implements IOutputData {

    private final boolean addFasta;
    private ObservableList<ObservableList> data;
    private TableControllerUserBench tableController;
    private String delimiter;

    public GenericWriter(TableControllerUserBench tableController, Logger LOG, ObservableList<ObservableList> dataToExport,
                         String delimiter, boolean addfasta){
        this.data = dataToExport;
        this.tableController = tableController;
        this.delimiter = delimiter;
        this.addFasta = addfasta;

    }


    /**
     * This method writes the current content of the tableview to a csv file incl. header line
     * (comma separated)
     *
     * @param file
     * @param tableController
     * @throws Exception
     */
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        ColumnNameMapper mapper = new ColumnNameMapper();

        if(delimiter.equals(",")){
            if(!file.endsWith(".csv"))
                file = file + ".csv";
        } else {
            if(!file.endsWith(".tsv"))
                file = file + ".tsv";
        }

        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fileChannel = fos.getChannel();

        try {

            int index_id = tableController.getColIndex("ID");
            int index_mt = tableController.getColIndex("MTSequence");


            // write header
            String header = "";
            List<String> columns = this.tableController.getCurrentColumnNames();
            for (int i = 0; i < columns.size(); i++){
                String colname = columns.get(i);
                if(i == columns.size()-1){
                    if(colname.contains("(Grouping)")){
                        colname = colname.replace("(Grouping)","").trim();
                    } else if(!addFasta && colname.equals("MTSequence")){
                        header = header.substring(0, header.length() - 1);
                        colname = "";
                    }
                    header += mapper.mapString(colname) + "\n";
                } else {
                    if(!addFasta && colname.equals("MTSequence")){
                        header = header.substring(0, header.length() - 1);
                    } else {
                        header += mapper.mapString(colname) + delimiter;
                    }

                }
            }

            String text = "##" + header;
            fileChannel.write(ByteBuffer.wrap(text.getBytes()));


            // write header type:
            String headertypes = "";
            HashMap<String, String> colname_to_type = tableController.getHeadertypes();
            for(String colname : columns){
                if(!addFasta && colname.equals("MTSequence")){
                    headertypes = headertypes.substring(0, headertypes.length() - 1);
                } else {
                    headertypes += colname_to_type.get(colname) + delimiter;
                }
            }

            if(headertypes.endsWith(","))
                headertypes = headertypes.substring(0, headertypes.length() - 1);
            text = "#" + headertypes + "\n";
            fileChannel.write(ByteBuffer.wrap(text.getBytes()));

            int limit = data.get(0).size();
            if(!addFasta){
                if(index_mt == data.get(0).size()-1){
                    limit = data.get(0).size()-1;
                } else {
                    limit = data.get(0).size();
                }
            }

            // write view.data
            for (ObservableList entry :  this.data) {
                text = "";
                for(int i = 0; i < limit; i++){
                    if(i==index_mt){
                        String mt_seq = tableController.getDataTable().getMtStorage().getData().get(entry.get(index_id));
                        if(i == limit-1){
                            text = mt_seq + "\n";
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        } else {
                            text = mt_seq + delimiter;
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        }

                    } else {
                        if(i == limit-1){
                            text = entry.get(i) + "\n";
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        } else {
                            text = entry.get(i) + delimiter;
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            fileChannel.close();
            fos.close();
        }
    }

    @Override
    public void setGroups(String groupID) {
        //Do nothing here
    }


}
