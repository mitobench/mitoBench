package io.writer;

import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import controller.TableControllerUserBench;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 09.12.2016.
 */
public class ProjectWriter {
    private final ObservableList<ObservableList> data;
    private final Logger log;
    private String MITOBENCH_VERSION;


    public ProjectWriter(String mitoVersion, Logger LOG, ObservableList<ObservableList> dataToExport){
        MITOBENCH_VERSION = mitoVersion;
        this.data = dataToExport;
        this.log = LOG;
    }


    public void write(String outfile, TableControllerUserBench tableController, String[] user_defined_hg_list) throws IOException {

        //Initialize properly
        if (!outfile.endsWith(".mitoproj")) {
            outfile = outfile + ".mitoproj";
        }

        FileOutputStream fos = new FileOutputStream(outfile);
        FileChannel fileChannel = fos.getChannel();

        Date date = new Date();

        try {


            // write header
            // This has been generated with starter.MitoBenchStarter version XYZ, do not edit manually unless you know what you are doing.
            String header = "# This file has been generated with starter.MitoBenchStarter version " + MITOBENCH_VERSION +
                    " and contains all information of a starter.MitoBenchStarter project\n# Created on  "+ date.toString()
                    + "\n# Please do NOT edit manually unless you know what you are doing.\n\n";
            fileChannel.write(ByteBuffer.wrap(header.getBytes()));

            // write all data table information as Entry List
            HashMap<String, List<Entry>> tableData = tableController.getTable_content(data);

            fileChannel.write(ByteBuffer.wrap("<datatable\n".getBytes()));
            for(String sample_id : tableData.keySet()){
                fileChannel.write(ByteBuffer.wrap(("\tkey\t " + sample_id + "\n").getBytes()));

                // write column names
                fileChannel.write(ByteBuffer.wrap("\t\t##ID\t".getBytes()));
                for(Entry e : tableData.get(sample_id)){
                    if(!e.getIdentifier().equals("ID"))
                        fileChannel.write(ByteBuffer.wrap((e.getIdentifier() + "\t").getBytes()));
                }
                fileChannel.write(ByteBuffer.wrap("\n".getBytes()));

                // write column types
                fileChannel.write(ByteBuffer.wrap("\t\t#String\t".getBytes()));
                for(Entry e : tableData.get(sample_id)) {
                    if(!e.getIdentifier().equals("ID"))
                        fileChannel.write(ByteBuffer.wrap((e.getType().getTypeInformation() + "\t").getBytes()));

                }
                fileChannel.write(ByteBuffer.wrap("\n".getBytes()));

                // write data
                fileChannel.write(ByteBuffer.wrap(("\t\t" + sample_id + "\t").getBytes()));
                for(Entry e : tableData.get(sample_id)){
                    if(!e.getIdentifier().equals("ID"))
                        fileChannel.write(ByteBuffer.wrap((e.getData().getTableInformation() + "\t").getBytes()));
                }
                fileChannel.write(ByteBuffer.wrap("\n".getBytes()));
            }

            fileChannel.write(ByteBuffer.wrap(">\n".getBytes()));

            if(user_defined_hg_list!=null){
                fileChannel.write(ByteBuffer.wrap("<haplogroupList\n\t".getBytes()));
                for(String hg : user_defined_hg_list){
                    fileChannel.write(ByteBuffer.wrap((hg + ",").getBytes()));
                }

                fileChannel.write(ByteBuffer.wrap("\n>\n".getBytes()));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            fileChannel.close();
            fos.close();
        }

    }

}
