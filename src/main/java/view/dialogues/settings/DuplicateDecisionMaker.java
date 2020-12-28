package view.dialogues.settings;

import io.datastructure.Entry;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import view.MitoBenchWindow;

import java.util.List;

public class DuplicateDecisionMaker extends ATabpaneDialogue  {

    private final Stage stage_duplicates;
    private final ScrollPane scrollPane;
    private final Scene scene;
    private Button btn_keep_db_entry;
    private Button btn_keep_new_entry;
    private Button btn_keep_both;

    public DuplicateDecisionMaker(MitoBenchWindow mito){
        super("Duplicate Decision Maker", mito.getLogClass());

        stage_duplicates = mito.getPrimaryStage();
        scrollPane = new ScrollPane();
        scene = new Scene(scrollPane,700, 250);
    }

    public void addEntries(List<Entry> database_entrylist, List<Entry> new_entrylist, String accession) {
        btn_keep_db_entry = new Button("Keep 1");
        btn_keep_new_entry = new Button("Keep 2");
        btn_keep_both = new Button("Keep 1 and 2");

        dialogGrid.getChildren().clear();
        int row = 0;
        int col = 0;
        dialogGrid.add(new Label("Decision for Accession ID " + accession),col,row,1,1);
        ++row;
        for (int i = 0; i < database_entrylist.size(); i++){
            String identifier = database_entrylist.get(i).getIdentifier();
            Entry e_db = new_entrylist.get(i);
            String data_db = e_db.getData().getTableInformation();
            System.out.println(i + "_" + identifier);
            for (int j = 0; j < new_entrylist.size(); j++){
                Entry e_new = new_entrylist.get(j);
                String identifier_new = e_new.getIdentifier();
                String data_new = e_new.getData().getTableInformation();

                if(identifier.equals(identifier_new)){
                    System.out.println(j + "_" + identifier);
                    dialogGrid.add(new Label(identifier),col,row,1,1);
                    dialogGrid.add(new Label(data_db),col,++row,1,1);
                    dialogGrid.add(new Label(data_new),col,++row,1,1);
                    ++col;
                    row = row-3;
                    break;
                }
            }
        }

        row = row+3;
        dialogGrid.add(btn_keep_db_entry, col++, row,1,1);
        dialogGrid.add(btn_keep_new_entry, col++, row,1,1);
        dialogGrid.add(btn_keep_both, col++, row,1,1);
    }

    public void show(){
        scrollPane.setContent(dialogGrid);
        stage_duplicates.setScene(scene);
        stage_duplicates.showAndWait();
    }
}
