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
            case "continent":
                return "Continent";
            case "country":
                return "Country";
            case "country_region":
                return "Region(Country)";
            case "culture_type":
                return "Culture Type";
            case "dating_lower":
                return "Dating lower";
            case "dating_upper":
                return "Dating upper";
            case "language":
                return "Language";
            case "location":
                return "Location";
            case "mito_type":
                return "Sequence type";
            case "population":
                return "Population";
            case "published":
                return "Published";
            case "reference":
                return "Reference Genome";
            case "reference_literature":
                return "Publication";
            case "region":
                return "Region(Continent)";


        }

        return colNameDB;
    }
}
