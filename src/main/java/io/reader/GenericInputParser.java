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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peltzer on 17/11/2016.
 */
public class GenericInputParser implements IInputData {
    private HashMap<String, List<Entry>> map = new HashMap<>();


    public GenericInputParser(String file, Logger LOG, String delimiter) throws IOException {
        LOG.info("Read generic file: " + file);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();
        String[] headergroup = null;
        String[] headertype = null;

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
                String[] splitLine = currline.split(delimiter);
                for(int i = 0; i < splitLine.length; i++){
                    splitLine[i].trim();
                    if(splitLine[i].equals("")){
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
                    } else if(headerType.endsWith("Location")){
                        e = new Entry(
                                mapper.mapString(headergroup[i]),
                                new LocationInputType(headerType),
                                new LocationData(splitLine[i], LocationData.PARSE_LOCATION_INFORMATION)
                        );
                    }
                    else {
                        e = new Entry(
                                mapper.mapString(headergroup[i]),
                                new CategoricInputType(headerType),
                                new GenericInputData(splitLine[i])
                        );
                    }

                    entries.add(e);
                }
                //Now add with ID to hashmap
                String id = splitLine[0].trim();
                if(id.matches(".*[^\\d]\\d{1}$")){
                    id = id.split("\\.")[0];
                }
                map.put(id, entries);
            }
        }
    }


    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
