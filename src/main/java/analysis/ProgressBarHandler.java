package analysis;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;

public class ProgressBarHandler {

    private ProgressBar progressBar;

    public ProgressBarHandler(){

    }

    public void create(){
        progressBar = new ProgressBar(0);
        progressBar.setDisable(true);
    }

    public void activate(ObservableValue task){
        progressBar.setProgress(0);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task);

    }

    public void stop(){
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        progressBar.setDisable(true);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }



}
