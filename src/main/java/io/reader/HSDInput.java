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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This method is intended to parse HSD format files, e.g. output from HaploGrep v1/v2 correctly.
 * The entries are stored in Entries classes, thus being stored in lists.
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
        boolean init = true;

        while ((currline = bfr.readLine()) != null) {
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
                String quality = round(Double.parseDouble(splitGroup[3])*100,2) + "%";
                String polys_not_found = splitGroup[4];
                String polys_found = splitGroup[5];
                String polys_remaining = splitGroup[6];
                String acc_in_remainings = splitGroup[7];
                String input_sample = splitGroup[8];

                Entry entry = new Entry(mapper.mapString("Haplogroup"), new CategoricInputType("String"), new GenericInputData(group));
                Entry entry2 = new Entry(mapper.mapString("Haplotype"), new CategoricInputType("String"), new GenericInputData(polys_found));
                Entry entry_HGqual = new Entry("Haplogrep Quality", new CategoricInputType("String"), new GenericInputData(quality));
                List<Entry> entries = new ArrayList<>();
                entries.add(entry);
                entries.add(entry2);
                entries.add(entry_HGqual);
                map.put(id, entries);
            }
        }
    }


    /**
     * This method rounds a double value on n digits.
     * @param value
     * @param numberOfDigitsAfterDecimalPoint
     * @return double rounded
     */
    public double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal.doubleValue();
    }



    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
