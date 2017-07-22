package view.dialogues.settings;


import Logging.LogClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 15.02.17.
 */
public class AdvancedStackedBarchartDialogue  extends APopupDialogue {

    private TableView<ObservableList> table;
    private Button applyBtn;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private TextField textField_hgList;


    public AdvancedStackedBarchartDialogue(String title, String[] groups, LogClass logClass) {
        super(title, logClass);
        dialogGrid.setId("stackedBarChartDialogue");

        addComponents(groups);
        allowDragAndDrop();

        show(300,300);
    }

    private void addComponents(String[] groups) {
        applyBtn = new Button("Apply");
        applyBtn.setId("stackedBarApplyBtn");

        Label label_stackOrder = new Label("Please choose stack order.");
        label_stackOrder.setId("id_label_stackOrder");
        Label label_HgList = new Label("Please choose Haplogroups.");
        label_HgList.setId("id_label_HgList");

        textField_hgList = new TextField("H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        textField_hgList.setId("id_textField_hgList");

        dialogGrid.add(label_HgList, 0,0);
        dialogGrid.add(textField_hgList, 0,1);
        dialogGrid.add(new Separator(), 0,2);
        dialogGrid.add(label_stackOrder, 0,3);
        dialogGrid.add( setTable(groups), 0,4);
        dialogGrid.add(applyBtn, 0,5);

    }

    private TableView setTable(String[] groups) {

        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        for(String s : groups){
            if(!s.equals("Undefined")){
                ObservableList data = FXCollections.observableArrayList(s);
                items.add(data);
            }

        }

        table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn groupsCol = new TableColumn("Groups");
        groupsCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });

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

    public TextField getTextField_hgList() {
        return textField_hgList;
    }

    public Button getApplyBtn() {
        return applyBtn;
    }

}
