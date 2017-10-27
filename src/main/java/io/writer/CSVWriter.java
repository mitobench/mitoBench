package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import controller.TableControllerUserBench;

import java.io.*;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class CSVWriter implements IOutputData {

    private ObservableList<ObservableList> data;
    private TableControllerUserBench tableController;
    private String separator = ",";

    public CSVWriter(TableControllerUserBench tableController, Logger LOG, ObservableList<ObservableList> dataToExport){
        this.data = dataToExport;
        this.tableController = tableController;

    }


    /**
     * This method writes the current content of the tableview to a csv file incl. header line
     * (comma separated)
     *
     * @param file
     * @param tableController
     * @throws Exception
     */
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        Writer writer = null;
        try {
            if(!file.endsWith(".csv"))
                file = file + ".csv";

            int index_id = tableController.getColIndex("ID");
            int index_mt = tableController.getColIndex("MTSequence");

            writer = new BufferedWriter(new FileWriter(new File(file)));

            // write header
            String header = "";
            List<String> columns = this.tableController.getCurrentColumnNames();
            for (int i = 0; i < columns.size(); i++){
                if(i == columns.size()-1){
                    header += columns.get(i) + "\n";
                } else {
                    header += columns.get(i) + separator;
                }
            }
            writer.write(header);

            // write view.data
            for (ObservableList entry :  this.data) {
                String text = "";
                for(int i = 0; i < entry.size(); i++){
                    if(i==index_mt){
                        String mt_seq = tableController.getDataTable().getMtStorage().getData().get(entry.get(index_id));
                        if(i == entry.size()-1){
                            text += mt_seq + "\n";
                        } else {
                            text += mt_seq + separator;
                        }

                    } else {
                        if(i == entry.size()-1){
                            text += entry.get(i) + "\n";
                        } else {
                            text += entry.get(i) + separator;
                        }
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
