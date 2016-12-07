package io.reader;

import io.Exceptions.HSDException;
import io.IInputData;
import io.datastructure.Entry;
import io.inputtypes.CategoricInputType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public HSDInput(String filetoParse) throws IOException, HSDException {
        FileReader fr = new FileReader(new File(filetoParse));
        BufferedReader bfr = new BufferedReader(fr);
        map = new HashMap<>();

        String currline = "";
        int line_counter = 0;
        boolean init = true;

        while ((currline = bfr.readLine()) != null) {
            line_counter++;
            if(currline.startsWith("SampleID") && init){
                init = false;
                continue; //Skip header
            } else {
                if(init){
                    throw new HSDException("This is probably not a HSD input format file, as the header is missing.");
                }
                String[] splitGroup = currline.split("\t");

                String id = splitGroup[0];
                String group = splitGroup[2];
                Entry entry = new Entry("Haplogroup", new CategoricInputType("String"), group);
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
