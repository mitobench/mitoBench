package io.reader;

import database.ColumnNameMapper;
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

    public ExcelReader(String file, Logger logger) throws IOException {

        Logger LOG = logger;
        LOG.info("Read Excel file: " + file);

        ColumnNameMapper mapper = new ColumnNameMapper();
        String excelFilePath = file;
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        Iterator<Cell> cellIterator;

        // read header
        List<String> header = new ArrayList<>();
        Row headerRow = iterator.next();

        if(headerRow.getCell(0).getStringCellValue().startsWith("##")){
            cellIterator = headerRow.cellIterator();


            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        header.add(cell.getStringCellValue().replace("##","").trim());
                        break;
                }
            }
        }


        // read types
        List<String> types = new ArrayList<>();
        Row typeRow = iterator.next();

        if(typeRow.getCell(0).getStringCellValue().startsWith("#")){
            cellIterator = typeRow.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        types.add(cell.getStringCellValue().replace("#","").trim());
                        break;
                }
            }
        }


        List<Entry> entries;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            cellIterator = nextRow.cellIterator();
            String id = "";
            int i = 0;
            entries = new ArrayList<>();

            Cell cell;

            for(int j = 0; j < nextRow.getLastCellNum(); j++) {
                cell = nextRow.getCell(j, Row.CREATE_NULL_AS_BLANK);
                Entry e = null;
                String colname;
                String data;

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        colname = mapper.mapString(header.get(i));
                        if(colname.equals("ID"))
                            id = cell.getStringCellValue();
                        data = cell.getStringCellValue();
                        e = new Entry(colname, new CategoricInputType("String"), new GenericInputData(data.toLowerCase()));
                        i++;
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        colname = mapper.mapString(header.get(i));
                        if(colname.equals("ID"))
                            id = cell.getStringCellValue();
                        data = String.valueOf(cell.getNumericCellValue());
                        e = new Entry(colname, new CategoricInputType("String"), new GenericInputData(data.toLowerCase()));
                        i++;
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        colname = mapper.mapString(header.get(i));
                        if(colname.equals("ID"))
                            id = cell.getStringCellValue();
                        data = String.valueOf(cell.getBooleanCellValue());
                        e = new Entry(colname, new CategoricInputType("String"), new GenericInputData(data.toLowerCase()));
                        i++;
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        colname = mapper.mapString(header.get(i));
                        if(colname.equals("ID"))
                            id = cell.getStringCellValue();
                        data = "Undefined";
                        e = new Entry(colname, new CategoricInputType("String"), new GenericInputData(data.toLowerCase()));
                        i++;
                        break;

                }
                entries.add(e);
            }
            map.put(id.toLowerCase(), entries);
            i++;
        }

        workbook.close();
        inputStream.close();

    }

    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return this.map;
    }
}