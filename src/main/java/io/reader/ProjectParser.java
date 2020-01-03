package io.reader;

import controller.ChartController;
import database.ColumnNameMapper;
import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.datastructure.location.LocationData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.LocationInputType;
import io.inputtypes.RadioCarbonInputType;
import org.apache.log4j.Logger;
import controller.ATableController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 09.12.2016.
 */
public class ProjectParser {


    private HashMap<String, List<Entry>> datatable;
    private String[] hgs_user_defined;

    public ProjectParser(){}

    public void read(File infile, Logger LOG) throws IOException, ProjectException {
        LOG.info("Read project file: " + infile);
        FileReader fr = new FileReader(infile);
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();
        datatable = new HashMap<>();
        String currline;
        String[] headergroup = null;
        String[] headertype = null;
        String[] entries;
        String key = null;
        boolean init = true;

        while ((currline = bfr.readLine()) != null) {
            if(init) {
                init = false;
                //check if format is indeed ARP format
                if (currline.startsWith("#")) {
                } else {
                    throw new ProjectException("This is not in .mitoproj Format!");
                }

            } else {
                // read data table
                if (currline.startsWith("<datatable")) {

                    while (!(currline = bfr.readLine()).equals(">")) {

                        if (currline.startsWith("\tkey\t")) {
                            // parse one entry
                            key = currline.replace("\tkey\t", "").trim();
                        } else if (currline.startsWith("\t\t##")) {
                            headergroup = currline.replace("\t\t##", "").split("\t");
                        } else if (currline.startsWith("\t\t#")) {
                            headertype = currline.replace("\t\t#", "").split("\t");
                        } else if (currline.startsWith("\t\t")) {
                            entries = currline.replace("\t\t", "").split("\t");

                            List<Entry> all_entries = new ArrayList<>();
                            for (int i = 0; i < headergroup.length; i++) {
                                // create correct data type
                                Entry entry;
                                if (headertype.equals("C14")) {
                                    entry = new Entry(mapper.mapString(headergroup[i].trim()), new RadioCarbonInputType(headertype[i].trim()), new RadioCarbonData(entries[i].trim(), RadioCarbonData.PARSE_C14_DATE_INFORMATION));
                                }  else if(headertype.equals("Location")) {
                                    entry = new Entry(mapper.mapString(headergroup[i]), new LocationInputType(headertype[i].trim()), new LocationData(entries[i].trim(), LocationData.PARSE_LOCATION_INFORMATION));
                                } else {
                                    entry = new Entry(mapper.mapString(headergroup[i].trim()), new CategoricInputType(headertype[i].trim()), new GenericInputData(entries[i].trim()));
                                }
                                all_entries.add(entry);
                            }
                            datatable.put(key, all_entries);
                        }
                    }
                } else if (currline.startsWith("<haplogroupList")){
                    while (!(currline = bfr.readLine()).equals(">")) {
                        currline = currline.substring(1);
                        hgs_user_defined = currline.split(",");
                    }
                }
            }
        }
    }



    public void loadData(ATableController tableController, ChartController chartController){

        tableController.cleanVersions();
        tableController.updateTable(datatable);
        tableController.loadGroups();
        chartController.setCustomHGList(hgs_user_defined);

    }

}
