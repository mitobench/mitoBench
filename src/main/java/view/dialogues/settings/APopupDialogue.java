package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Created by neukamm on 09.02.17.
 */
public abstract class APopupDialogue {


    protected GridPane dialogGrid;
    protected Stage dialog;
    protected LogClass logClass;
    protected Logger LOG;

    public APopupDialogue(String title, LogClass logClass){
        dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        this.logClass = logClass;
        LOG = logClass.getLogger(this.getClass());

    }

    /**
     * This method displays dialogue.
     */
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void close(){
        dialog.close();
    }
}
