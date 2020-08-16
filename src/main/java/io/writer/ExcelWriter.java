package io.writer;

import controller.ATableController;
import io.IOutputData;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import controller.TableControllerUserBench;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
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
        Writer writer = null;
        Workbook wb = new XSSFWorkbook();

        //Create file extension if its not there already...

        if(!file.endsWith("xlsx")) {
            file = file + ".xlsx";
        }

        String safe_sheetname = WorkbookUtil.createSafeSheetName(file);
        int index_id = tableController.getColIndex("ID");
        int index_mt = tableController.getColIndex("MTSequence");

            Sheet sheet1 = wb.createSheet(safe_sheetname);
            Row row = sheet1.createRow(0);

            // write header
            List<String> columns = this.tableController.getCurrentColumnNames();
            for (int i = 0; i < columns.size(); i++) {
                Cell c = row.createCell(i);
                String text = columns.get(i);
                if(text.endsWith("(Grouping)")){
                    text = text.split(" ")[0];
                }
                c.setCellValue(text);
            }

            // write table content
            int rowcounter = 1; //Else, we loose our header here!
            for (Object list_entry : data) {
                ObservableList e = (ObservableList) list_entry;
                Row row_to_add = sheet1.createRow(rowcounter);
                rowcounter++;
                for (int i = 0; i < e.size(); i++) {
                    if(i==index_mt) {
                        String mt_seq = tableController.getDataTable().getMtStorage().getData().get(e.get(index_id));
                        Cell c = row_to_add.createCell(i);
                        c.setCellValue(mt_seq);
                    }else{
                        Cell c = row_to_add.createCell(i);
                        if(e.get(i).equals(""))
                            c.setCellValue("");
                        else
                            c.setCellValue((String) e.get(i));
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            fileOutputStream.close();




    }

    @Override
    public void setGroups(String groupID) {
        //Do nothing here, not required for this format at all
    }

}
