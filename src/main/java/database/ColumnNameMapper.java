package database;

/**
 * Created by neukamm on 03.02.2017.
 */
public class ColumnNameMapper {

    public ColumnNameMapper(){

    }

    public String mapString(String colNameDB){

        switch(colNameDB){
            case "sequence":
                return "MTSequence";
            case "haplogroup":
                return "Haplogroup";

        }

        return colNameDB;
    }
}
