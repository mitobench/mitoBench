package io.writer;


import controller.ATableController;
import controller.TableControllerUserBench;
import io.IOutputData;
import javafx.collections.ObservableList;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;


/**
 * Created by peltzer on 22/11/2016.
 */
public class ExcelWriter implements IOutputData {
    private final ObservableList<ObservableList> data;
    private TableControllerUserBench tableController;

    public ExcelWriter(TableControllerUserBench tableController, ObservableList<ObservableList> dataToExport) {

        this.tableController = tableController;
        this.data = dataToExport;
    }

    @Override
    public void writeData(String file, ATableController tableController) throws Exception {
        //Create file extension if its not there already...
        if(!file.endsWith("xlsx")) {
            file = file + ".xlsx";
        }

        // create wb and sheet

        try (OutputStream os = new FileOutputStream(file)){
            Workbook wb = new Workbook(os, "MyApplication", "1.0");
            Worksheet ws = wb.newWorksheet("Sheet 1");


            int index_id = tableController.getColIndex("ID");
            int index_mt = tableController.getColIndex("MTSequence");

            int row_index = 0;

            // write headers
            List<String> columns = this.tableController.getCurrentColumnNames();
            HashMap<String, String> types = this.tableController.getHeadertypes();

            for (int i = 0; i < columns.size(); i++) {
                String text = columns.get(i);
                String type = types.get(columns.get(i));
                if(text.endsWith("(Grouping)")){
                    text = text.split(" ")[0];
                }

                if (i==0) {
                    text = "##"+text;
                    type = "#"+type;
                }
                ws.value(row_index, i, text);
                ws.value(row_index+1, i, type);
            }

            row_index++;

            // write table content
            for (Object list_entry : data) {
                ObservableList e = (ObservableList) list_entry;
                row_index++;
                for (int i = 0; i < e.size(); i++) {
                    if(i==index_mt) {
                        String mt_seq = tableController.getDataTable().getMtStorage().getData().get(e.get(index_id));
                        ws.value(row_index,i,mt_seq);

                    }else{
                        if(e.get(i).equals(""))
                            ws.value(row_index, i, "");
                        else
                            ws.value(row_index, i, e.get(i));
                    }
                }
            }
            wb.finish();
        }
    }

    @Override
    public void setGroups(String groupID) {
        //Do nothing here, not required for this format at all
    }

}
