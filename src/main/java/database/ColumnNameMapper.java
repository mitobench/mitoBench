package database;

/**
 * Created by neukamm on 03.02.2017.
 */
public class ColumnNameMapper {

    public ColumnNameMapper(){

    }

    public String mapString(String colNameDB){

        switch(colNameDB){
            case "accession_id":
                return "ID";
                //return "Accession ID";
            case "mt_sequence":
                return "MTSequence";
            case "user_email":
                return "Submitter (Email)";
            case "user_firstname":
                return "Submitter firstname";
            case "user_surname":
                return "Submitter surname";
            case "user_affiliation":
                return "Submitter affiliation";
            case "haplogroup_originally_published":
                return "Haplogroup (original)";
            case "data_type":
                return "Data Type";
            case "labsample_id":
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
            case "generations_to_tma":
                return "Generations to TMA";
            case "geographic_info_tma_inferred_latitude":
                return "Latitude (TMA inferred)";
            case "geographic_info_tma_inferred_longitude":
                return "Longitude (TMA inferred)";
            case "geographic_info_tma_inferred_region_m49":
                return "Geographic Area (TMA inferred)";
            case "geographic_info_tma_inferred_subregion_m49":
                return "Subregion (TMA inferred)";
            case "geographic_info_tma_inferred_intermediate_region_m49":
                return "Intermediate region (TMA inferred)";
            case "geographic_info_tma_inferred_city":
                return "City (TMA inferred)";
            case "geographic_info_tma_inferred_country_m49":
                return "Country (TMA inferred)";
            case "marriage_rules":
                return "Marriage Rules";
            case "descent_system":
                return "Descent System";
            case "residence_system":
                return "Residence System";
            case "subsistence":
                return "Subsistence";
            case "clan":
                return "Clan";
            case "ethnicity":
                return "Ethnicity";
            case "population":
                return "Population";
            case "doi":
                return "DOI";
            case "author":
                return "Author";
            case "publication_date":
                return "Publication Date";
            case "journal":
                return "Journal";
            case "title":
                return "Publication Title";
            case "reference_type":
                return "Publication Reference Type";
            case "publication_status":
                return "Publication Status";
            case "publication_comments":
                return "Publication Comments";
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
            case "uncalibrated_date":
                return "Radiocarbon Date";
            case "reference_genome":
                return "Reference Gnome";
            case "starting_np":
                return "Starting position";
            case "ending_np":
                return "Ending position";
            case "sampling_origin_latitude":
                return "Latitude (Sampling)";
            case "sampling_origin_longitude":
                return "Longitude (Sampling)";
            case "sampling_origin_region_m49":
                return "Geographic Area (sampling)";
            case "sampling_origin_subregion_m49":
                return "Subregion (sampling)";
            case "Sampling_origin_intermediate_region_m49":
                return "Intermediate region (sampling)";
            case "sampling_origin_country_m49":
                return "Country (sampling)";
            case "sampling_origin_city":
                return "City (sampling)";
            case "sampling_comments":
                return "Comments (sampling)";
            case "sample_origin_latitude":
                return "Latitude (Sample)";
            case "sample_origin_longitude":
                return "Longitude (Sample)";
            case "sample_origin_region_m49":
                return "Geographic Area (Sample)";
            case "sample_origin_subregion_m49":
                return "Subregion (Sample)";
            case "sample_origin_intermediate_region_m49":
                return "Intermediate region (Sample)";
            case "sample_origin_country_m49":
                return "Country (Sample)";
            case "sample_origin_city":
                return "City (Sample)";
            case "haplotype_current_versions":
                return "Haplotype";
            case "haplogroup_current_versions":
                return "Haplogroup";
            case "macro_haplogroup":
                return "Macro Haplogroup";
            case "percentage_n":
                return "Percentage of N";
            case "completeness":
                return "Completeness";

        }

        return colNameDB;
    }
}
