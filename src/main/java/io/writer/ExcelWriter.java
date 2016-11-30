package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.table.TableController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by peltzer on 22/11/2016.
 */
public class ExcelWriter implements IOutputData {
    private TableController tableController;

    public ExcelWriter(TableController tableController) {

        this.tableController = tableController;
    }

    @Override
    public void writeData(String file) throws IOException {
        Writer writer = null;
        Workbook wb = new XSSFWorkbook();

        //Create file extension if its not there already...

        if(!file.endsWith("xlsx")) {
            file = file + ".xlsx";
        }

        String safe_sheetname = WorkbookUtil.createSafeSheetName(file);


        Sheet sheet1 = wb.createSheet(safe_sheetname);
        Row row = sheet1.createRow(0);

        // write header
        String header = "";
        List<String> columns = tableController.getCurrentColumnNames();
        for (int i = 0; i < columns.size(); i++) {
            Cell c = row.createCell(i);
            c.setCellValue(columns.get(i));
        }



        int rowcounter = 1; //Else, we loose our header here!
        for (Object list_entry : tableController.getViewDataCurrent()) {
            ObservableList e = (ObservableList) list_entry;
            Row row_to_add = sheet1.createRow(rowcounter);
            rowcounter++;
            for (int i = 0; i < e.size(); i++) {
                Cell c = row_to_add.createCell(i);
                c.setCellValue((String) e.get(i));
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
