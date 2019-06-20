package view.dialogues.settings;


import Logging.LogClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;
import view.MitoBenchWindow;

import java.util.Arrays;


/**
 * Created by neukamm on 15.02.17.
 */
public class SettingsDialogueStackedBarchart extends AHGDialogue {

    private TableView<ObservableList> table;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");


    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Sub-Saharan Africa (L0a,L0d,L0k,L1b,L1c,L2a,L2b,L2c,L3b,L3d,L3e,L3f,L4,L5)",
                    "Americas and the Caribbean (A2,B2,C1b,C1c,C1d,C4c,D1,D2a,D3,D4h3a,X2a,X2g)",
                    "South-eastern Asia (M*,M7,M8,M9,G,D,N*,R*,R9,B)",
                    "Europe (H)");

    public SettingsDialogueStackedBarchart(String title, String[] groups, LogClass logClass) {
        super(title, logClass);
        dialogGrid.setId("stackedBarChartDialogue");

        addComponents(groups);
        allowDragAndDrop();

    }

    private void addComponents(String[] groups) {

        Label label_stackOrder = new Label("Please choose stack order.");
        label_stackOrder.setId("id_label_stackOrder");


        dialogGrid.add(new Separator(), 0,++row);
        dialogGrid.add(label_stackOrder, 0,++row);
        dialogGrid.add( setTable(groups), 0,++row);



    }

    private TableView setTable(String[] groups) {

        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        for(String s : groups){
            if(!s.equals("")){
                ObservableList data = FXCollections.observableArrayList(s);
                items.add(data);
            }

        }

        table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn groupsCol = new TableColumn("Groups");
        groupsCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>)
                param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        table.getColumns().add(groupsCol);
        table.getItems().addAll(items);
        return table;
    }


    private void allowDragAndDrop(){


        table.setRowFactory(tv -> {
            TableRow<ObservableList> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    ObservableList draggedPerson = table.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = table.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    table.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    table.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row ;
        });

    }

    public String[] getStackOrder(){
        ObservableList<ObservableList> items = table.getItems();
        String[] entries = new String[items.size()];
        for ( int i = 0; i < items.size(); i++){
            entries[i] = (String) items.get(i).get(0);
        }
        return entries;

    }
}
