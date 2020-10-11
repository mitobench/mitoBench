package dataCompleter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MetaInfoReader {



    private final String filepath;
    private String header;
    private String[] header_list;
    private String types;
    private String[] types_list;
    private ArrayList<String> entry_list = new ArrayList<>();



    public MetaInfoReader(String filepath){

        this.filepath = filepath;

    }

    public void read(){
        BufferedReader br;
        String delimiter = "\t";
        String line;

        try {
            br = new BufferedReader(new FileReader(filepath));

            while ((line = br.readLine())!=null){

                if (line.startsWith("##")){
                    header = line;
                    header_list = line.split(delimiter);

                } else if (line.startsWith("#")){
                    types = line;
                    types_list = line.split(delimiter);

                } else {
                    entry_list.add(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void addToHeader(String head) {
        this.header += head;

    }

    public void addTotypes(String type) {
        this.types += type;

    }

    public int getIndexOfArrtibute(String attr){
        return Arrays.asList(header_list).indexOf(attr);
    }

    public String getHeader() {
        return header;
    }

    public ArrayList<String> getEntry_list() {
        return entry_list;
    }

    public int getAccessionIDIndex(){
        return header.indexOf("##accession_id");
    }

    public int getUserSurnameIndex(){
        return header.indexOf("user_surname");
    }

    public int getUserFirstNameIndex(){
        int index = header.indexOf("user_firstname");
        return index;
    }

    public String getTypes() {
        return types;
    }

    public String[] getTypes_list() {
        return types_list;
    }

    public String[] getHeader_list() {
        return header_list;
    }


}
