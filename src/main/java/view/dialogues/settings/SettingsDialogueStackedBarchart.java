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


/**
 * Created by neukamm on 15.02.17.
 */
public class SettingsDialogueStackedBarchart extends ATabpaneDialogue {

    private final MitoBenchWindow mito;
    private TableView<ObservableList> table;
    private Button applyBtn;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private TextField textField_hgList;
    private CheckBox default_list_checkbox;


    public SettingsDialogueStackedBarchart(String title, String[] groups, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);
        dialogGrid.setId("stackedBarChartDialogue");
        this.mito = mito;

        addComponents(groups);
        allowDragAndDrop();

    }

    private void addComponents(String[] groups) {
        applyBtn = new Button("Apply");
        applyBtn.setId("stackedBarApplyBtn");

        Label label_stackOrder = new Label("Please choose stack order.");
        label_stackOrder.setId("id_label_stackOrder");
        Label label_HgList = new Label("Please enter comma separated list of haplogroups \n " +
                "\n according to which the haplogroups should be grouped:") ;
        label_HgList.setId("id_label_HgList");

        Label default_list = new Label("or use the example list:");

        textField_hgList = new TextField();
        textField_hgList.setId("id_textField_hgList");

        if(mito.getChartController().getCustomHGList()!=null) {
            if (mito.getChartController().getCustomHGList().length != 0) {
                String hgs = "";
                for(String s : mito.getChartController().getCustomHGList())
                    hgs += s + ",";
                textField_hgList.setText(hgs.substring(0, hgs.length()-1));
            }
        }


        default_list_checkbox = new CheckBox("Use example list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        int row_index = 0;
        dialogGrid.add(label_HgList, 0,row_index);
        dialogGrid.add(textField_hgList, 0,++row_index);
        dialogGrid.add(default_list, 0,++row_index);
        dialogGrid.add(default_list_checkbox, 1,row_index);
        dialogGrid.add(new Separator(), 0,++row_index);
        dialogGrid.add(label_stackOrder, 0,++row_index);
        dialogGrid.add( setTable(groups), 0,++row_index);
        dialogGrid.add(applyBtn, 0,++row_index);


        Tooltip tp = new Tooltip("Default list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        default_list_checkbox.setOnMouseEntered(event -> {
            Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());

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

    public TextField getTextField_hgList() {
        return textField_hgList;
    }
    public Button getApplyBtn() {
        return applyBtn;
    }


    public void setApplyBtn(Button applyBtn) {
        this.applyBtn = applyBtn;
    }

    public void setTextField_hgList(TextField textField_hgList) {
        this.textField_hgList = textField_hgList;
    }

    public CheckBox getDefault_list_checkbox() {
        return default_list_checkbox;
    }

    public void setDefault_list_checkbox(CheckBox default_list_checkbox) {
        this.default_list_checkbox = default_list_checkbox;
    }
}
