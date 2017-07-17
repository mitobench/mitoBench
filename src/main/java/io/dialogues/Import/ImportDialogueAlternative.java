package io.dialogues.Import;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.menus.FileMenu;

import java.io.File;

/**
 * Created by neukamm on 31.01.17.
 */
public class ImportDialogueAlternative {

    private Stage stage;
    private VBox box_background;
    private TextField textField_filepath;
    private Button openBtn;
    private FileMenu fileMenu;

    public ImportDialogueAlternative(FileMenu fm){
        fileMenu = fm;
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(new Stage());
        box_background = new VBox(20);
        box_background.setId("import_dialogue_alt");
        textField_filepath = new TextField();
        textField_filepath.setId("textfield_path");

        openBtn = new Button("Open");
        openBtn.setId("btn_open");
        setAction(openBtn);

        box_background.getChildren().addAll(textField_filepath, openBtn);

        show();

    }


    private void setAction(Button openBtn) {
        openBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileMenu.openFile(new File(textField_filepath.getText()));
                stage.close();
            }
        });
    }

    /**
     * This method displays dialogue.
     */
    private void show() {
        Scene dialogScene = new Scene(box_background, 400, 280);
        stage.setScene(dialogScene);
        stage.show();
    }

    public File getFile() {
        return new File(textField_filepath.getText());
    }

}
