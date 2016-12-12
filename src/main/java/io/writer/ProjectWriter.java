package io.writer;

import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.RadioCarbonInputType;
import view.groups.Group;
import view.groups.GroupController;
import view.table.TableController;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.apache.poi.hssf.usermodel.HeaderFooter.date;

/**
 * Created by neukamm on 09.12.2016.
 */
public class ProjectWriter {


    public ProjectWriter(){};


    public void write(File outfile, TableController tableController) throws IOException, ProjectException {
        GroupController groupController = tableController.getGroupController();
        Date date = new Date();
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outfile));

            // write header
            String header = "# This file contains all information of a MitoBench project and was created on  "+ date.toString()
                    + "\n# Please do NOT change this file.\n\n";
            writer.write(header);
            // write all data table information as Entry List
            //HashMap<String, String[]> dataTable = tableController.getDataTable().getDataTable();
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

                // write data types
               // writer.write("\t\t");
               // writer.write("#String\t");
                //for(Entry e : tableData.get(sample_id)){
                //    if(!e.getIdentifier().equals("ID"))
                //        writer.write(e.getData() + "\t" );
                //}
                //writer.write("\n");


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



            // write group information

//            HashMap<String, Group> allGroups = groupController.getAllGroups();
//            if(allGroups.size() != 0){
//                writer.write("<grouping\n");
//                for(String group_key : allGroups.keySet()){
//                    writer.write("\t"+group_key+"\n");
//                    for(Object member : allGroups.get(group_key).getEntries()){
//                        writer.write("\t\t"+ member.toString() + "\n");
//                    }
//                }
//
//                writer.write(">");
//            }




        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }

    }
}
