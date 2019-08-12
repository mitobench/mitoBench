package analysis.fstcalculation;

import java.util.ArrayList;
import java.util.List;

public class DistanceTypeParser {

    List<String> types = new ArrayList<>();

    public DistanceTypeParser(){

        types.add("Pairwise Difference");
        types.add("Percentage Difference");
        types.add("Jukes and Cantor");
        types.add("Kimura 2-parameters");
        types.add("Tamura");


    }

    public int parse(String type){
        return types.indexOf(type);
    }
}
