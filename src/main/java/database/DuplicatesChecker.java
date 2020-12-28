package database;

import io.datastructure.Entry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DuplicatesChecker {

    private final DatabaseQueryHandler databaseQueryHandler;

    public DuplicatesChecker(DatabaseQueryHandler databaseQueryHandler){
        this.databaseQueryHandler = databaseQueryHandler;
    }


    public boolean isDuplicate(String acc) {
        // get all accession IDs from database
        Set<String> accessionIDs_DB = new HashSet<>(databaseQueryHandler.getColumnSet("accession_id", "String"));

        if(accessionIDs_DB.contains(acc)){
            System.out.println("Duplicated Accession ID detected. Getting entry from database" );
            return true;
        } else {
            return false;
        }

    }
}
