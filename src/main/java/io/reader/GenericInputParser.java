package io.reader;

import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
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


    public GenericInputParser(String file, Logger LOG) throws IOException {
        LOG.info("Read generic file: " + file);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        String[] headergroup = null;
        String[] headertype = null;

        String currline;

        while ((currline = bfr.readLine()) != null) {
            //Parse header, two line header !!
            if (currline.startsWith("##")) {
                headergroup = currline.replace("##","").split("\t");
                continue;
            } else if (currline.startsWith("#")) {
                headertype = currline.replace("#","").split("\t");
                continue;
            } else {
                String[] splitLine = currline.split("\t");
                //Assume ID is always first! -> requirement
                List<Entry> entries = new ArrayList<>();
                //we need to take care of our different types here now, too
                for (int i = 0; i < splitLine.length; i++) {
                    String headerType = headertype[i];
                    Entry e = null;
                    if (headerType.equals("C14")) {
                        e = new Entry(headergroup[i], new RadioCarbonInputType(headerType), new RadioCarbonData(splitLine[i], RadioCarbonData.PARSE_C14_DATE_INFORMATION));
                    } else {
                        e = new Entry(headergroup[i], new CategoricInputType(headerType), new GenericInputData(splitLine[i]));
                    }

                    entries.add(e);
                }
                //Now add with ID to hashmap
                map.put(splitLine[0], entries);
            }
        }
    }


    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
