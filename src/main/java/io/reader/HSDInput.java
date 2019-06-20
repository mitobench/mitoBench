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

        String currline;
        boolean init = true;
        String line = bfr.readLine();
        String[] header = line.replace("\"", "").split("\t");


        int polys_not_found_index = -1;
        int polys_found_index = -1;
        int polys_remaining_index = -1;
        int acc_in_remainings_index = -1;
        int input_sample_index = -1;

        int id_index = -1;
        int group_index = -1;
        int quality_index = -1;

        for (int i = 0; i < header.length; i++){
            switch (header[i].replace("\"", "")) {
                case "SampleID":  id_index = i;
                    break;
                case "Haplogroup":  group_index = i;
                    break;
                //case "Polymorphisms":  polys_found_index = i;
                //    break;
                case "Found_Polys":  polys_found_index = i;
                    break;
                case "Quality":  quality_index = i;
                    break;
                case "Not_Found_Polys":  polys_not_found_index = i;
                    break;
                case "Remaining_Polys":  polys_remaining_index = i;
                    break;
                case "AAC_In_Remainings":  acc_in_remainings_index = i;
                    break;
                case "Input_Sample":  input_sample_index = i;
                    break;

            }
        }


        while ((currline = bfr.readLine()) != null) {
            if(!header[0].replace("\"","").equals("SampleID") && init){
                if(init){
                    throw new HSDException("This is probably not a HSD input format file, as the header is missing.");
                }
            } else {

                String[] splitGroup = currline.replace("\"", "").split("\t");

                String polys_not_found = "";
                String polys_found = "";
                String polys_remaining = "";
                String acc_in_remainings = "";
                String input_sample = "";
                String id = "";
                String group = "";
                String quality = "";

                if(id_index!=-1){
                    id = splitGroup[id_index].trim();
                    if(id.matches(".*[^\\d]\\d{1}$")){
                        id = id.split("\\.")[0];
                    }
                }
                if(group_index!=-1){
                    group = splitGroup[group_index].trim();
                }
                if(quality_index!=-1){
                    double num = Double.parseDouble(splitGroup[quality_index]);
                    quality = round(num*100,2);
                }
                if(polys_not_found_index!=-1) {
                    polys_not_found = splitGroup[polys_not_found_index].trim();
                }
                if(polys_found_index!=-1){
                    polys_found = splitGroup[polys_found_index].trim();
                }
                if(polys_remaining_index!=-1) {
                    polys_remaining = splitGroup[polys_remaining_index].trim();
                }
                if(acc_in_remainings_index!=-1) {
                    acc_in_remainings = splitGroup[acc_in_remainings_index].trim();
                }
                if(input_sample_index!=-1) {
                    input_sample = splitGroup[input_sample_index].trim();
                }


                List<Entry> entries = new ArrayList<>();
                Entry entry = new Entry(mapper.mapString("Haplogroup"), new CategoricInputType("String"), new GenericInputData(group));
                if(!polys_found.equals("")){
                    Entry entry2 = new Entry(mapper.mapString("Haplotype"), new CategoricInputType("String"), new GenericInputData(polys_found));
                    entries.add(entry2);
                }
                if(!quality.equals("")){
                    Entry entry_HGqual = new Entry("Haplogrep Quality", new CategoricInputType("String"), new GenericInputData(quality));
                    entries.add(entry_HGqual);
                }


                entries.add(entry);

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
    public String round(double value, int numberOfDigitsAfterDecimalPoint) {
        if(Double.isNaN(value))
            return "Nan";
        else if (Double.isInfinite(value))
            return "Inf";
        else {
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                    BigDecimal.ROUND_HALF_EVEN);
            return bigDecimal.doubleValue()+"%";
        }

    }



    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return map;
    }
}
