package controller;

import calculations.Validator;
import com.company.DataCompleter;
import database.DataUploader;
import database.DuplicatesChecker;
import io.reader.GenericInputParser;
import io.writer.GenericWriter;
import io.writer.MultiFastaWriter;
import javafx.scene.control.CustomMenuItem;
import org.apache.log4j.Logger;
import view.dialogues.information.DataValidationDialogue;
import view.dialogues.information.InformationDialogue;

import javax.sound.sampled.DataLine;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class MenuController {
    private final Logger log;
    private TableControllerUserBench tablecontroller;

    public MenuController(Logger logger) {
        this.log = logger;
    }

    public void setEditMenuValidateData(CustomMenuItem validateData) {

        validateData.setOnAction(t -> {
            // start validation on non-empty data table only
            if(tablecontroller.getTable().getItems().size() == 0){
                InformationDialogue informationDialogue = new InformationDialogue("Data validation", "No data for data validation", "", "");
                System.err.println("No data for data validation");
            } else {
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
                String file_fasta = "tmp_fasta_toValidate.fasta";

                // get fasta headers
                List<String> fasta_headers = new ArrayList<>();
                fasta_headers.addAll(tablecontroller.getDataTable().getMtStorage().getData().keySet());
                String file_meta="tmp_meta_data_toValidate.csv";

                String log="";
                String result = "";

                // run validation
                Validator validator = new Validator();
                System.out.println("running validation");
                try{
                    validator.validate(file_meta, fasta_headers, log, file_fasta);
                } catch (ArrayIndexOutOfBoundsException e){
                    log += "Problems with column names. Please use the csv template.\n\n" + validator.getLogfileTxt() + "\nMissing columns:\n\n" + validator.getLog_missing_columns();
                }

                System.out.println("finished validation");
                boolean uploadPossible = validator.isUploadPossible();
                if(uploadPossible){
                    result = "Data upload is possible.";
                } else {
                    result = "Data upload not possible. Please check the report below.";
                    log += validator.getLogfileTxt();
                }


                // write to popup window
                DataValidationDialogue dataValidationDialogue = new DataValidationDialogue("Result data validation", result, log, uploadPossible);

                // Button 'upload later': do nothing
                dataValidationDialogue.getUpload_later_btn().setOnAction(event -> {
                    dataValidationDialogue.getDialog().close();
                });

                // Button 'upload now':
                //  - check for duplicates
                //  - complete meta information
                //  - upload data
                dataValidationDialogue.getUpload_now_btn().setOnAction(event -> {

                    // todo: check for duplicates
                    DuplicatesChecker duplicatesChecker = new DuplicatesChecker();
                    System.out.println("Checking for duplicates ....");
                    duplicatesChecker.check();

                    // - data completion
                    DataCompleter dataCompleter = new DataCompleter();
                    System.out.println("Completing meta information ....");
                    try {
                        dataCompleter.run(file_meta, file_fasta, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  --  update data view in mitoBench data window
                    System.out.println("Update data in mitoBench ....");
                    try {
                        GenericInputParser genericInputParser = new GenericInputParser(dataCompleter.getOutfile(), this.log, ",");
                        tablecontroller.updateTable(genericInputParser.getCorrespondingData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // - upload data
                    System.out.println("Uploading data to database ....");
                    DataUploader dataUploader = new DataUploader(tablecontroller);
                    dataUploader.upload();

                    dataValidationDialogue.getDialog().close();

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTableController(TableControllerUserBench tableControllerUserBench) {
        this.tablecontroller = tableControllerUserBench;
    }
}
