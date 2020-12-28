package controller;

import Logging.LogClass;
import io.Exceptions.*;
import io.datastructure.Entry;
import io.reader.*;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.dialogues.error.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileReaderController {

    private LogClass logClass;
    private Set<String> message_duplications;
    private ATableController tableControllerUserBench;
    private Logger LOG;
    private MitoBenchWindow mito;

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
                        HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                        tableControllerUserBench.updateTable(input_multifasta);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FastAException e) {
                    FastAErrorDialogue fastAErrorDialogue = new FastAErrorDialogue(e);
                }

            }

            //Input is HSD Format
            if (absolutePath.endsWith(".hsd")) {
                try {
                    HSDParser hsdInputParser = null;
                    try {
                        hsdInputParser = new HSDParser(f.getPath(), LOG, message_duplications);
                        HashMap<String, List<Entry>> data_map = hsdInputParser.getCorrespondingData();
                        tableControllerUserBench.updateTable(data_map);
                    } catch (HSDException e) {
                        HSDErrorDialogue hsdErrorDialogue = new HSDErrorDialogue(e);
                    }

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

            //Input is Excel Format

            if (absolutePath.endsWith(".xlsx") || absolutePath.endsWith(".xls")) {
                ExcelParser excelReader = null;
                try {
                    excelReader = new ExcelParser(f.getPath(), LOG, message_duplications);
                    HashMap<String, List<Entry>> data_map = excelReader.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EXCELException e) {
                    ExcelErrorDialogue excelErrorDialogue = new ExcelErrorDialogue(e);
                }
            }

            //Input is in ARP Format

            if(absolutePath.endsWith(".arp")){
                ARPParser arpreader = null;
                try {
                    arpreader = new ARPParser(f.getPath(), LOG, message_duplications);
                    HashMap<String, List<Entry>> data_map = arpreader.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ARPException e) {
                    ARPErrorDialogue arpErrorDialogue = new ARPErrorDialogue(e);
                }

            }

            if(absolutePath.endsWith(".mitoproj")){
                ProjectParser projectReader = new ProjectParser();
                try {

                    projectReader.read(f, LOG);
                    projectReader.loadData(tableControllerUserBench, mito.getChartController());
                    mito.setAnotherProjectLoaded(true);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ProjectException e) {
                    e.printStackTrace();
                }

            }


        } else {
            try {
                // do nothing
            }catch (Exception e ) {
                System.out.println(e.getMessage());
            }
        }
    }
}
