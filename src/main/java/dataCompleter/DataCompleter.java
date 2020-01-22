package dataCompleter;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataCompleter {


    private String outfile;

    public void run(String data_template_filepath, String data_fasta_filepath, String outfolder) throws IOException {

        String[] fileName = data_fasta_filepath.replaceFirst("[.][^.]+$", "").split("/");
        String fileNameWithoutExt = fileName[fileName.length-1];

        outfile = outfolder + java.time.LocalDateTime.now() + "_" + fileNameWithoutExt + "_" + "completed.csv";
        BufferedWriter data_meta_file_updated = new BufferedWriter(new FileWriter(outfile));

        MetaInfoReader metaInfoReader = new MetaInfoReader(data_template_filepath);
        metaInfoReader.read();
        String[] types = metaInfoReader.getTypes_list();
        String[] header = metaInfoReader.getHeader_list();

        FastaReader fastaReader = new FastaReader(data_fasta_filepath);
        fastaReader.parseFasta();

        boolean isheaderWritter = false;

        // calculate haplogroups
        HaplogrepCaller haplogrepCaller = new HaplogrepCaller();
        haplogrepCaller.call(data_fasta_filepath);

        HSDParser hsdParser = new HSDParser();
        HashMap<String, ArrayList<String>> entryList = null;

        try {
            hsdParser.parseFile("haplogroups.hsd");
            entryList = hsdParser.getEntryList();
        } catch (Exception e) {
            System.out.println("'haplogroups.hsd' could not be read.");
            System.out.println(e.toString());
            System.exit(0);
        }

        haplogrepCaller.deleteTmpFiles();

        // complete geographic locations based on already given information
        LocationCompleter locationCompleter = new LocationCompleter();
        locationCompleter.setHeader(header);
        locationCompleter.setIndexes();

        Statistics calculator = new Statistics();

        ArrayList<String> entries = metaInfoReader.getEntry_list();

        for (String entry : entries) {

            String[] meta_info = entry.split(",", types.length);
            String accessionID_with_version = meta_info[metaInfoReader.getIndexOfArrtibute("##accession_id")].replace("\"","");
            String accessionID = accessionID_with_version.split("\\.")[0].trim();
            String sequence = fastaReader.getSequenceMap().get(accessionID);

            double percentageOfN = calculator.calculatePercentageOfN(sequence);

            // determine user alias
            String user_alias = "";
            int index_firstname = metaInfoReader.getIndexOfArrtibute("user_firstname");
            if(index_firstname != -1){
                user_alias  = meta_info[metaInfoReader.getIndexOfArrtibute("user_firstname")].trim() + "" + meta_info[metaInfoReader.getIndexOfArrtibute("user_surname")].trim();
            }

            // fill HaploGrep2 results
            String haplogroup="NULL";
            String haplotype="NULL";
            String quality="NULL";

            if(entryList == null){
                haplogroup = "NULL";
                haplotype = "NULL";
                quality = "NULL";
            } else {
                try {
                    haplogroup = entryList.get(accessionID).get(0).replace("'", "");
                    haplotype = entryList.get(accessionID).get(3);
                    quality = entryList.get(accessionID).get(1);
                } catch (Exception e) {
                    System.out.println("Sequence with accession id "+ accessionID + " not contained in Haplogrep2 result file");
                }
            }

            // complete geographic information
            locationCompleter.setEntry(entry.split(",", types.length));

            String[] entry_completed = locationCompleter.getCompletedInformation();
            meta_info = entry_completed;
            String meta_info_parsed = "";

            for (int i = 0; i < types.length; i++) {

                String type = types[i].replace("#", "").trim();
                String info;
                if( meta_info[i] == null){
                    info = "NULL";
                } else {
                    info = meta_info[i].replace("\"", "").trim();
                }

                if(info == null) {
                    meta_info_parsed += "NULL,";
                } else if (info.equals("NULL")) {
                    meta_info_parsed += "NULL,";
                }else if (info.equals("")) {
                    meta_info_parsed += "NULL,";
                } else if (info.contains("'")) {
                    String hg_tmp = info.replace("'", "");
                    meta_info_parsed += "'" + hg_tmp + "',";
                } else if (type.equals("String")) {
                    meta_info_parsed += "'" + info + "',";
                } else {
                    meta_info_parsed += info + ",";
                }
            }
            if (meta_info_parsed.endsWith(",")) {
                meta_info_parsed = meta_info_parsed.substring(0, meta_info_parsed.length() - 1);
            }


            // write new header
            if (!isheaderWritter) {
                metaInfoReader.addToHeader(",percentage_n,user_alias,haplogroup_current_versions,macro_haplogroup,haplotype_current_versions,quality_haplotype_current_version, mt_sequence");
                metaInfoReader.addTotypes(",real,String,String,String,String,real,String");
                data_meta_file_updated.write(metaInfoReader.getHeader());
                data_meta_file_updated.newLine();
                data_meta_file_updated.write(metaInfoReader.getTypes());
                data_meta_file_updated.newLine();
                isheaderWritter = true;
            }

            String macro =  setMacrogroup(haplogroup);


            // write new meta data entry
            String values = meta_info_parsed + "," + percentageOfN + ",'" + user_alias + "','" + haplogroup + "','" +
                    macro + "," + haplotype + "'," + quality + ",'" + fastaReader.getSequenceMap().get(accessionID) + "'";

            values = values.replace("'NULL'", "NULL");
            values = values.replace("NULL", "");
            values = values.replace("'\"", "'");
            values = values.replace("\"'", "'");
            values = values.replace("\"", "");
            values = values.replace("'", "");

            data_meta_file_updated.write(values);
            data_meta_file_updated.newLine();
            //System.out.println(values);
            //System.out.println("");
        }

        data_meta_file_updated.close();
    }

    private String setMacrogroup(String haplogroup){

        if(haplogroup.startsWith("L0")){
            return "L0";
        } else if(haplogroup.startsWith("L1")){
            return "L1";
        } else if(haplogroup.startsWith("L2")){
            return "L2";
        } else if(haplogroup.startsWith("L3")){
            return "L3";
        } else if(haplogroup.startsWith("L4")){
            return "L4";
        } else if(haplogroup.startsWith("L5")){
            return "L5";
        } else if(haplogroup.startsWith("L6")){
            return "L6";
        } else if(haplogroup.startsWith("M7")){
            return "M7";
        } else if(haplogroup.startsWith("M8")){
            return "M8";
        } else if(haplogroup.startsWith("M9")){
            return "M9";
        } else if(haplogroup.startsWith("M")){
            return "M";
        } else if(haplogroup.startsWith("N1")){
            return "N1";
        } else if(haplogroup.startsWith("N2")){
            return "N2";
        } else if(haplogroup.startsWith("N9")){
            return "N9";
        } else if(haplogroup.startsWith("N")){
            return "N";
        } else if(haplogroup.startsWith("G")){
            return "G";
        } else if(haplogroup.startsWith("D")){
            return "D";
        } else if(haplogroup.startsWith("A")){
            return "A";
        } else if(haplogroup.startsWith("X")){
            return "X";
        } else if(haplogroup.startsWith("Q")){
            return "Q";
        } else if(haplogroup.startsWith("C")){
            return "C";
        } else if(haplogroup.startsWith("Z")){
            return "Z";
        } else if(haplogroup.startsWith("E")){
            return "E";
        } else if(haplogroup.startsWith("O")){
            return "O";
        } else if(haplogroup.startsWith("S")){
            return "S";
        } else if(haplogroup.startsWith("I")){
            return "I";
        } else if(haplogroup.startsWith("W")){
            return "W";
        } else if(haplogroup.startsWith("Y")){
            return "Y";
        } else if(haplogroup.startsWith("R0")){
            return "R0";
        } else if(haplogroup.startsWith("R9")){
            return "R9";
        } else if(haplogroup.startsWith("R")){
            return "R";
        } else if(haplogroup.startsWith("P")){
            return "P";
        } else if(haplogroup.startsWith("J") || haplogroup.startsWith("T")){
            return "JT";
        } else if(haplogroup.startsWith("HV")){
            return "HV";
        } else if(haplogroup.startsWith("H")){
            return "H";
        } else if(haplogroup.startsWith("V")){
            return "V";
        } else if(haplogroup.startsWith("F")){
            return "F";
        } else if(haplogroup.startsWith("B")){
            return "B";
        } else if(haplogroup.startsWith("U")){
            return "U";
        } else if(haplogroup.startsWith("K")){
            return "K";
        }  else {
            return haplogroup;
        }


    }

    public String getOutfile() {
        return outfile;
    }

}
