package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DuplicatesChecker {

    private final DatabaseQueryHandler databaseQueryHandler;

    public DuplicatesChecker(DatabaseQueryHandler databaseQueryHandler){
        this.databaseQueryHandler = databaseQueryHandler;
    }



    public void check(List<String> fasta_headers) {
        // get all accession IDs from database
        Set<String> accessionIDs = databaseQueryHandler.getAccessionIDs();
        List<String> duplicates = new ArrayList<>();

        for (String acc_to_upload : fasta_headers){
            if (accessionIDs.contains(acc_to_upload)){
                duplicates.add(acc_to_upload);
            }
        }

        // todo
        for (String dup : duplicates)
            System.out.println(dup);

    }
}
