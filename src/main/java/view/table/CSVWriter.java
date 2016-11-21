package view.table;

import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created by neukamm on 07.11.16.
 */
public class CSVWriter {

    private ObservableList<ObservableList> data;
    private TableController tableController;
    private String separator = ",";

    public CSVWriter(TableController tableController){
        this.data = tableController.getData();
        this.tableController = tableController;
    }


    /**
     * This method write the current content of the tableview to a csv file incl. header line
     * (comma separated)
     *
     * @param file
     * @throws Exception
     */
    public void writeExcel(String file) throws Exception {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(file+".csv")));

            // write header
            String header = "";
            List<String> columns = tableController.getCol_names();
            for (int i = 0; i < columns.size(); i++){
                if(i == columns.size()-1){
                    header += columns.get(i) + "\n";
                } else {
                    header += columns.get(i) + separator;
                }
            }
            writer.write(header);

            // write data
            for (ObservableList entry : data) {
                String text = "";
                for(int i = 0; i < entry.size(); i++){
                    if(i == entry.size()-1){
                        text += (String)entry.get(i) + "\n";
                    } else {
                        text += (String)entry.get(i) + separator;
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




}
