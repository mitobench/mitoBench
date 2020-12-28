package view.dialogues.settings;

import Logging.LogClass;
import analysis.HaplotypeCaller;
import controller.TableControllerUserBench;
import io.dialogues.Export.SaveAsDialogue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.MitoBenchWindow;
import view.visualizations.SampleTree;

import java.io.File;
import java.io.IOException;

public class SampleTreeTabPaneDialogue extends ATabpaneDialogue{

    private Button btn_saveSVG;
    private MitoBenchWindow mito;
    private TableControllerUserBench tablecontroller;
    private SampleTree sampleTree;
    private int row;


    public SampleTreeTabPaneDialogue(String title, LogClass logClass) {
        super(title, logClass);

        addComponents();
        addListener();

    }

    /**
     * Add all graphical components
     */
    private void addComponents() {

        row = 0;

        this.btn_saveSVG = new Button("Select folder");
        Label label_infotext = new Label(
                "This generates a phylogenetic tree based on the haplogroups.\n" +
                "This function is provided and executed by HaploGrep2.\n" +
                "The resulting tree cannot be displayed within the workbench\n" +
                "due to its size,however, it will be saved as dot and SVG file.\n" +
                "To create the tree, HaploGrep2 needs to be executed. Be aware\n" +
                "that this can take some minutes,\ndepending on the number of sample.\n\n" +
                "Please select the folder where all files will be saved");

        label_infotext.setMinWidth(GridPane.USE_PREF_SIZE);
        this.dialogGrid.add(label_infotext, 0,row,1,1);
        this.dialogGrid.add(new Separator(),0,++row,2,1);
        this.dialogGrid.add(btn_saveSVG, 0,++row,1,1);

    }

    /**
     * All all function to the graphical components
     */
    private void addListener() {

        this.btn_saveSVG.setOnAction(e -> {

            // ask for result directory
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Folder");

            String filepath = directoryChooser.showDialog(new Stage()).getAbsolutePath();
            String svgfile = filepath + File.separator + "tree.svg";

            if(svgfile != null) {

                HaplotypeCaller haplotypeCaller = new HaplotypeCaller(tablecontroller, logClass);
                String finalSvgfile = svgfile;
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {

                        haplotypeCaller.call("--lineage", filepath+File.separator);
                        return true;
                    }
                };

                mito.getProgressBarhandler().activate(task);

                task.setOnCancelled((EventHandler<Event>) event -> {
                    haplotypeCaller.deleteTmpFiles();
                    mito.getProgressBarhandler().stop();
                });


                task.setOnSucceeded((EventHandler<Event>) event -> {
                    sampleTree = new SampleTree("","", mito.getLogClass());

                    // read graphviz file
                    VBox bottom_content = new VBox();
                    bottom_content.setPadding(new Insets(10,10,10,10));
                    try {
                        sampleTree.start(finalSvgfile, filepath);
                        this.dialogGrid.add(new Separator(), 0,++row,1,1);
                        this.dialogGrid.add(new Label("Finished.\nSVG file is created: " + finalSvgfile), 0,++row,1,1);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    haplotypeCaller.deleteTmpFiles();
                    mito.getProgressBarhandler().stop();

                });
                new Thread(task).start();
            }
        });
    }


    public void setMito(MitoBenchWindow mito) {
        this.mito = mito;
    }

    public void setTableController(TableControllerUserBench tableController) {
        this.tablecontroller = tableController;
    }
}
