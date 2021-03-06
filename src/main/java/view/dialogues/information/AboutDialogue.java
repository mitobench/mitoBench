package view.dialogues.information;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by peltzer on 02/12/2016.
 */
public class AboutDialogue {

    private final Stage dialog;
    private final GridPane dialogGrid;
    private final ImageView imageView;
    private Hyperlink link;

    public AboutDialogue(String title, String message) {

        dialog = new Stage();
        dialog.setTitle(title);

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);


        imageView = new ImageView(new Image(getClass().getResourceAsStream("/logo/mitoBenchLogo.jpg")));
        imageView.setFitHeight(144);
        imageView.setFitWidth(200);

        addComponents(message);
        addListener();

        //show(600,400);
    }

    private void addComponents(String message) {

        int row=0;

        Label textArea_message = new Label(message);
        link = new Hyperlink();
        link.setText("Documentation");

        String text = "MitoBench is a tool aimed at helping researchers to organize, \n" +
                "visualize and maintain their mitochondrial data sets. Some functionality \n" +
                "is aimed towards generating population genetics statistics with \n" +
                "additional visualization.";

        dialogGrid.add(imageView,0,row,3,1);
        dialogGrid.add(new Separator(), 0, ++row, 3,1);
        dialogGrid.add(new Label( text), 0, ++row,3,1);
        dialogGrid.add(textArea_message, 0, ++row, 3,1);
        dialogGrid.add(link, 0, ++row, 3,1);
        dialogGrid.add(new Separator(), 0,++row, 3,1);
        dialogGrid.add(new Label("Development team currently includes Judith Neukamm (judith.neukamm@uzh.ch)\n" +
                "and Alexander Peltzer."), 0,++row,3,1);

    }

    private void addListener() {

        link.setOnAction(event -> new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("http://mitobench.readthedocs.io/en/latest/"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }).start());

    }

    /**
     * This method displays dialogue.
     */
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * This method displays dialogue.
     */
    public void show(int width, int height){
        Scene dialogScene = new Scene(dialogGrid, width, height);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void close(){
        dialog.close();
    }

    public GridPane getDialogGrid() {
        return dialogGrid;
    }
}
