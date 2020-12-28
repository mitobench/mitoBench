package io.reader;

import database.ColumnNameMapper;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.datastructure.location.LocationData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.LocationInputType;
import io.inputtypes.RadioCarbonInputType;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



/**
 * Created by peltzer on 17/11/2016.
 */
public class GenericInputParser implements IInputData {

    private String[] headertype;
    private String[] headergroup;
    private HashMap<String, List<Entry>> map = new HashMap<>();
    private HashMap<String, List<Entry>> list_duplicates;

    public GenericInputParser(String file, Logger LOG, String delimiter, Set<String> message_duplicates) throws IOException {
        LOG.info("Read generic file: " + file);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();
        headergroup = null;
        headertype = null;
        list_duplicates = new HashMap<>();

        String currline;

        while ((currline = bfr.readLine()) != null) {
            //Parse header, two line header !!
            if (currline.startsWith("##")) {
                headergroup = currline.replace("##","").split(delimiter);
                for(int i = 0; i < headergroup.length; i++){
                    String s = headergroup[i].replace("\t","");
                    if(s.contains("(Grouping)"))
                        s = s.replace("(Grouping)", "");
                    s.trim();
                    headergroup[i] = s;
                }

                continue;
            } else if (currline.startsWith("#")) {
                headertype = currline.replace("#","").split(delimiter);
                for(String s : headertype){
                    s.trim().replace(delimiter,"");
                }

                continue;
            } else {
                String[] splitLine = currline.split(delimiter, -1);
                for (int i = 0; i < splitLine.length; i++) {
                    splitLine[i].trim();
                    if (splitLine[i].equals("")) {
                        splitLine[i] = "";
                    }
                }

                if(splitLine.length != headergroup.length || splitLine.length != headertype.length){
                    System.err.println("Entry and headergroups/-types have unequal length.");
                } else {
                    //Assume ID is always first! -> requirement
                    List<Entry> entries = new ArrayList<>();
                    //we need to take care of our different types here now, too
                    for (int i = 0; i < splitLine.length; i++) {
                        String headerType = headertype[i];
                        Entry e = null;
                        if (headerType.equals("C14")) {
                            e = new Entry(
                                    mapper.mapString(headergroup[i]),
                                    new RadioCarbonInputType(headerType),
                                    new RadioCarbonData(splitLine[i], RadioCarbonData.PARSE_C14_DATE_INFORMATION)
                            );
                        } else if (headerType.endsWith("Location")) {
                            e = new Entry(
                                    mapper.mapString(headergroup[i]),
                                    new LocationInputType(headerType),
                                    new LocationData(splitLine[i], LocationData.PARSE_LOCATION_INFORMATION)
                            );
                        } else {
                            if (!mapper.mapString(headergroup[i]).equals("ID")) {
                                e = new Entry(
                                        mapper.mapString(headergroup[i]),
                                        new CategoricInputType(headerType),
                                        new GenericInputData(splitLine[i])
                                );
                            }
                        }

                        if (e != null)
                            entries.add(e);
                    }
                    //Now add with ID to hashmap
                    String id = splitLine[0].split(" ")[0].trim();
                    if (id.matches(".*[^\\d]\\d{1}$")) {
                        id = id.split("\\.")[0];
                    }

                    // Duplicates within input file are not allowed!
                    if(map.keySet().contains(id)){
                        String id_plus = id + "_" + Math.random();
                        this.list_duplicates.put(id_plus, entries);
                        this.list_duplicates.put(id, map.get(id));
                        map.remove(id);
                        //DuplicatesException duplicatesException = new DuplicatesException("The input file contains duplicates: " + id +
                        //        "\nOnly first hit will be added");
                        //DuplicatesErrorDialogue duplicatesErrorDialogue = new DuplicatesErrorDialogue(duplicatesException);
                    } else {
                        map.put(id , entries);
                    }
                }



            }
        }
    }


    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }

    public String[] getTypes() {
        return headertype;
    }

    public String[] getHeader() {
        return headergroup;
    }


    public HashMap<String, List<Entry>> getList_duplicates() { return list_duplicates; }
}
