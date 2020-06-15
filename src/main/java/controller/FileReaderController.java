package controller;

import Logging.LogClass;
import dataValidator.Validator;
import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.reader.*;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.dialogues.error.ARPErrorDialogue;
import view.dialogues.error.FastAErrorDialogue;
import view.dialogues.error.HSDErrorDialogue;
import view.dialogues.information.InformationDialogue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FileReaderController {

    private ATableController tableControllerUserBench;
    private Logger LOG;
    private MitoBenchWindow mito;

    public FileReaderController(TableControllerUserBench tableControllerUserBench, LogClass logClass, MitoBenchWindow mitoBenchWindow){
        this.tableControllerUserBench = tableControllerUserBench;
        this.LOG = logClass.getLogger(this.getClass());
        this.mito = mitoBenchWindow;

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
                        multiFastAInput = new MultiFastaParser(f.getPath(), LOG);
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
                        hsdInputParser = new HSDParser(f.getPath(), LOG);
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
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath(), LOG, "\t");
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (absolutePath.endsWith(".csv")) {
                try {
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath(), LOG, ",");
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
                    excelReader = new ExcelParser(f.getPath(), LOG);
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
                    arpreader = new ARPParser(f.getPath(), LOG);
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

//                if(mito.isAnotherProjectLoaded()){
//                    InformationDialogue informationDialogue = new InformationDialogue(
//                            "Project already loaded",
//                            "Please clean up your analysis before \na new project can be loaded.",
//                            "Project already loaded",
//                            "projectLoadedDialogue"
//                    );
//                } else {
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

                //}
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
