package io.writer;

import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import view.table.controller.TableControllerUserBench;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 09.12.2016.
 */
public class ProjectWriter {
    private String MITOBENCH_VERSION;


    public ProjectWriter(String mitoVersion){ MITOBENCH_VERSION = mitoVersion;}


    public void write(String outfile, TableControllerUserBench tableController) throws IOException, ProjectException {

        //Initialize properly
        if (!outfile.endsWith("mitoproj")) {
            outfile =outfile+ ".mitoproj";
        }

        Date date = new Date();
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outfile));

            // write header
            // This has been generated with MitoBench version XYZ, do not edit manually unless you know what you are doing.
            String header = "# This file has been generated with MitoBench version " + MITOBENCH_VERSION + " and contains all information of a MitoBench project\n# Created on  "+ date.toString()
                    + "\n# Please do NOT edit manually unless you know what you are doing.\n\n";
            writer.write(header);

            // write all data table information as Entry List
            HashMap<String, List<Entry>> tableData = tableController.getTable_content();

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


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }

    }
}
