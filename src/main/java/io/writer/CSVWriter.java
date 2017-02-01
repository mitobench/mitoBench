package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import view.table.TableControllerUserBench;

import java.io.*;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class CSVWriter implements IOutputData {

    private ObservableList<ObservableList> data;
    private TableControllerUserBench tableController;
    private String separator = ",";

    public CSVWriter(TableControllerUserBench tableController){
        this.data = tableController.getData();
        this.tableController = tableController;
    }


    /**
     * This method writes the current content of the tableview to a csv file incl. header line
     * (comma separated)
     *
     * @param file
     * @throws Exception
     */
    public void writeData(String file) throws IOException {
        Writer writer = null;
        try {
            if(!file.endsWith(".csv"))
                file = file + ".csv";

            writer = new BufferedWriter(new FileWriter(new File(file)));

            // write header
            String header = "";
            List<String> columns = tableController.getCurrentColumnNames();
            for (int i = 0; i < columns.size(); i++){
                if(i == columns.size()-1){
                    header += columns.get(i) + "\n";
                } else {
                    header += columns.get(i) + separator;
                }
            }
            writer.write(header);

            // write view.data
            for (ObservableList entry :  tableController.getViewDataCurrent()) {
                String text = "";
                for(int i = 0; i < entry.size(); i++){
                    if(i == entry.size()-1){
                        text += entry.get(i) + "\n";
                    } else {
                        text += entry.get(i) + separator;
                    }

                }
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }
    }

    @Override
    public void setGroups(String groupID) {
        //Do nothing here, we dont care for CSV files atalll
    }


}
