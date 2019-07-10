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
            case "mt_sequence":
                return "MTSequence";
            case "user_email":
                return "Submitter Email";
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
                return "Labsample Ident";
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
                return "TMA inferred Latitude";
            case "geographic_info_tma_inferred_longitude":
                return "TMA inferred Longitude";
            case "geographic_info_tma_inferred_region":
                return "TMA inferred Continent";
            case "geographic_info_tma_inferred_subregion":
                return "TMA inferred Subregion";
            case "geographic_info_tma_inferred_intermediate_region":
                return "TMA inferred Intermediate region";
            case "geographic_info_tma_inferred_city":
                return "TMA inferred City";
            case "geographic_info_tma_inferred_country":
                return "TMA inferred Country";
            case "marriage_rules":
                return "Marriage Rules";
            case "marriage_system":
                return "Marriage System";
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
            case "publication_type":
                return "Publication Type";
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
                return "Coverage (Min. depth)";
            case "maximum_coverage":
                return "Coverage (Max. depth)";
            case "mean_coverage":
                return "Coverage (mean)";
            case "std_dev_coverage":
                return "Coverage (SD)";
            case "calibrated_date_range_from":
                return "Calibrated Date lower limit";
            case "calibrated_date_range_to":
                return "Calibrated Date upper limit";
            case "c14_age_bp":
                return "C14 Radiocarbon Date";
            case "indirect_contextual_date":
                return "Indirect contextual Date";
            case "radiocarbon_lab_code":
                return "Lab code of radiocarbon dating";
            case "dating_comments":
                return "Dating comments";
            case "reference_genome":
                return "Reference Genome";
            case "starting_np":
                return "Starting position";
            case "ending_np":
                return "Ending position";
            case "sampling_latitude":
                return "Sampling Latitude";
            case "sampling_longitude":
                return "Sampling Longitude";
            case "sampling_region":
                return "Sampling Continent";
            case "sampling_subregion":
                return "Sampling Subregion";
            case "sampling_intermediate_region":
                return "Sampling Intermediate region";
            case "sampling_country":
                return "Sampling Country";
            case "sampling_city":
                return "Sampling City";
            case "sample_origin_latitude":
                return "Sample Latitude";
            case "sample_origin_longitude":
                return "Sample Longitude";
            case "sample_origin_region":
                return "Sample Continent";
            case "sample_origin_subregion":
                return "Sample Subregion";
            case "sample_origin_intermediate_region":
                return "Sample Intermediate region";
            case "sample_origin_country":
                return "Sample Country";
            case "sample_origin_city":
                return "Sample City";
            case "haplotype_current_versions":
                return "Haplotype";
            case "quality_haplotype_current_version":
                return "Haplotype Quality";
            case "haplogroup_current_versions":
                return "Haplogroup";
            case "macro_haplogroup":
                return "Macro Haplogroup";
            case "percentage_n":
                return "Percentage of N";
            case "completeness":
                return "Completeness";
            case "sequence_versions":
                return "Sequence versions";
            case "comments_sequence_version":
                return "Sequence version comments";
            case "comments":
                return "General comments";
            case "meta_info_id":
                return "mitoBenchID";

        }

        return colNameDB;
    }
}
