package io.reader;

import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.RadioCarbonInputType;
import view.table.TableController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 09.12.2016.
 */
public class ProjectReader {


    private HashMap<String, List<Entry>> datatable;

    public ProjectReader(){}

    public void read(File infile) throws IOException, ProjectException {
        FileReader fr = new FileReader(infile);
        BufferedReader bfr = new BufferedReader(fr);
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
                                // create right data type
                                Entry entry;
                                if (headertype.equals("C14")) {
                                    entry = new Entry(headergroup[i].trim(), new RadioCarbonInputType(headertype[i].trim()), new RadioCarbonData(entries[i].trim(), RadioCarbonData.PARSE_C14_DATE_INFORMATION));
                                } else {
                                    entry = new Entry(headergroup[i].trim(), new CategoricInputType(headertype[i].trim()), new GenericInputData(entries[i].trim()));
                                }

                                all_entries.add(entry);


                            }
                            datatable.put(key, all_entries);

                        }
                    }
                }
            }
        }
    }



    public void loadData(TableController tableController){

        tableController.updateTable(datatable);
        tableController.loadGroups();

    }

}
