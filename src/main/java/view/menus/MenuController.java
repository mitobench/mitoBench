package view.menus;

import au.com.bytecode.opencsv.CSVWriter;
import controller.TableControllerUserBench;
import io.writer.GenericWriter;
import io.writer.MultiFastaWriter;
import javafx.scene.control.CustomMenuItem;
import validator.calculations.Validator;
import view.dialogues.information.InformationDialogue;

import java.io.IOException;
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
            validator.validate(file_meta, fasta_headers, log, file_fasta);
            boolean uploadPossible = validator.isUploadPossible();
            if(uploadPossible){
                result = "Data upload is possible. Please use the option 'Upload data'\n" +
                        "to import your data to the daatbase.";
            } else {
                result = "Data upload not possible. Please check the report below.";
            }

            // todo: delete tmp files
            // write to popup window
            InformationDialogue datavalidation_info_Dialogue = new InformationDialogue("Result data validation", log,
                    result, "dataValidationInfo");



        });
    }

    public void setTableController(TableControllerUserBench tableControllerUserBench) {
        this.tablecontroller = tableControllerUserBench;
    }
}
