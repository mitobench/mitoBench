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
    private ObservableList<ObservableList> data;
    private TableController tableController;

    public ExcelWriter(TableController tableController) {
        this.data = tableController.getData();
        this.tableController = tableController;
    }

    @Override
    public void writeData(String file) throws IOException {
        Writer writer = null;
        Workbook wb = new XSSFWorkbook();
        String safe_sheetname = WorkbookUtil.createSafeSheetName(file);
        Sheet sheet1 = wb.createSheet(safe_sheetname);
        Row row = sheet1.createRow(0);

        // write header
        String header = "";
        List<String> columns = tableController.getCol_names();
        for (int i = 0; i < columns.size(); i++) {
            Cell c = row.createCell(i);
            c.setCellValue(columns.get(i));
        }

        int rowcounter = 0;
        for (ObservableList list_entry : data) {
            Row row_to_add = sheet1.createRow(rowcounter);
            rowcounter++;
            for (int i = 0; i < list_entry.size(); i++) {
                Cell c = row_to_add.createCell(i);
                c.setCellValue((String) list_entry.get(i));
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        wb.write(fileOutputStream);
        fileOutputStream.close();

    }

}
