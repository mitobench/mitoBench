package view.dialogues.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import statistics.HaploStatistics;

import java.io.IOException;

/**
 * Created by neukamm on 10.01.17.
 */
public class HGStatisticsPopupDialogue {


    private VBox dialogVbox;
    private Stage dialog;

    public HGStatisticsPopupDialogue(HaploStatistics haploStatistics){
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());
        dialogVbox = new VBox(20);
        addComponents(haploStatistics);

    }

    /**
     * This method adds all components to dialogue.
     * @param haploStatistics
     */
    private void addComponents(HaploStatistics haploStatistics){
        TextField textField = new TextField();
        Label label = new Label("Please enter comma separated list of haplogroups \naccording to which the haplogroups should be grouped.");
        Button okBtn = new Button("OK");
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(textField.getText().equals("") || textField.getText().startsWith("Please")){
                    textField.setText("Please enter list here.");
                } else {
                    haploStatistics.count(textField.getText().split(","));
                    try {
                        haploStatistics.printStatistics();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    dialog.close();
                }


            }
        });

        dialogVbox.getChildren().addAll(label, textField, okBtn);
        dialogVbox.setAlignment(Pos.CENTER);
    }


    public void show(){

        Scene dialogScene = new Scene(dialogVbox, 400, 140);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
