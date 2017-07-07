package io.writer;

import io.IOutputData;
import io.datastructure.fastA.FastaEntry;
import javafx.scene.control.TableColumn;
import view.table.controller.TableControllerUserBench;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by peltzer on 07/12/2016.
 * This method should produce a multiFastA output file with corresponding ID + C14 Dating information (where applicable). If nothing is given for a certain individual in terms of dating information,
 * we produce output in the form of
 * <ID>_0 (whereas we use year zero == 2000 after julian calender to be more precise.
 */
public class BEASTWriter implements IOutputData {
    private TableControllerUserBench tblcontroller;
    private FileWriter fileWriter;
    private BufferedWriter bfWriter;

    public BEASTWriter(TableControllerUserBench tblcontroller) {
        this.tblcontroller = tblcontroller;
    }


    @Override
    public void writeData(String file) throws IOException {
        fileWriter = new FileWriter(new File(file));
        bfWriter = new BufferedWriter(fileWriter);
        HashMap<FastaEntry, String> tmp = getSequenceData();
        for (FastaEntry key : tmp.keySet()) {
            String c14date = tmp.get(key);
            bfWriter.write(">" + key.getHeader() + getC14String(c14date) + "\n" + key.getSequence() + "\n");
        }
        bfWriter.flush();
        bfWriter.close();
    }

    @Override
    public void setGroups(String groupID) {
        //nothing we need to do here
    }


    private HashMap<FastaEntry, String> getSequenceData() {
        HashMap<FastaEntry, String> list = new HashMap<>();
        TableColumn tbclm_id = tblcontroller.getTableColumnByName("ID");
        TableColumn tbclm_c14 = tblcontroller.getTableColumnByName("C14-Date");
        // write view.data

        if(tbclm_c14!=null){
            tblcontroller.getTable().getItems().stream().forEach((o)
                    -> list.put(new FastaEntry(tblcontroller.getDataTable().getMtStorage().getData().get(tbclm_id.getCellData(o)), (String) tbclm_id.getCellData(o)), (String) tbclm_c14.getCellData(o))
            );
        } else {
            tblcontroller.getTable().getItems().stream().forEach((o)
                    -> list.put(new FastaEntry(tblcontroller.getDataTable().getMtStorage().getData().get(tbclm_id.getCellData(o)), (String) tbclm_id.getCellData(o)), "")
            );
        }


        return list;
    }

    /**
     * Performs deduction of year 2000 correctly.
     *
     * @param c14data
     * @return
     */
    private String getC14String(String c14data) {
        String tmp = "";
        if(!c14data.equals("")){
            if (c14data.equals("Undefined")) {
                tmp = "_0";
            } else {
                tmp = "_" + Math.abs(Double.parseDouble(c14data) - 2000);
            }

            return tmp;
        } else {
            return "";
        }

    }
}
