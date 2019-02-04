package io.writer;

import io.IOutputData;
import io.datastructure.fastA.FastaEntry;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.apache.log4j.Logger;
import controller.TableControllerUserBench;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peltzer on 07/12/2016.
 * This method should produce a multiFastA output file with corresponding ID + C14 Dating information (where applicable).
 * If nothing is given for a certain individual in terms of dating information,
 * we produce output in the form of
 * <ID>_0 (whereas we use year zero == 2000 after julian calender to be more precise.
 */
public class BEASTWriter implements IOutputData {
    private final ObservableList<ObservableList> data;
    private final int year;
    private TableControllerUserBench tableController;
    private Logger LOG;
    private FileWriter fileWriter;
    private BufferedWriter bfWriter;

    public BEASTWriter(TableControllerUserBench tblcontroller, Logger LOG, ObservableList<ObservableList> dataToExport, int year) {
        this.tableController = tblcontroller;
        this.LOG = LOG;
        this.data = dataToExport;
        this.year = year;
    }

    @Override
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        fileWriter = null;
        try {
            if(!(file.endsWith(".fasta") || !file.endsWith(".fa") || file.endsWith(".fas")))
                file = file + ".beast.fasta";

            fileWriter = new FileWriter(new File(file));
            bfWriter = new BufferedWriter(fileWriter);
            HashMap<FastaEntry, String> tmp = getSequenceData();

            List<String> ids = new ArrayList<>();
            for(int i = 0; i < data.size(); i++){
                ids.add((String)data.get(i).get(0));
            }

            for (FastaEntry key : tmp.keySet()) {
                if(ids.contains(key.getHeader())){
                    String c14date = tmp.get(key);
                    bfWriter.write(">" + key.getHeader() + getC14String(c14date, year) + "\n" + key.getSequence() + "\n");
                }
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            fileWriter.flush();
            bfWriter.close();
            fileWriter.close();
        }

    }

    @Override
    public void setGroups(String groupID) {
        //nothing we need to do here
    }


    private HashMap<FastaEntry, String> getSequenceData() {
        HashMap<FastaEntry, String> list = new HashMap<>();
        TableColumn tbclm_id = tableController.getTableColumnByName("ID");
        TableColumn tbclm_c14 = tableController.getTableColumnByName("C14-Date");
        // write view.data

        if(tbclm_c14!=null){
            tableController.getTable().getItems().stream().forEach((o)
                    -> list.put(new FastaEntry(tableController.getDataTable().getMtStorage().getData().get(tbclm_id.getCellData(o)), (String) tbclm_id.getCellData(o)), (String) tbclm_c14.getCellData(o))
            );
        } else {
            tableController.getTable().getItems().stream().forEach((o)
                    -> list.put(new FastaEntry(tableController.getDataTable().getMtStorage().getData().get(tbclm_id.getCellData(o)), (String) tbclm_id.getCellData(o)), "")
            );
        }


        return list;
    }

    /**
     * Performs deduction of year 2000 correctly.
     *
     * @param c14data
     * @param year
     * @return
     */
    private String getC14String(String c14data, int year) {
        String tmp = "";
        if(!c14data.equals("")){
            if (!c14data.equals("Undefined")) {
                //tmp = "_" + Math.abs(Double.parseDouble(c14data) - 2000);
                tmp = "_" + Math.abs(year - Double.parseDouble(c14data)); // years before present
            }

            return tmp;
        } else {
            return "";
        }

    }
}
