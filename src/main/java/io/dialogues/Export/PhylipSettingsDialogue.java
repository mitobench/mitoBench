package io.dialogues.Export;

import Logging.LogClass;
import controller.ChartController;
import controller.TableControllerUserBench;
import io.writer.PhyLipWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.dialogues.settings.APopupDialogue;


public class PhylipSettingsDialogue extends APopupDialogue {
    private final ObservableList<ObservableList> data;
    private final TableControllerUserBench tableController;
    private final ChartController cc;
    private final boolean exportAllData;
    private final String version;
    private Button ok;
    private ComboBox comboBox1;
    private ComboBox comboBox2;
    private Button back;


    public PhylipSettingsDialogue(String title, LogClass logClass,
                                  ObservableList<ObservableList> dataToExport,
                                  TableControllerUserBench tableController,
                                  String MITOBENCH_VERSION,
                                  ChartController cc,
                                  boolean exportAllData) {
        super(title, logClass);
        this.data = dataToExport;
        this.tableController = tableController;
        this.cc = cc;
        this.exportAllData = exportAllData;
        this.version = MITOBENCH_VERSION;
        addComponents();
        addListener();
        show();
    }



    private void addComponents() {
        comboBox1 = new ComboBox();
        ObservableList items = FXCollections.observableArrayList();
        items.addAll("Strict", "Relaxed");
        comboBox1.setItems(items);
        comboBox1.getSelectionModel().select("Strict");

        comboBox2 = new ComboBox();
        ObservableList items2 = FXCollections.observableArrayList();
        items2.addAll("Sequential", "Interleaved");
        comboBox2.setItems(items2);
        comboBox2.getSelectionModel().select("Interleaved");

        ok = new Button("OK");
        back = new Button("Back");

        dialogGrid.add(comboBox1, 0,0,1,1 );
        dialogGrid.add(comboBox2, 1,0,1,1 );
        dialogGrid.add(new Separator(), 0,1,2,1);
        dialogGrid.add(back, 0,2,1,1);
        dialogGrid.add(ok, 1,2,1,1);
    }

    private void addListener() {
        ok.setOnAction(t -> {
            close();
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Phylip (*.phylip)", "*.phylip");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            if (saveAsDialogue.getOutFile() != null) {
                String outFileDB = saveAsDialogue.getOutFile();
                try {
                    PhyLipWriter phyLipWriter = new PhyLipWriter(data,
                            comboBox1.getSelectionModel().getSelectedItem().toString(),
                            comboBox2.getSelectionModel().getSelectedItem().toString());
                    phyLipWriter.writeData(outFileDB, tableController);
                    LOG.info("Export data into Phylip format. File: " + outFileDB);
                } catch (Exception e) {
                    System.err.println("Caught Exception: " + e.getMessage());
                }
            }
        });

        back.setOnAction(t -> {
            close();
            ExportDialogue exportDialogue = new ExportDialogue(tableController, version, logClass,
                    cc, exportAllData);
            try {
                exportDialogue.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
