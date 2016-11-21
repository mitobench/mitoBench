package io.reader;

import io.IInputData;
import io.datastructure.Entry;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This method is intended to parse HSD format files, e.g. output from HaploGrep v1/v2 correctly. The entries are stored in Entries classes, thus being stored in lists.
 *
 * Created by peltzer on 17/11/2016.
 */
public class HSDInput implements IInputData {
    private HashMap<String, List<Entry>> map;

    public HSDInput(String filetoParse) throws IOException {
        FileReader fr = new FileReader(new File(filetoParse));
        BufferedReader bfr = new BufferedReader(fr);
        map = new HashMap<>();

        String currline = "";
        while ((currline = bfr.readLine()) != null) {
            if(currline.startsWith("SampleID")){
                continue; //Skip header
            } else {
                String[] splitGroup = currline.split("\t");
                String id = splitGroup[0];
                String group = splitGroup[2];
                Entry entry =  new Entry("Haplogroup", "String", group);
                List<Entry> entries = new ArrayList<>();
                entries.add(entry);
                map.put(id, entries);
            }
        }
    }



    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
