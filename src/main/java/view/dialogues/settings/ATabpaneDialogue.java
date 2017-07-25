package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

/**
 * Created by neukamm on 09.02.17.
 */
public abstract class ATabpaneDialogue {


    private final Tab tab;
    protected GridPane dialogGrid;
    protected LogClass logClass;
    protected Logger LOG;

    public ATabpaneDialogue(String title, LogClass logClass){

        tab = new Tab();

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        tab.setContent(dialogGrid);
        tab.setText(title);

        this.logClass = logClass;
        LOG = logClass.getLogger(this.getClass());

    }

    public GridPane getDialogGrid() {
        return dialogGrid;
    }
    public Tab getTab() { return tab; }
}

