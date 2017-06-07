package view.dialogues.settings;

import Logging.LogClass;
import analysis.FstCalculationRunner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import view.MitoBenchWindow;

import java.io.IOException;

/**
 * Created by neukamm on 07.06.17.
 */
public class FstSettingsDialogue extends APopupDialogue{

    private Button okBtn;
    private CheckBox linearized_slatkin;
    private CheckBox linearized_reynolds;
    private MitoBenchWindow mito;

    public FstSettingsDialogue(String title, LogClass logClass) {
        super(title, logClass);
        dialogGrid.setId("fst_popup");
        this.LOG = this.logClass.getLogger(this.getClass());
        show();
    }

    public void init(MitoBenchWindow mito){
        this.mito = mito;
        addComponents();
        addListener();
    }



    private void addComponents() {
        Label label = new Label("This method calculates the pairwise Fst value." +
                "\nPredefined settings:" +
                "\n\t- compute distance matrix" +
                "\n\t- missing data threshold: 5%");

        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        linearized_slatkin = new CheckBox("Slatkin's distance");
        linearized_slatkin.setId("checkbox_slatkin");
        linearized_slatkin.setSelected(false);
        linearized_reynolds = new CheckBox("Reynolds's distance");
        linearized_reynolds.setId("checkbox_reynolds");
        linearized_reynolds.setSelected(false);

        dialogGrid.add(label, 0,0,3,4);
        dialogGrid.add(linearized_slatkin, 0,5,3,1);
        dialogGrid.add(linearized_reynolds,0,6,1,1);
        dialogGrid.add(okBtn,2,7,1,1);


    }

    private void addListener() {

        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                try {
                    FstCalculationRunner fstCalculationRunner = new FstCalculationRunner(mito);
                    fstCalculationRunner.run(linearized_slatkin.isSelected(), linearized_slatkin.isSelected());

                    System.out.println("fst run....");
                    TableView table = fstCalculationRunner.writeToTable();
                    Tab tab = new Tab();
                    tab.setId("tab_fstCalc");
                    tab.setText("Fst values");
                    tab.setContent(table);
                    mito.getTabpane_statistics().getTabs().add(tab);
                    mito.getTabpane_statistics().getSelectionModel().select(tab);


                    System.out.println("wrote to table");
                    logClass.getLogger(this.getClass()).info("Calculate pairwise Fst " +
                            "values between following groups:\n" + fstCalculationRunner.getGroupnames());

                    dialog.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


    }

}
