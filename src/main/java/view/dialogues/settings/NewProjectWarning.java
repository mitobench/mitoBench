package view.dialogues.settings;

import Logging.LogClass;
import io.dialogues.Export.ExportDialogue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

import javafx.stage.Stage;
import view.MitoBenchWindow;


public class NewProjectWarning extends APopupDialogue {

    private final MitoBenchWindow mito;
    private Button saveLogFileBtn;
    private Label infoLogFile;
    private Button discardLogFile;


    public NewProjectWarning(String title, LogClass logClass, MitoBenchWindow mitoBenchWindow) {
        super(title, logClass);

        this.mito = mitoBenchWindow;
        dialogGrid.setId("logDialogue");
        setComponents();
        addButtonListener();
        show();

    }

    private void setComponents() {

        infoLogFile = new Label("Do you want to save the current project?");
        saveLogFileBtn = new Button("Save project");
        saveLogFileBtn.setId("saveProjectBtn");
        discardLogFile = new Button("Discard project");
        discardLogFile.setId("discardProjectBtn");

        dialogGrid.add(infoLogFile, 0,0,2,1);
        dialogGrid.add(new Separator(), 0,1,3,1);
        dialogGrid.add(saveLogFileBtn, 1,2,1,1);
        dialogGrid.add(discardLogFile, 2,2,1,1);


    }

    private void addButtonListener() {

        saveLogFileBtn.setOnAction(e -> {
            if(mito.getTableControllerUserBench().getTable().getItems().size() != 0){
                ExportDialogue exportDialogue = new ExportDialogue(mito.getTableControllerUserBench(), mito.getMITOBENCH_VERSION(), logClass,
                        mito.getChartController(), true);

                try {
                    exportDialogue.start(new Stage());
                    resetProject();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }

        });


        discardLogFile.setOnAction(e -> {
            // clear all
           resetProject();

        });
    }



    private void resetProject(){
        mito.getTableControllerUserBench().cleartable();
        mito.getTabpane_visualization().getTabs().removeAll(mito.getTabpane_visualization().getTabs());
        mito.getTabpane_statistics().getTabs().removeAll(mito.getTabpane_statistics().getTabs());
        mito.setAnotherProjectLoaded(false);
        LOG.info("New project was created.");
        this.close();
    }

}
