package view.menus;

import controller.FileReaderController;
import io.dialogues.Export.ExportDialogue;
import io.dialogues.Import.IImportDialogue;
import io.dialogues.Import.IImportDialogueFactory;
import io.dialogues.Import.ImportDialogueAlternative;
import io.dialogues.Import.ImportDialogueFactoryImpl;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import view.MitoBenchWindow;

import java.util.Enumeration;
import java.util.Properties;

public class Toolbarpane extends ToolBar {

    private final FileReaderController fileReaderController;
    private final Stage stage;
    private final MitoBenchWindow mito;
    private Button goBackBtn;
    private Button openFileBtn;
    private Button saveprojectBtn;
    private IImportDialogueFactory importDialogueFactory;



    public Toolbarpane(FileReaderController fileReaderController, MitoBenchWindow mitoBenchWindow){
        create();
        addFunctionality();

        this.fileReaderController = fileReaderController;
        this.stage = mitoBenchWindow.getPrimaryStage();
        this.mito = mitoBenchWindow;
        importDialogueFactory = new ImportDialogueFactoryImpl();

    }


    private void create(){
        openFileBtn = new Button();
        saveprojectBtn = new Button();
        goBackBtn = new Button();

        //Set the icon/graphic for the ToolBar Buttons.
        openFileBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open-archive.png"))));
        saveprojectBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/save-file-option.png"))));
        goBackBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/left-arrow-angle.png"))));

        openFileBtn.setTooltip(new Tooltip("Import file"));
        saveprojectBtn.setTooltip(new Tooltip("Export all data"));
        goBackBtn.setTooltip(new Tooltip("Go one filtering step back"));
        this.getItems().addAll(openFileBtn, saveprojectBtn, goBackBtn);
    }


    private void addFunctionality() {
        openFileBtn.setOnAction(e -> {
            IImportDialogue importDialogue;
            importDialogue = importDialogueFactory.create(stage);
            importDialogue.start();
           fileReaderController.openFile(importDialogue.getSelectedFile());


        });

        saveprojectBtn.setOnAction(t -> {

            if(mito.getTableControllerUserBench().getTable().getItems().size() != 0){
                ExportDialogue exportDialogue = new ExportDialogue(mito.getTableControllerUserBench(), mito.getMITOBENCH_VERSION(), mito.getLogClass(),
                        mito.getChartController(), true);
                try {
                    exportDialogue.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        goBackBtn.setOnAction(e -> {
            mito.getTableControllerUserBench().resetToUnfilteredData();
        });
    }

    /**
     * This method tests based on a user defined System.property whether java is in
     * testing mode.
     *
     * @return
     */
    public static boolean isJUnitTest() {
        Properties props = System.getProperties();
        Enumeration e = props.propertyNames();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if(key.startsWith("javafx.running")){
                if(props.getProperty("javafx.running").equals("true")){
                    return true;
                }

            }
        }
        return false;
    }




}
