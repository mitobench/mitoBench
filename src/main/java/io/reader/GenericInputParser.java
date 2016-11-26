package io.reader;

import io.IInputData;
import io.datastructure.Entry;

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


    public GenericInputParser(String file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        String[] headergroup = null;
        String[] headertype = null;

        String currline = "";
        int count = 0;

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

                for (int i = 0; i < splitLine.length; i++) {
                    Entry e = new Entry(headergroup[i], headertype[i], splitLine[i]);
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
