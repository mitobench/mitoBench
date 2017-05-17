package io.writer;

import
        io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.*;

/**
 * Created by neukamm on 17.01.17.
 */
public class StatisticsWriter implements IOutputData{

    private TableView table;
    private ObservableList<ObservableList<String>> content;


    public StatisticsWriter(Tab tab) {
        table = (TableView) tab.getContent();
        content = table.getItems();

    }

    /**
     * This method writes the statistics to csv file.
     * @throws IOException
     */
    @Override
    public void writeData(String path) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(path).getAbsoluteFile());
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(outputStream, "UTF-8");

        // write column names as first line
        for(Object col : table.getColumns()){
            TableColumn column = (TableColumn) col;
            writerOutputStream.write(column.getText()+",");
        }
        writerOutputStream.write("\n");
        for(ObservableList row : content){
            for(Object cell : row){
                writerOutputStream.write(cell + ",");
            }
            writerOutputStream.write("\n");
        }

        writerOutputStream.close();

    }


    @Override
    public void setGroups(String groupID) {
        //Do nothing here, not required for this format at all
    }
}
