package controller;

import io.writer.GenericWriter;
import io.writer.MultiFastaWriter;
import javafx.scene.control.CustomMenuItem;
import validator.calculations.Validator;
import view.dialogues.information.DataValidationDialogue;
import view.dialogues.information.InformationDialogue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class MenuController {
    private TableControllerUserBench tablecontroller;

    public MenuController() {
    }

    public void setEditMenuValidateData(CustomMenuItem validateData) {
        validateData.setOnAction(t -> {

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

            GenericWriter metaWriter = new GenericWriter(tablecontroller, null, tablecontroller.getSelectedRows(),
                    ",", false);
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
               log += "Problems with column names. Please use the csv template.\n\n";
            }

            System.out.println("finished validation");
            boolean uploadPossible = validator.isUploadPossible();
            if(uploadPossible){
                result = "Data upload is possible.";
            } else {
                result = "Data upload not possible. Please check the report below.";
                log += validator.getLogfileTxt();
            }

            //delete tmp files
            deleteTmpFiles();

            // write to popup window
            DataValidationDialogue dataValidationDialogue = new DataValidationDialogue("Result data validation", result, log, uploadPossible);

            dataValidationDialogue.getUpload_later_btn().setOnAction(event -> {
                dataValidationDialogue.getDialog().close();
            });

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
