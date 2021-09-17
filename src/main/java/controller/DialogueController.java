package controller;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;


public class DialogueController {

    private final MitoBenchWindow mito;
    private Logger log;

    public DialogueController(MitoBenchWindow mito){

        this.mito = mito;
        this.log = mito.getLogClass().getLogger(this.getClass());
    }



    public void unalignedDialogue_setCancel_btn(Button cancel_btn, Stage dialog) {
        cancel_btn.setOnAction(e -> {
            dialog.close();
        });

    }
}

