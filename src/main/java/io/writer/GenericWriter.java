package io.writer;

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

    private ObservableList<ObservableList> data;
    private TableControllerUserBench tableController;
    private String delimiter;

    public GenericWriter(TableControllerUserBench tableController, Logger LOG, ObservableList<ObservableList> dataToExport,
                         String delimiter){
        this.data = dataToExport;
        this.tableController = tableController;
        this.delimiter = delimiter;


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
                    }
                    header += colname + "\n";
                } else {
                    header += colname + delimiter;
                }
            }

            String text = "##" + header;
            fileChannel.write(ByteBuffer.wrap(text.getBytes()));


            // write header type:
            String headertypes = "";
            HashMap<String, String> colname_to_type = tableController.getHeadertypes();
            for(String colname : columns){
                headertypes += colname_to_type.get(colname) + delimiter;
            }

            text = "#" + headertypes.substring(0, headertypes.length()-1) + "\n";
            fileChannel.write(ByteBuffer.wrap(text.getBytes()));

            // write view.data
            for (ObservableList entry :  this.data) {
                text = "";
                for(int i = 0; i < entry.size(); i++){
                    if(i==index_mt){
                        String mt_seq = tableController.getDataTable().getMtStorage().getData().get(entry.get(index_id));
                        if(i == entry.size()-1){
                            text = mt_seq + "\n";
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        } else {
                            text = mt_seq + delimiter;
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        }

                    } else {
                        if(i == entry.size()-1){
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
        //Do nothing here, we dont care for CSV files atalll
    }


}
