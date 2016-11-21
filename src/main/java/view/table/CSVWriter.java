package view.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
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


    public void writeExcel(String file) throws Exception {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(file+".csv")));

            // write header
            String header = "";
            List<Object> columns = tableController.getTable().getColumns();
            for (int i = 0; i < columns.size(); i++){
                TableColumn c = (TableColumn) columns.get(i);
                if(i == columns.size()-1){
                    header += c.getText() + "\n";
                } else {
                    header += c.getText() + separator;
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
