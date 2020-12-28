package controller;

import Logging.LogClass;
import io.datastructure.Entry;
import javafx.collections.ObservableList;
import view.MitoBenchWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 01.02.17.
 */
public class TableControllerDB extends ATableController {



    public TableControllerDB(LogClass logClass){
        super(logClass);

    }

    public HashMap<String, List<Entry>> parseDataToEntrylist(){

        ObservableList<ObservableList> selected_rows = getSelectedRows();

        List<String> accessions = new ArrayList<>();
        for(ObservableList entry : selected_rows){
            accessions.add((String) entry.get(getColIndex("ID")));
        }

        HashMap<String, List<Entry>> entry_list_selection = new HashMap<>();
        for (String acc : accessions){
            entry_list_selection.put(acc, table_content.get(acc));
        }

        return entry_list_selection;

    }

}
