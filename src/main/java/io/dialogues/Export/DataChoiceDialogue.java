package io.dialogues.Export;

import javafx.scene.control.ChoiceDialog;

import java.util.List;
import java.util.Optional;

/**
 * Created by peltzer on 30/11/2016.
 */
public class DataChoiceDialogue {
    private String selected;

    public DataChoiceDialogue(List<String> options) {
        ChoiceDialog<String> chd = new ChoiceDialog<String>(options.get(0), options);
        chd.setTitle("Choose Group");
        chd.setHeaderText("Choose group for export categorization.");
        Optional<String> result = chd.showAndWait();
        if (result.isPresent()) {
            this.selected = result.get();
        } else {
            try {
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public String getSelected(){
        return this.selected;
    }
}
