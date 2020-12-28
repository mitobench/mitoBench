package io.reader;

import database.ColumnNameMapper;
import io.Exceptions.EXCELException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
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

    private ArrayList<String> header;
    private ArrayList<String> types;
    private HashMap<String, List<Entry>> map = new HashMap<>();

    public ExcelParser(String filepath, Logger logger, Set<String> message_duplications) throws IOException, EXCELException {

        Logger LOG = logger;
        LOG.info("Read Excel file: " + filepath);
        ColumnNameMapper mapper = new ColumnNameMapper();
        InputStream is = new FileInputStream(filepath);
        ReadableWorkbook wb = new ReadableWorkbook(is);
        Sheet sheet = wb.getFirstSheet();

        List<Row> rows_tmp = sheet.read();
        if (!rows_tmp.get(0).getCellAsString(0).orElse(null).startsWith("##") ||
                !rows_tmp.get(1).getCellAsString(0).orElse(null).startsWith("#")){
            throw new EXCELException("This is not in correct Format!\nThe header lines are missing or incorrect. Please also check " +
                    "if the file contains empty rows.");
        } else {
            rows_tmp.clear();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {
                    if(r.getCellAsString(0).orElse(null).startsWith("##")){
                        // read header
                        header = new ArrayList<>();
                        for (int i = 0; i < r.getCellCount(); i++){
                            header.add(r.getCellAsString(i).orElse(null).replace("##","").trim());
                        }
                    } else if(r.getCellAsString(0).orElse(null).startsWith("#")){
                        // read types
                        types = new ArrayList<>();

                        for (int i = 0; i < r.getCellCount(); i++){
                            types.add(r.getCellAsString(i).orElse(null).replace("#","").trim());
                        }
                    } else {
                        // read meta information
                        List<Entry> entries;
                        String id = "";
                        entries = new ArrayList<>();

                        for(int j = 0; j < r.getCellCount(); j++) {
                            String colname;
                            String data;

                            colname = mapper.mapString(header.get(j));
                            data = r.getCellAsString(j).orElse(null);

                            if(colname.equals("ID")){
                                id = r.getCellAsString(j).orElse(null);
                                id = id.split(" ")[0];
                                if(id.matches(".*[^\\d]\\d{1}$")){
                                    id = id.split("\\.")[0];
                                    data = id;
                                }
                            }
                            entries.add(new Entry(colname, new CategoricInputType("String"), new GenericInputData(data)));
                        }
                        map.put(id , entries);
                    }
                });
            } catch (Exception e){
                throw new EXCELException();
            }

        }
        wb.close();
        is.close();
    }

    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return this.map;
    }
}