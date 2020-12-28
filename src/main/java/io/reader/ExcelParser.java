package io.reader;

import database.ColumnNameMapper;
import io.IInputData;
import io.datastructure.Entry;
import org.apache.log4j.Logger;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;


/**
 * Created by neukamm on 22.03.17.
 */
public class ExcelParser implements IInputData{

    private HashMap<String, List<Entry>> map = new HashMap<>();

    public ExcelParser(String filepath, Logger logger, Set<String> message_duplications) throws IOException {

        Logger LOG = logger;
        LOG.info("Read Excel file: " + filepath);

        ColumnNameMapper mapper = new ColumnNameMapper();

        try (InputStream is = new FileInputStream(filepath); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();
            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {
                    String str = r.getCellAsString(0).orElse(null);
                    System.out.println(str);
                });
            }
        }
    }

    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return this.map;
    }
}