package view.dialogues.settings;

import Logging.LogClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import view.MitoBenchWindow;

import java.util.List;

public class ColumnOrderDialogue extends ATabpaneDialogue{

    private final List<String> colnames;
    private final MitoBenchWindow mito;
    private Label label_info;
    private Button button_apply_list;
    private TableView table;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");


    public ColumnOrderDialogue(String title, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);

        LOG = this.logClass.getLogger(this.getClass());
        colnames = mito.getTableControllerUserBench().getCurrentColumnNames();
        this.mito = mito;
        dialogGrid.setId("custom_column_order_dialogue");
        addComponents();
        allowDragAndDrop();
        addListener();
    }


    private void addComponents() {

        label_info = new Label("You can define you own column order here.\nOrder can be defined via drag and drop.");

        button_apply_list = new Button("Apply");


        int row_index = 0;
        dialogGrid.add(label_info, 0,row_index,1,1);
        dialogGrid.add(setTable(colnames), 0,++row_index);
        dialogGrid.add(button_apply_list, 0,++row_index,1,1);

    }

    private TableView setTable(List<String> groups) {

        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        for(String s : groups){
            if(!s.equals("")){
                ObservableList data = FXCollections.observableArrayList(s);
                items.add(data);
            }

        }

        table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn groupsCol = new TableColumn("Column Order");
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
                    ObservableList draggedPerson = (ObservableList) table.getItems().remove(draggedIndex);

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


    private void addListener() {
        button_apply_list.setOnAction(e -> {

            String[] order = getStackOrder();
            mito.getTableControllerUserBench().setCustomColumnOrder(order);
            mito.getTableControllerUserBench().updateTable(mito.getTableControllerUserBench().getTable_content());
            mito.getTabpane_statistics().getTabs().remove(this.getTab());
        });
    }
}
