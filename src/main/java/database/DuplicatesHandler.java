package database;

import Logging.LogClass;
import io.datastructure.Entry;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.MitoBenchWindow;
import view.dialogues.settings.DuplicatesHandlerDialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DuplicatesHandler {
    private final DatabaseQueryHandler databaseQueryHandler;
    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private DuplicatesHandlerDialogue duplicatesHandlerDialogue;
    private HashMap<String, List<List<Entry>>> duplicates;
    private Stage stage_duplicates;
    private List<List<Entry>> entries_dup_to_upload = new ArrayList<>();
    private List<List<Entry>> duplicates_input;
    private DataUploader dataUploader;
    private List<String> accessionIDs=new ArrayList<>();
    private String accession;

    public DuplicatesHandler(MitoBenchWindow mito) {
        this.databaseQueryHandler = new DatabaseQueryHandler();
        this.logClass = mito.getLogClass();
        this.mito = mito;
        this.duplicates = new HashMap<>();

    }

    /**
     * Collect duplicates for later handling/checking.
     *
     * @param row
     * @param acc
     */
    public void collectDuplicates(List<Entry> row, String acc){

        if(!duplicates.keySet().contains(acc)){
            List l = new ArrayList();
            l.add(row);
            duplicates.put(acc, l);
        } else {
            List<List<Entry>> oldList = duplicates.get(acc);
            oldList.add(row);
            duplicates.put(acc, oldList);
        }

    }

    /**
     * Let the user decide what to do with the identified duplicates.
     */
    public void handle() {

        stage_duplicates = new Stage();
        stage_duplicates.initOwner(mito.getPrimaryStage());
        stage_duplicates.initModality(Modality.APPLICATION_MODAL);
        ScrollPane scrollPane = new ScrollPane();
        Scene scene = new Scene(scrollPane,700, 250);

        duplicatesHandlerDialogue = new DuplicatesHandlerDialogue("Duplicates handler", logClass);

        for (String acc : duplicates.keySet()){
            accession = acc;
            duplicatesHandlerDialogue.getDialogGrid().getChildren().clear();

            // get duplicates from input dataset
            duplicates_input = duplicates.get(acc);
            // get entry from database
            HashMap<Integer, List<Entry>> duplicates_database = databaseQueryHandler.getDuplicatesFromDatabase("accession_id=eq." + acc);

            // get all entry information from database entry
            List<String> author_DB = new ArrayList<>();
            List<String> labsample_id_DB = new ArrayList<>();
            for (int i : duplicates_database.keySet()){
                List<Entry> entries = duplicates_database.get(i);
                for (Entry entry : entries){
                    if(entry.getIdentifier().equals("Author")){
                        author_DB.add(entry.getData().getTableInformation());
                    } else if(entry.getIdentifier().equals("Labsample ID")){
                        labsample_id_DB.add(entry.getData().getTableInformation());
                    }
                }
            }

            // parse duplicates to dialogue and let user decide what to do
            for (List<Entry> row : duplicates_input){

                // compare with row
                List<String> author_row = new ArrayList<>();
                List<String> labsample_id_row = new ArrayList<>();
                for (Entry entry : row) {
                    if (entry.getIdentifier().equals("Author")) {
                        author_row.add(entry.getData().getTableInformation());
                    } else if (entry.getIdentifier().equals("Labsample ID")) {
                        labsample_id_row.add(entry.getData().getTableInformation());
                    }
                }

                duplicatesHandlerDialogue.fillGrid(
                        duplicates_database,
                        duplicates_input,
                        acc,
                        author_DB,
                        labsample_id_DB,
                        author_row,
                        labsample_id_row);

            }

            // implement button action
            addListener();

            //mito.getTabpane_statistics().getTabs().add(this.getTab());
            //mito.getTabpane_statistics().getSelectionModel().select(this.getTab());
            scrollPane.setContent(duplicatesHandlerDialogue.getDialogGrid());
            stage_duplicates.setScene(scene);
            stage_duplicates.showAndWait();

        }

        // upload all selected data
        ColumnNameMapper mapper = new ColumnNameMapper();
        if (entries_dup_to_upload.size()>0){

            for (int k = 0; k < entries_dup_to_upload.size(); k++){
                String[] header = new String[entries_dup_to_upload.get(k).size()+1];
                String[] types = new String[entries_dup_to_upload.get(k).size()+1];
                header[0] = mapper.mapString("ID");
                types[0] = "String";
                for (int i = 1; i < entries_dup_to_upload.get(k).size()+1; i++){
                    Entry entry = entries_dup_to_upload.get(k).get(i-1);
                    header[i] = mapper.mapString(entry.getIdentifier());
                    types[i] = entry.getType().getTypeInformation();
                }
                String id = accessionIDs.get(k);
                dataUploader.upload(header, types, entries_dup_to_upload.get(k), id,"","");
            }

        }
    }

    private void addListener() {
        duplicatesHandlerDialogue.getDiscard().setOnAction(e -> {
           stage_duplicates.close();
        });

        duplicatesHandlerDialogue.getUpload().setOnAction(e -> {
            // remove accession ID from entries to upload
            for (Entry entry : duplicates_input.get(0) ){
                if(entry.getIdentifier().equals("ID")){
                    duplicates_input.get(0).remove(entry);
                    break;
                }
            }

            // add to list for later upload
            entries_dup_to_upload.add(duplicates_input.get(0));
            accessionIDs.add(accession);
            stage_duplicates.close();
        });
    }

    public Tab getTab(){
        return duplicatesHandlerDialogue.getTab();
    }

    public int getNumOfDuplicates() {
        return duplicates.size();
    }

    public int getNumberOfUploadedDuplicates() {
        return 0;
    }

    public void startUpload() {
        dataUploader = new DataUploader(mito, this);
        dataUploader.parseMeta("data_to_upload.tsv", "", "");
    }
}
