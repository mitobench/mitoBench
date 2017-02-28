package Logging;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.dialogues.settings.APopupDialogue;

import java.io.File;


/**
 * Created by neukamm on 26.11.2016.
 */
public class LoggerSettingsDialogue extends APopupDialogue{


    private final LogClass logger;
    private final Stage stage;
    private Label filePathLabel;


    public LoggerSettingsDialogue(String title, LogClass logClass, Stage primaryStage) {
        super(title);
        logger = logClass;
        setComponents();
        stage = primaryStage;
        show();
    }

    private void setComponents() {

        Label setFilePathLabel = new Label("Choose log file location");
        filePathLabel = new Label("/path/to/file/log.log");
        Button chooseFileBtn = new Button("Choose location");
        Button applyBtn = new Button("Apply");

        addButtonListener(applyBtn, chooseFileBtn);

        dialogGrid.add(setFilePathLabel, 0,0,1,1);
        dialogGrid.add(chooseFileBtn, 2,0,1,1);
        dialogGrid.add(filePathLabel, 0,1,3,1);
        dialogGrid.add(applyBtn,2,2,1,1);

    }

    private void addButtonListener(Button applyBtn, Button chooseFileBtn) {


        chooseFileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                File file = fc.showSaveDialog(stage);
                filePathLabel.setText(file.getAbsolutePath());

            }
        });


        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                logger.updateLog4jConfiguration(filePathLabel.getText());
                close();
                stage.close();
            }
        });

    }
}
