package io.reader;

import database.ColumnNameMapper;
import io.Exceptions.DuplicatesException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.datastructure.location.LocationData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.LocationInputType;
import io.inputtypes.RadioCarbonInputType;
import org.apache.log4j.Logger;
import view.dialogues.error.DuplicatesErrorDialogue;

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

    public GenericInputParser(String file, Logger LOG, String delimiter) throws IOException {
        LOG.info("Read generic file: " + file);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();
        headergroup = null;
        headertype = null;

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
                    s.trim().replace("\t","");
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
                    DuplicatesException duplicatesException = new DuplicatesException("The input file contains duplicates: " + id +
                            "\nOnly first hit will be added");
                    DuplicatesErrorDialogue duplicatesErrorDialogue = new DuplicatesErrorDialogue(duplicatesException);
                } else {
                    map.put(id , entries);
                }

            }
        }
    }

//    private boolean isUpdateNeeded(HashMap<String, List<Entry>> map, List<Entry> e_new, String id) {
//        Set<String> keyset = map.keySet();
//        List<List<Entry>> list_identical_ids = new ArrayList<List<Entry>>();
//        for(String key : keyset){
//            String key_stripped = key.split("_")[0];
//            if(key_stripped.equals(id)){
//                // collect all entries with this accession id
//                list_identical_ids.add(map.get(key));
//            }
//        }
//
//        // iterate over list and check if update new entry already exists
//        for(List<Entry> e : list_identical_ids){
//            if(entryIsIdentical(e, e_new)){
//                return false;
//            }
//        }
//            return true;
//    }
//
//    private boolean entryIsIdentical(List<Entry> es_new, List<Entry> es_old) {
//
//        for (int i = 0; i < es_old.size(); i++){
//            Entry e_old = es_old.get(i);
//            Entry e_new = es_new.get(i);
//            if(!e_old.getIdentifier().equals(e_new.getIdentifier()) ||
//                    !e_old.getType().equals(e_new.getType()) ||
//                    !e_old.getData().getTableInformation().toLowerCase().equals(e_new.getData().getTableInformation().toLowerCase())){
//                return false;
//            }
//        }
//        return true;
//    }


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
}
