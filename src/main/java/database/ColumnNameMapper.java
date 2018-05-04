package database;

/**
 * Created by neukamm on 03.02.2017.
 */
public class ColumnNameMapper {

    public ColumnNameMapper(){

    }

    public String mapString(String colNameDB){

        switch(colNameDB){
            case "haplotype_current_versions":
                return "Haplotype";
            case "haplogroup_originally_published":
                return "Haplogroup (original)";
            case "haplogroup_current_versions":
                return "Haplogroup";
            case "macro_haplogroup":
                return "Macro Haplogroup";
            case "data_type":
                return "Data Type";
            case "accession_ID":
                return "Accession ID";
            case "labSample_ID":
                return "Labsample ID";
            case "sex":
                return "Sex";
            case "age":
                return "Age";
            case "population_purpose":
                return "Population Purpose";
            case "access":
                return "Access";
            case "language":
                return "Language";
            case "generations_to_TMA":
                return "Generations to TMA";
            case "geographic_info_TMA_inferred_latitude":
                return "Latitude (TMA inferred)";
            case "geographic_info_TMA_inferred_longitude":
                return "Longitude (TMA inferred)";
            case "geographic_info_TMA_inferred_geographic_area_m49":
                return "Geographic Area (TMA inferred)";
            case "geographic_info_TMA_inferred_subregion_m49":
                return "Subregion (TMA inferred)";
            case "geographic_info_TMA_inferred_city":
                return "City (TMA inferred)";
            case "geographic_info_TMA_inferred_country_m49":
                return "Country (TMA inferred)";
            case "marriage_rules":
                return "Marriage Rles";
            case "descent_system":
                return "Descent System";
            case "residence_system":
                return "Residence System";
            case "subsistence":
                return "Subsistence";
            case "clan":
                return "Clan";
            case "ehnicity":
                return "Ehnicity";
            case "population":
                return "Population";
            case "doi":
                return "DOI";
            case "author":
                return "Author";
            case "publication_date":
                return "Publication Date";
            case "title":
                return "Publication Title";
            case "journal":
                return "Journal";
            case "reference_type":
                return "Reference Type";
            case "publication_status":
                return "Publication Status";
            case "publication_comments":
                return "Publication Comments";
            case "mt_sequence":
                return "MTSequence";
            case "percentage_of_N":
                return "Percentage of N";
            case "completeness":
                return "Completeness";
            case "tissue_sampled":
                return "Tissue Sampled";
            case "sampling_date":
                return "Sampling Date";
            case "sequencing_platform":
                return "Sequencing Platform";
            case "enrichment_method":
                return "Enrichment Method";
            case "extraction_protocol":
                return "Extraction Protocol";
            case "minimum_coverage":
                return "Minimum Depth of Coverage";
            case "maximum_coverage":
                return "Maximum Depth of Coverage";
            case "mean_coverage":
                return "Mean depth of Coverage";
            case "std_dev_coverage":
                return "Standard Deviation depth of Coverage";
            case "calibrated_date":
                return "Calibrated Date";
            case "radio_carbon_date":
                return "Radiocarbon Date";
            case "reference_genome":
                return "Reference Gnome";
            case "starting_np":
                return "Starting np";
            case "ending_np":
                return "Ending np";
            case "sampling_latitude":
                return "Latitude (Sampling)";
            case "sampling_longitude":
                return "Longitude (Sampling)";
            case "sampling_geographic_area_m49":
                return "Geographic Area (sampling)";
            case "sampling_subregion_m49":
                return "Subregion Area (sampling)";
            case "sampling_country_m49":
                return "Country Area (sampling)";
            case "sampling_city":
                return "City Area (sampling)";
            case "sampling_comments":
                return "Comments (sampling)";
            case "sample_origin_latitude":
                return "Latitude (Sample origin)";
            case "sample_origin_longitude":
                return "Longitude (Sample origin)";
            case "sample_origin_geographic_area_m49":
                return "Geographic Area (Sample origin)";
            case "sample_origin_subregion_m49":
                return "Subregion (Sample origin)";
            case "sample_origin_country_m49":
                return "Country (Sample origin)";
            case "sample_origin_city":
                return "City (Sample origin)";
            case "user_email":
                return "Submitter (Email)";

        }

        return colNameDB;
    }
}
