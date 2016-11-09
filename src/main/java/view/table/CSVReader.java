package view.table;

import java.io.*;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class CSVReader {


    public void populateTable(
            final TableManager tableManager,
            final File input, final boolean hasHeader) {

        TableView<ObservableList<StringProperty>> table = tableManager.getTable();
        // todo: check if table should be cleaned before adding new entries
        //table.getItems().clear();
        //table.getColumns().clear();
        table.setPlaceholder(new Label("Loading..."));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                BufferedReader in =  new BufferedReader(new FileReader(input));
                // Header line
                if (hasHeader) {
                    final String headerLine = in.readLine();
                    final String[] headerValues = headerLine.split(";");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int column = 0; column < headerValues.length; column++) {
                                tableManager.addColumn(tableManager.getCol_names().get(column));
                            }
                        }
                    });
                }

                // Data:
                String dataLine;
                while ((dataLine = in.readLine()) != null) {
                    final String[] dataValues = dataLine.split(";");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Add additional columns if necessary:
                            for (int columnIndex = table.getColumns().size(); columnIndex < dataValues.length; columnIndex++) {
                                tableManager.addColumn(tableManager.getCol_names().get(columnIndex));
                            }
                            // Add data to table:
                            String[] vals = new String[1];
                            vals[0] = tableManager.getId_intern()+"";
                            String[] both = Stream.of(vals, dataValues).flatMap(Stream::of)
                                    .toArray(String[]::new);
                            TableDataModel entry = new TableDataModel(both); // data values
                            tableManager.addEntry(entry);


                        }
                    });
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


}