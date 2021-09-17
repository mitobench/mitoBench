package controller;

import Logging.LogClass;
import analysis.SequenceAligner;
import dataCompleter.DataCompleter;
import dataValidator.Validator;
import io.reader.GenericInputParser;
import io.writer.GenericWriter;
import io.writer.MultiFastaWriter;
import javafx.scene.control.MenuItem;
import view.MitoBenchWindow;
import view.dialogues.information.DataValidationDialogue;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.DataFilteringHaplotypeBasedDialogue;
import view.dialogues.settings.DataFilteringTreebasedDialogue;
import view.dialogues.settings.HGListDialogue;

import java.io.IOException;
import java.util.ArrayList;


public class MenuController {
    private final MitoBenchWindow mito;
    private LogClass log;
    private TableControllerUserBench tablecontroller;
    private String log_validation;
    private String result_validation;
    private ArrayList<String> fasta_headers;
    private String file_fasta;
    private String file_meta;
    private boolean uploadPossible;

    public MenuController(MitoBenchWindow mito) {
        this.log = mito.getLogClass();
        this.mito = mito;
    }

    /**
     * Set/Define all actions of the Edit Menu
     *
     * @param filterTreeBased
     * @param filterWithMutation
     * @param defineHGList
     */
    public void setEditMenu(MenuItem filterTreeBased, MenuItem filterWithMutation, MenuItem defineHGList) {

        filterTreeBased.setOnAction(t -> {

            DataFilteringTreebasedDialogue dataFilteringWithListDialogue =
                    new DataFilteringTreebasedDialogue("Tree based data filtering",
                            log, mito);
            mito.getTabpane_statistics().getTabs().add(dataFilteringWithListDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(dataFilteringWithListDialogue.getTab());

        });

        filterWithMutation.setOnAction(t -> {

            DataFilteringHaplotypeBasedDialogue dataFilteringMutationBasedDialogue =
                    new DataFilteringHaplotypeBasedDialogue("Haplotype based data filtering", log, mito);
            mito.getTabpane_statistics().getTabs().add(dataFilteringMutationBasedDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(dataFilteringMutationBasedDialogue.getTab());
        });


        defineHGList.setOnAction(t -> {

            HGListDialogue hgListDialogue = new HGListDialogue("Custom Haplogroup list", log, mito);
            HGListController hgListController = new HGListController(hgListDialogue, mito.getChartController(), mito);
            mito.getTabpane_statistics().getTabs().add(hgListDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(hgListDialogue.getTab());

        });
    }


    public void setToolsMenu(MenuItem dataAlignerMenuItem, MenuItem dataValidatorMenuItem, MenuItem dataCompleterMenuItem){

        dataAlignerMenuItem.setOnAction(t -> {
            SequenceAligner sequenceAligner = new SequenceAligner(mito.getLogClass().getLogger(this.getClass()), mito.getTableControllerUserBench(), mito);

            // get sequences to align
            MultiFastaWriter multiFastaWriter = new MultiFastaWriter(mito.getTableControllerUserBench().getDataTable().getMtStorage(),
                    mito.getTableControllerUserBench().getSelectedRows(),
                    true);
            try {
                try {
                    multiFastaWriter.writeData("sequences_to_align.fasta", mito.getTableControllerUserBench());
                    sequenceAligner.align("sequences_to_align.fasta");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        });


        dataValidatorMenuItem.setOnAction(t -> {
            // start validation and completion on non-empty data table only
            if(tablecontroller.getTable().getItems().size() == 0){
                InformationDialogue informationDialogue = new InformationDialogue("Data validation", "No data for data validation", "", "");
                System.err.println("No data for data validation");
            } else {

                if(!tablecontroller.isValidated()){
                    validate();
                    //InformationDialogue informationDialogue = new InformationDialogue("Data validation", result_validation, "Data validation finished", "");
                    // write to popup window
                    DataValidationDialogue dataValidationDialogue = new DataValidationDialogue("Result data validation", result_validation, log_validation, uploadPossible);
                    dataValidationDialogue.setUpload_later_btn_disabled(true);
                    dataValidationDialogue.setUpload_now_btn_disabled(true);
                    dataValidationDialogue.show(600,400);

                    tablecontroller.setValidated(true);
                } else {
                    System.out.println("Data is already validated. Continue");
                }
            }
        });


        dataCompleterMenuItem.setOnAction(t -> {
            // start validation and completion on non-empty data table only
            if(tablecontroller.getTable().getItems().size() == 0){
                InformationDialogue informationDialogue = new InformationDialogue("Data completion", "No data for data completion", "", "");
                System.err.println("No data for data validation");
            } else {
                if(!tablecontroller.isValidated()){
                    validate();
                    tablecontroller.setValidated(true);
                } else {
                    System.out.println("Data is already validated. Continue");
                }

                if(uploadPossible){
                    // - data completion
                    if(!tablecontroller.isCompleted()){
                        DataCompleter dataCompleter = new DataCompleter();
                        System.out.println("Completing meta information ....");
                        try {

                            dataCompleter.run(file_meta, file_fasta, "");
                            GenericInputParser genericInputParser = new GenericInputParser(
                                    dataCompleter.getOutfile(),
                                    this.log.getLogger(this.getClass()),
                                    "\t",
                                    null);
                            tablecontroller.updateTable(genericInputParser.getCorrespondingData());
                            tablecontroller.setValidated(true);
                            tablecontroller.setCompleted(true);
                            InformationDialogue informationDialogue = new InformationDialogue("Data completion", "Data validation finished.\nData completion finished.", "", "");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Data is already completed. Continue");
                    }
                }
            }
        });

    }



    /**
     * Validate data
     */
    private void validate(){
        System.out.println("Start data validation");

        log_validation="";
        result_validation="";
        uploadPossible = false;

        // write data to fasta and csv file
        MultiFastaWriter multiFastaWriter = new MultiFastaWriter(
                tablecontroller.getDataTable().getMtStorage(),
                tablecontroller.getSelectedRows(),
                true);
        try {
            multiFastaWriter.writeData("tmp_fasta_toValidate.fasta", tablecontroller);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GenericWriter metaWriter = new GenericWriter(tablecontroller.getSelectedRows(),"\t", false);
        try {
            metaWriter.writeData("tmp_meta_data_toValidate.tsv", tablecontroller);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file_fasta = "tmp_fasta_toValidate.fasta";

        // get fasta headers
        fasta_headers = new ArrayList<>();
        fasta_headers.addAll(tablecontroller.getDataTable().getMtStorage().getData().keySet());
        file_meta="tmp_meta_data_toValidate.tsv";

        log_validation="";
        result_validation = "";


        // run validation
        Validator validator = new Validator();
        System.out.println("running validation");
        try{
            validator.validate(file_meta, fasta_headers, log_validation, file_fasta, mito.getTableControllerUserBench().getTable().getItems().size());
        } catch (ArrayIndexOutOfBoundsException e){
            log_validation += "Problems with column names. Please use the csv template.\n\n" + validator.getLogfileTxt() + "\nMissing columns:\n\n" + validator.getLog_missing_columns();
        }

        System.out.println("finished validation");
        uploadPossible = true;
        if(uploadPossible){
            result_validation = "All values are in correct format.";
        } else {
            result_validation = "Data upload not possible. Please check the report below.";
        }
    }


    /*
         Setter and Getter
     */
    public void setTableController(TableControllerUserBench tableControllerUserBench) {
        this.tablecontroller = tableControllerUserBench;
    }


}
