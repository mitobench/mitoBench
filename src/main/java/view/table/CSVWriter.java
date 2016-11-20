package view.table;

import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * Created by neukamm on 07.11.16.
 */
public class CSVWriter {

    private ObservableList<TableDataModel> data;

    public CSVWriter(ObservableList<TableDataModel> data){
        this.data = data;
    }


    public void writeExcel(String file) throws Exception {
//        Writer writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter(new File(file+".csv")));
//            for (TableDataModel entry : data) {
//                // todo: should be changed ! Iterating over attributes instead....
//                String text = entry.getID() + ";" + entry.getMTsequence() + ";"
//                        + entry.getDating() + entry.getHaplogroup() + "\n";
//                writer.write(text);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        finally {
//
//            writer.flush();
//            writer.close();
//        }
    }




}
