package analysis;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import model.CancelButton;

public class ProgressBarHandler {

    private final CancelButton btn_cancel;
    private ProgressBar progressBar;

    public ProgressBarHandler(CancelButton btn_cancel){
        this.btn_cancel = btn_cancel;

    }

    public void create(){
        progressBar = new ProgressBar(0);
        progressBar.setDisable(true);
        progressBar.setPadding(new Insets(5,5,5,5));
    }

    public void activate(Task task){
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        progressBar.progressProperty().bind(task.progressProperty());
        btn_cancel.setDisable(false);
        btn_cancel.activate(task, progressBar);

    }

    public void stop(){
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        progressBar.setDisable(true);
        btn_cancel.setDisable(true);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }



}
