package controller;

import Logging.LogClass;
import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.reader.*;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.dialogues.error.ARPErrorDialogue;
import view.dialogues.error.DuplicatesDialogue;
import view.dialogues.error.FastAErrorDialogue;
import view.dialogues.error.HSDErrorDialogue;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileReaderController {

    private LogClass logClass;
    private Set<String> message_duplications;
    private ATableController tableControllerUserBench;
    private Logger LOG;
    private MitoBenchWindow mito;
    private HashMap<String, List<Entry>> table_list_duplications;

    public FileReaderController(TableControllerUserBench tableControllerUserBench, LogClass logClass, MitoBenchWindow mitoBenchWindow){
        this.tableControllerUserBench = tableControllerUserBench;
        this.logClass = logClass;
        this.LOG = logClass.getLogger(this.getClass());
        this.mito = mitoBenchWindow;
        this.message_duplications = new HashSet<>();

    }



    /**
     * Method to open files with specific parser.
     * @param f
     */
    public void openFile(File f){

        if (f != null) {
            String absolutePath = f.getAbsolutePath();

            //Input is FastA
            if (absolutePath.endsWith(".fasta") || absolutePath.endsWith(".fas") || absolutePath.endsWith(".fa") || absolutePath.endsWith(".fna") ) {

                MultiFastaParser multiFastAInput = null;
                try {
                    try {
                        multiFastAInput = new MultiFastaParser(f.getPath(), LOG, message_duplications);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FastAException e) {
                    FastAErrorDialogue fastAErrorDialogue = new FastAErrorDialogue(e);
                }
                HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                tableControllerUserBench.updateTable(input_multifasta);
            }

            //Input is HSD Format
            if (absolutePath.endsWith(".hsd")) {
                try {
                    HSDParser hsdInputParser = null;
                    try {
                        hsdInputParser = new HSDParser(f.getPath(), LOG, message_duplications);
                    } catch (HSDException e) {
                        HSDErrorDialogue hsdErrorDialogue = new HSDErrorDialogue(e);
                    }
                    HashMap<String, List<Entry>> data_map = hsdInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is Generic Format

            if (absolutePath.endsWith(".tsv")) {
                try {
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath(), LOG, "\t", message_duplications);

                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (absolutePath.endsWith(".csv")) {
                try {
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath(), LOG, ",", message_duplications);
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    //message_duplications = genericInputParser.getList_duplicates();
                    table_list_duplications = genericInputParser.getList_duplicates();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is Excel Format

            if (absolutePath.endsWith(".xlsx") || absolutePath.endsWith(".xls")) {
                ExcelParser excelReader = null;
                try {
                    excelReader = new ExcelParser(f.getPath(), LOG, message_duplications);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String, List<Entry>> data_map = excelReader.getCorrespondingData();
                tableControllerUserBench.updateTable(data_map);
                //tableControllerUserBench.loadGroups();
            }

            //Input is in ARP Format

            if(absolutePath.endsWith(".arp")){
                ARPParser arpreader = null;
                try {
                    arpreader = new ARPParser(f.getPath(), LOG, message_duplications);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ARPException e) {
                    ARPErrorDialogue arpErrorDialogue = new ARPErrorDialogue(e);
                }
                HashMap<String, List<Entry>> data_map = arpreader.getCorrespondingData();
                tableControllerUserBench.updateTable(data_map);
                //tableControllerUserBench.loadGroups();
            }

            if(absolutePath.endsWith(".mitoproj")){
                ProjectParser projectReader = new ProjectParser();
                try {

                    projectReader.read(f, LOG);
                    projectReader.loadData(tableControllerUserBench, mito.getChartController());
                    //tableControllerUserBench.loadGroups();
                    mito.setAnotherProjectLoaded(true);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ProjectException e) {
                    e.printStackTrace();
                }

            }

            DuplicatesDialogue duplicatesErrorDialogue = new DuplicatesDialogue(table_list_duplications,  logClass, mito.getTableControllerDB());


        } else {
            try {
                // do nothing
            }catch (Exception e ) {
                System.out.println(e.getMessage());
            }
        }
    }
}
