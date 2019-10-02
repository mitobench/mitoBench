package model;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CancelButton extends Button {

    private Task task;
    private ProgressBar progressbar;

    public CancelButton(){
        ImageView view_open = new ImageView(new Image(getClass().getResourceAsStream("/icons/cancel.png")));
        view_open.setFitHeight(20);
        view_open.setFitWidth(20);
        this.setGraphic(view_open);
        this.setDisable(true);

        this.setPadding(new Insets(5,5,5,5));
        setAction();
    }


    private void setAction(){
        this.setOnAction(e -> {
                    task.cancel();
                    progressbar.progressProperty().unbind();
                    progressbar.setDisable(true);
                    this.setDisable(true);
                    System.out.println("....Task cancelled.");
                }
        );

    }

    public void activate(Task task, ProgressBar progressBar) {
        this.task = task;
        this.progressbar = progressBar;

    }
}
