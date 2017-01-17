package view.dialogues.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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

    public HGStatisticsPopupDialogue(HaploStatistics haploStatistics, TabPane statsTabpane, Scene scene){
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());
        dialogVbox = new VBox(20);
        addComponents(haploStatistics, statsTabpane, scene);

    }

    /**
     * This method adds all components to dialogue.
     * @param haploStatistics
     */
    private void addComponents(HaploStatistics haploStatistics, TabPane statsTabPane, Scene scene){
        TextField textField = new TextField();
        Label label = new Label("Please enter comma separated list of haplogroups \naccording to which the haplogroups should be grouped:");
        Button okBtn = new Button("OK");
        Label default_list = new Label("or use the default list:");
        CheckBox default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setSelected(false);
        Tooltip tp = new Tooltip("Default list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        default_list_checkbox.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
                tp.show(default_list_checkbox, p.getX(), p.getY());
            }
        });
        default_list_checkbox.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                tp.hide();
            }
        });
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if((textField.getText().equals("") || textField.getText().startsWith("Please")) &&  !default_list_checkbox.isSelected()){
                    textField.setText("Please enter list here.");

                } else {
                    String[] hg_list;
                    if(default_list_checkbox.isSelected()){
                        hg_list = haploStatistics.getChartController().getCoreHGs();
                    } else {
                        hg_list = textField.getText().split(",");
                    }
                    haploStatistics.count(hg_list);

                    TableView table = haploStatistics.writeToTable(haploStatistics.getData_all(), scene);
                    Tab tab = new Tab();
                    tab.setText("Count statistics");
                    tab.setContent(table);
                    statsTabPane.getTabs().add(tab);

                    dialog.close();
                }


            }
        });

        dialogVbox.getChildren().addAll(label, textField, default_list, default_list_checkbox, okBtn);
        dialogVbox.setAlignment(Pos.CENTER);
    }


    /**
     * This method displays dialogue.
     */
    public void show(){
        Scene dialogScene = new Scene(dialogVbox, 400, 280);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
