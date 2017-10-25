package io.reader;

import database.ColumnNameMapper;
import io.Exceptions.HSDException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.apache.log4j.Logger;

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

    public HSDInput(String filetoParse, Logger LOG) throws IOException, HSDException {
        LOG.info("Read HSD file: " + filetoParse);
        FileReader fr = new FileReader(new File(filetoParse));
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();
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
                String type = getHaplotypes(splitGroup);
                Entry entry = new Entry(mapper.mapString("Haplogroup"), new CategoricInputType("String"), new GenericInputData(group));
                Entry entry2 = new Entry(mapper.mapString("Haplotype"), new CategoricInputType("String"), new GenericInputData(type));
                List<Entry> entries = new ArrayList<>();
                entries.add(entry);
                entries.add(entry2);
                map.put(id, entries);
            }
        }
    }


    private String getHaplotypes(String[] splitGroup){
        String types = "";
        for(int i = 3; i < splitGroup.length; i++){
            if(i<splitGroup.length-1){
                types += splitGroup[i]+",";
            } else {
                types += splitGroup[i];
            }
        }
        return types;

    }


    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
