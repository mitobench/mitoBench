package view.dialogues.settings;

import Logging.LogClass;
import controller.TableControllerDuplicates;
import io.datastructure.Entry;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

public class DuplicatesHandlerDialogue extends ATabpaneDialogue{
    private final TableControllerDuplicates tableControllerDuplicates;
    private Button upload;
    private Button discard;

    public DuplicatesHandlerDialogue(String title, LogClass logClass) {
        super(title, logClass);

        tableControllerDuplicates = new TableControllerDuplicates(logClass);
    }



    public void fillGrid(HashMap<Integer,
            List<Entry>> duplicates_database,
                         List<List<Entry>> duplicates_input,
                         String acc,
                         List<String> author_DB,
                         List<String> labsample_id_DB,
                         List<String> author_row,
                         List<String> labsample_id_row)
    {

        //tableControllerDuplicates.updateTable(duplicates_database);
        Text label_input=new Text("Data from input dataset");
        label_input.setStyle("-fx-font-weight: bold");

        Text label_db=new Text("Data from database");
        label_db.setStyle("-fx-font-weight: bold");

        upload = new Button("Upload");
        discard = new Button("Discard");

        int row = 0;
        int col = 0;

        dialogGrid.add(new Label("Sample with accession ID: "+acc), col,row,1,1);
        dialogGrid.add(new Separator(), col,++row,5,1);
        dialogGrid.add(label_db, col,++row,1,1);
        dialogGrid.add(new Label("Accession ID"), col,++row,1,1);
        dialogGrid.add(new Label("Labsample ID"), ++col,row,1,1);
        dialogGrid.add(new Label("Author"), ++col,row,1,1);

        col=0;
        for (int i = 0; i < duplicates_database.size(); i++){
            dialogGrid.add(new Label(acc), col++,++row,1,1);
            dialogGrid.add(new Label(labsample_id_DB.get(i)), col++,row,1,1);
            dialogGrid.add(new Label(author_DB.get(i)), col++,row,1,1);
            col=0;
        }
        col=0;

        dialogGrid.add(new Separator(), 0,++row,5,1);
        dialogGrid.add(label_input, col,++row,1,1);
        col=0;
        dialogGrid.add(new Label("Accession ID"), col,++row,1,1);
        dialogGrid.add(new Label("Labsample ID"), ++col,row,1,1);
        dialogGrid.add(new Label("Author"), ++col,row,1,1);

        col=0;
        for (int i = 0; i < duplicates_input.size(); i++){
            dialogGrid.add(new Label(acc), col++,++row,1,1);
            dialogGrid.add(new Label(labsample_id_row.get(i)), col++,row,1,1);
            dialogGrid.add(new Label(author_row.get(i)), col++,row,1,1);
            dialogGrid.add(upload, ++col, row, 1,1);
            dialogGrid.add(discard, ++col, row, 1,1);
            col=0;
        }
    }

    public Button getUpload() {
        return upload;
    }

    public Button getDiscard() {
        return discard;
    }

}
