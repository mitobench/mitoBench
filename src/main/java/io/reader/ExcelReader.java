package io.reader;

import io.Exceptions.EXCELException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by neukamm on 22.03.17.
 */
public class ExcelReader implements IInputData{

    private HashMap<String, List<Entry>> map = new HashMap<>();
    private Logger LOG;

    public ExcelReader(String file, Logger logger) throws IOException, EXCELException {

        LOG = logger;
        LOG.info("Read Excel file: " + file);

        String excelFilePath = file;
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        // read header
        List<String> header = new ArrayList<>();
        Row headerRow = iterator.next();
        Iterator<Cell> cellIterator = headerRow.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    header.add(cell.getStringCellValue());
                    break;
            }
        }
        List<Entry> entries;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            cellIterator = nextRow.cellIterator();
            String id = "";
            int i = 0;
            entries = new ArrayList<>();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Entry e = null;

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        if(i==0)
                            id = cell.getStringCellValue();
                        e = new Entry(header.get(i), new CategoricInputType("String"), new GenericInputData(cell.getStringCellValue()));
                        i++;
                        break;
                }
                entries.add(e);
            }
            map.put(id, entries);


        }

        workbook.close();
        inputStream.close();


    }

    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return this.map;
    }
}
