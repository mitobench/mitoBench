package controller;

import Logging.LogClass;
import dataCompleter.DataCompleter;
import dataValidator.Validator;
import database.DataUploader;
import database.DatabaseQueryHandler;
import database.DuplicatesChecker;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


public class MenuController {
    private final MitoBenchWindow mito;
    private LogClass log;
    private DatabaseQueryHandler databaseQueryHandler;
    private TableControllerUserBench tablecontroller;
    private String log_validation;
    private String result_validation;
    private ArrayList<String> fasta_headers;
    private String file_fasta;
    private String file_meta;
    private boolean uploadPossible;
    private DataCompleter dataCompleter;

    public MenuController(DatabaseQueryHandler databaseQueryHandler, MitoBenchWindow mito) {
        this.log = mito.getLogClass();
        this.databaseQueryHandler = databaseQueryHandler;
        this.mito = mito;
    }

    /**
     * Set/Define all actions of the Edit Menu
     *
     * @param filterTreeBased
     * @param filterWithMutation
     * @param defineHGList
     */
    public void setEditMenu(MenuItem filterTreeBased, MenuItem filterWithMutation, MenuItem unfilterData, MenuItem defineHGList) {

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

        unfilterData.setOnAction(t -> {
            mito.getTableControllerUserBench().resetToUnfilteredData();

        });

        defineHGList.setOnAction(t -> {

            HGListDialogue hgListDialogue = new HGListDialogue("Custom Haplogroup list", log);
            HGListController hgListController = new HGListController(hgListDialogue, mito.getChartController(), mito);
            mito.getTabpane_statistics().getTabs().add(hgListDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(hgListDialogue.getTab());


        });

    }


    public void setToolsMenu(MenuItem dataValidatorMenuItem, MenuItem dataCompleterMenuItem, MenuItem dataUploaderMenuItem) {


        dataValidatorMenuItem.setOnAction(t -> {
            // start validation and completion on non-empty data table only
            if(tablecontroller.getTable().getItems().size() == 0){
                InformationDialogue informationDialogue = new InformationDialogue("Data validation", "No data for data validation", "", "");
                System.err.println("No data for data validation");
            } else {

                validate();
                //InformationDialogue informationDialogue = new InformationDialogue("Data validation", result_validation, "Data validation finished", "");
                // write to popup window
                DataValidationDialogue dataValidationDialogue = new DataValidationDialogue("Result data validation", result_validation, log_validation, uploadPossible);
                dataValidationDialogue.setUpload_later_btn_disabled(true);
                dataValidationDialogue.setUpload_now_btn_disabled(true);
                dataValidationDialogue.show(600,400);

                deleteTmpFiles();

            }

        });


        dataCompleterMenuItem.setOnAction(t -> {
                    // start validation and completion on non-empty data table only
                    if(tablecontroller.getTable().getItems().size() == 0){
                        InformationDialogue informationDialogue = new InformationDialogue("Data completion", "No data for data completion", "", "");
                        System.err.println("No data for data validation");
                    } else {

                        validate();
                        if(uploadPossible){
                            // - data completion
                            DataCompleter dataCompleter = new DataCompleter();
                            System.out.println("Completing meta information ....");

                            try {

                                dataCompleter.run(file_meta, file_fasta, "");
                                GenericInputParser genericInputParser = new GenericInputParser(dataCompleter.getOutfile(), this.log.getLogger(this.getClass()), ",");
                                tablecontroller.updateTable(genericInputParser.getCorrespondingData());
                                InformationDialogue informationDialogue = new InformationDialogue("Data completion", "Data validation finished.\nData completion finished.", "", "");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }

            deleteTmpFiles();

        });


        dataUploaderMenuItem.setOnAction(t -> {
            // start validation on non-empty data table only
            if(tablecontroller.getTable().getItems().size() == 0){
                InformationDialogue informationDialogue = new InformationDialogue("Data upload", "No data for data upload", "", "");
                System.err.println("No data for data validation");
            } else {

                validate();

                // write to popup window
                DataValidationDialogue dataValidationDialogue = new DataValidationDialogue("Result data validation", result_validation, log_validation, uploadPossible);
                dataValidationDialogue.show(600,400);
                // Button 'upload later': do nothing
                dataValidationDialogue.getUpload_later_btn().setOnAction(event -> {
                    dataValidationDialogue.getDialog().close();
                    deleteTmpFiles();
                });

                // Button 'upload now':
                //  - check for duplicates
                //  - complete meta information
                //  - upload data
                dataValidationDialogue.getUpload_now_btn().setOnAction(event -> {

                    // todo: check for duplicates
                    DuplicatesChecker duplicatesChecker = new DuplicatesChecker(databaseQueryHandler);
                    System.out.println("Checking for duplicates ....");
                    duplicatesChecker.check(fasta_headers);

                    // - data completion
                    dataCompleter = new DataCompleter();
                    System.out.println("Completing meta information ....");
                    try {
                        dataCompleter.run(file_meta, file_fasta, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  --  update data view in mitoBench data window
                    System.out.println("Update data in mitoBench ....");
                    try {
                        GenericInputParser genericInputParser = new GenericInputParser(dataCompleter.getOutfile(), this.log.getLogger(this.getClass()), ",");
                        tablecontroller.updateTable(genericInputParser.getCorrespondingData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // - upload data
                    System.out.println("Uploading data to database ....");
                    DataUploader dataUploader = new DataUploader(tablecontroller, this.log.getLogger(this.getClass()));
                    dataUploader.parseMeta(dataCompleter.getOutfile());

                    dataValidationDialogue.getDialog().close();
                    deleteTmpFiles();

                });


            }

        });
    }

    /**
     * Delete all temporary files that were created while data validation
     */
    private void deleteTmpFiles() {

        try {
            if (Files.exists(new File("tmp_meta_data_toValidate.csv").toPath()))
                Files.delete(new File("tmp_meta_data_toValidate.csv").toPath());

            if (Files.exists(new File("tmp_fasta_toValidate.fasta").toPath()))
                Files.delete(new File("tmp_fasta_toValidate.fasta").toPath());

            if(dataCompleter != null && Files.exists(new File(dataCompleter.getOutfile()).toPath())){
                Files.delete(new File(dataCompleter.getOutfile()).toPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTableController(TableControllerUserBench tableControllerUserBench) {
        this.tablecontroller = tableControllerUserBench;
    }


    private void validate(){

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

        GenericWriter metaWriter = new GenericWriter(tablecontroller.getSelectedRows(),",", false);
        try {
            metaWriter.writeData("tmp_meta_data_toValidate.csv", tablecontroller);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file_fasta = "tmp_fasta_toValidate.fasta";

        // get fasta headers
        fasta_headers = new ArrayList<>();
        fasta_headers.addAll(tablecontroller.getDataTable().getMtStorage().getData().keySet());
        file_meta="tmp_meta_data_toValidate.csv";

        log_validation="";
        result_validation = "";

        // run validation
        Validator validator = new Validator();
        System.out.println("running validation");
        try{
            validator.validate(file_meta, fasta_headers, log_validation, file_fasta);
            System.out.println();
        } catch (ArrayIndexOutOfBoundsException e){
            log_validation += "Problems with column names. Please use the csv template.\n\n" + validator.getLogfileTxt() + "\nMissing columns:\n\n" + validator.getLog_missing_columns();
        }

        System.out.println("finished validation");
        uploadPossible = validator.isUploadPossible();
        if(uploadPossible){
            result_validation = "All values are in correct format.";
        } else {
            result_validation = "Data upload not possible. Please check the report below.";
            log_validation += validator.getLogfileTxt();
        }

    }


}
