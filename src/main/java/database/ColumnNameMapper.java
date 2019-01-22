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
                return "Latitude (TMA inferred)";
            case "geographic_info_tma_inferred_longitude":
                return "Longitude (TMA inferred)";
            case "geographic_info_tma_inferred_region":
                return "Geographic Area (TMA inferred)";
            case "geographic_info_tma_inferred_subregion":
                return "Subregion (TMA inferred)";
            case "geographic_info_tma_inferred_intermediate_region":
                return "Intermediate region (TMA inferred)";
            case "geographic_info_tma_inferred_city":
                return "City (TMA inferred)";
            case "geographic_info_tma_inferred_country":
                return "Country (TMA inferred)";
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
                return "Minimum Depth of Coverage";
            case "maximum_coverage":
                return "Maximum Depth of Coverage";
            case "mean_coverage":
                return "Mean depth of Coverage";
            case "std_dev_coverage":
                return "Standard Deviation depth of Coverage";
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
                return "Latitude (Sampling)";
            case "sampling_longitude":
                return "Longitude (Sampling)";
            case "sampling_region":
                return "Geographic Area (sampling)";
            case "sampling_subregion":
                return "Subregion (sampling)";
            case "sampling_intermediate_region":
                return "Intermediate region (sampling)";
            case "sampling_country":
                return "Country (sampling)";
            case "sampling_city":
                return "City (sampling)";
            case "sample_origin_latitude":
                return "Latitude (Sample)";
            case "sample_origin_longitude":
                return "Longitude (Sample)";
            case "sample_origin_region":
                return "Geographic Area (Sample)";
            case "sample_origin_subregion":
                return "Subregion (Sample)";
            case "sample_origin_intermediate_region":
                return "Intermediate region (Sample)";
            case "sample_origin_country":
                return "Country (Sample)";
            case "sample_origin_city":
                return "City (Sample)";
            case "haplotype_current_versions":
                return "Haplotype";
            case "quality_haplotype_current_version":
                return "Haplotype Quality";
            case "haplogroup_current_versions":
                return "Haplogroup";
            case "macro_haplogroup":
                return "Macro Haplogroup";
            case "percentage_N":
                return "Percentage of N";
            case "completeness":
                return "Completeness";
            case "sequence_versions":
                return "Sequence versions";
            case "comments_sequence_version":
                return "Sequence version comments";

        }

        return colNameDB;
    }
}
