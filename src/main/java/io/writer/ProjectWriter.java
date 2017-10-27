package io.writer;

import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import controller.TableControllerUserBench;

import java.io.*;
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


    public void write(String outfile, TableControllerUserBench tableController, String[] user_defined_hg_list) throws IOException, ProjectException {

        //Initialize properly
        if (!outfile.endsWith(".mitoproj")) {
            outfile = outfile + ".mitoproj";
        }

        Date date = new Date();
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outfile));

            // write header
            // This has been generated with MitoBenchStarter version XYZ, do not edit manually unless you know what you are doing.
            String header = "# This file has been generated with MitoBenchStarter version " + MITOBENCH_VERSION +
                    " and contains all information of a MitoBenchStarter project\n# Created on  "+ date.toString()
                    + "\n# Please do NOT edit manually unless you know what you are doing.\n\n";
            writer.write(header);

            // write all data table information as Entry List
            HashMap<String, List<Entry>> tableData = tableController.getTable_content(data);

            writer.write("<datatable\n");
            for(String sample_id : tableData.keySet()){
                writer.write("\tkey\t " + sample_id + "\n");

                // write column names
                writer.write("\t\t##ID\t" );
                for(Entry e : tableData.get(sample_id)){
                    if(!e.getIdentifier().equals("ID"))
                        writer.write(e.getIdentifier() + "\t" );
                }
                writer.write("\n");

                // write column types
                writer.write("\t\t#String\t" );
                for(Entry e : tableData.get(sample_id)) {
                    if(!e.getIdentifier().equals("ID"))
                        writer.write(e.getType().getTypeInformation() + "\t" );

                }
                writer.write("\n");

                // write data
                writer.write("\t\t");
                writer.write(sample_id + "\t");
                for(Entry e : tableData.get(sample_id)){
                    if(!e.getIdentifier().equals("ID"))
                        writer.write(e.getData().getTableInformation() + "\t" );
                }
                writer.write("\n");
            }

            writer.write(">\n");

            if(user_defined_hg_list!=null){
                writer.write("<haplogroupList\n");
                writer.write("\t");
                for(String hg : user_defined_hg_list){
                    writer.write(hg + ",");
                }

                writer.write("\n>\n");

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }

    }

}
