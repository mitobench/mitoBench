package Logging;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import view.dialogues.settings.APopupDialogue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by neukamm on 26.11.2016.
 */
public class LoggerSettingsDialogue extends APopupDialogue{

    private final Stage stage;
    private TextField filePathLabel;
    private TitledPane titledPane;
    private Button saveLogFileBtn;
    private Button chooseFileBtn;
    private Button applyBtn;
    private Button discardLogFile;
    private Label setFilePathLabel;


    public LoggerSettingsDialogue(String title, LogClass logClass, Stage primaryStage) {
        super(title, logClass);
        dialogGrid.setId("logDialogue");
        stage = primaryStage;
        setComponents();
        disableComponents();
        show();
    }

    private void setComponents() {

        Label infoLogFile = new Label("Do you want to save the log file?");
        saveLogFileBtn = new Button("Save LOG file");
        saveLogFileBtn.setId("saveLogFileBtn");
        discardLogFile = new Button("Discard LOG file");
        discardLogFile.setId("discardLogFile");

        setFilePathLabel = new Label("File location");
        filePathLabel = new TextField(System.getProperty("user.dir"));
        filePathLabel.setDisable(true);

        chooseFileBtn = new Button("Change location");
        chooseFileBtn.setId("btn_chooseLogDir");

        applyBtn = new Button("Apply");
        applyBtn.setId("btn_applyLogDir");

        Separator separator1 = new Separator();

        addButtonListener(applyBtn, chooseFileBtn);
        dialogGrid.add(infoLogFile, 0,0,1,1);
        dialogGrid.add(saveLogFileBtn, 1,0,1,1);
        dialogGrid.add(discardLogFile, 2,0,1,1);

        dialogGrid.add(separator1, 0,1, 3,1);

        dialogGrid.add(setFilePathLabel, 0,2,1,1);
        dialogGrid.add(filePathLabel, 0,3,2,1);
        dialogGrid.add(chooseFileBtn, 2,3,1,1);

        dialogGrid.add(applyBtn,2,4,1,1);



        titledPane = new TitledPane();
        titledPane.setText("LOG file configuration");
        titledPane.setCollapsible(false);
        titledPane.setContent(dialogGrid);


    }

    private void addButtonListener(Button applyBtn, Button chooseFileBtn) {


        chooseFileBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("JavaFX Projects");
            File defaultDirectory = new File(System.getProperty("user.dir"));
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(stage);
            filePathLabel.setText(selectedDirectory.getAbsolutePath());

        });


        applyBtn.setOnAction(e -> {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
            Date date = new Date();
            String file_source = System.getProperty("user.dir") + File.separator + "mito_log_tmp.log";
            String file_target = filePathLabel.getText() + File.separator + "mitobench_log_" + dateFormat.format(date) + ".log";
            try {
                Files.move(Paths.get(file_source), Paths.get(file_target));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            close();
            stage.close();
            System.exit(0);
        });

        saveLogFileBtn.setOnAction(e -> enableComponents());

        discardLogFile.setOnAction(e -> {
            // remove tmp log file
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + File.separator + "mito_log_tmp.log"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            close();
            stage.close();
            System.exit(0);
        });
    }


    private void enableComponents(){
        applyBtn.setDisable(false);
        chooseFileBtn.setDisable(false);
        setFilePathLabel.setDisable(false);
        filePathLabel.setDisable(false);
        filePathLabel.setEditable(true);

    }


    private void disableComponents(){
        applyBtn.setDisable(true);
        chooseFileBtn.setDisable(true);
        setFilePathLabel.setDisable(true);
    }


    @Override
    protected void show(){
        Scene dialogScene = new Scene(titledPane, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @Override
    public void close(){
        dialog.close();
        stage.close();
        System.exit(0);

    }
}
