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
                return "Labsample Id";
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

            case "ID":
                return "accession_id";
            case "MTSequence":
                return "mt_sequence";
            case "Submitter Email":
                return "user_email";
            case "Submitter firstname":
                return "user_firstname";
            case "Submitter surname":
                return "user_surname";
            case "Submitter affiliation":
                return "user_affiliation";
            case "Haplogroup (original)":
                return "haplogroup_originally_published";
            case "Data Type":
                return "data_type";
            case "Labsample ID":
                return "labsample_id";
            case "Sex":
                return "sex";
            case "Age":
                return "age";
            case "Population Purpose":
                return "population_purpose";
            case "Access":
                return "access";
            case "Language":
                return "language";
            case "Generations to TMA":
                return "generations_to_tma";
            case "TMA inferred Latitude":
                return "geographic_info_tma_inferred_latitude";
            case "TMA inferred Longitude":
                return "geographic_info_tma_inferred_longitude";
            case "TMA inferred Continent":
                return "geographic_info_tma_inferred_region";
            case "TMA inferred Subregion":
                return "geographic_info_tma_inferred_subregion";
            case "TMA inferred Intermediate region":
                return "geographic_info_tma_inferred_intermediate_region";
            case "TMA inferred City":
                return "geographic_info_tma_inferred_city";
            case "TMA inferred Country":
                return "geographic_info_tma_inferred_country";
            case "Marriage Rules":
                return "marriage_rules";
            case "Marriage System":
                return "marriage_system";
            case "Descent System":
                return "descent_system";
            case "Residence System":
                return "residence_system";
            case "Subsistence":
                return "subsistence";
            case "Clan":
                return "clan";
            case "Ethnicity":
                return "ethnicity";
            case "Population":
                return "population";
            case "DOI":
                return "doi";
            case "Author":
                return "author";
            case "Publication Date":
                return "publication_date";
            case "Journal":
                return "journal";
            case "Publication Title":
                return "title";
            case "Publication Type":
                return "publication_type";
            case "Publication Status":
                return "publication_status";
            case "Publication Comments":
                return "publication_comments";
            case "Tissue Sampled":
                return "tissue_sampled";
            case "Sampling Date":
                return "sampling_date";
            case "Sequencing Platform":
                return "sequencing_platform";
            case "Enrichment Method":
                return "enrichment_method";
            case "Extraction Protocol":
                return "extraction_protocol";
            case "Coverage (Min. depth)":
                return "minimum_coverage";
            case "Coverage (Max. depth)":
                return "maximum_coverage";
            case "Coverage (mean)":
                return "mean_coverage";
            case "Coverage (SD)":
                return "std_dev_coverage";
            case "Calibrated Date lower limit":
                return "calibrated_date_range_from";
            case "Calibrated Date upper limit":
                return "calibrated_date_range_to";
            case "C14 Radiocarbon Date":
                return "c14_age_bp";
            case "Indirect contextual Date":
                return "indirect_contextual_date";
            case "Lab code of radiocarbon dating":
                return "radiocarbon_lab_code";
            case "Dating comments":
                return "dating_comments";
            case "Reference Genome":
                return "reference_genome";
            case "Starting position":
                return "starting_np";
            case "Ending position":
                return "ending_np";
            case "Sampling Latitude":
                return "sampling_latitude";
            case "Sampling Longitude":
                return "sampling_longitude";
            case "Sampling Continent":
                return "sampling_region";
            case "Sampling Subregion":
                return "sampling_subregion";
            case "Sampling Intermediate region":
                return "sampling_intermediate_region";
            case "Sampling Country":
                return "sampling_country";
            case "Sampling City":
                return "sampling_city";
            case "Sample Latitude":
                return "sample_origin_latitude";
            case "Sample Longitude":
                return "sample_origin_longitude";
            case "Sample Continent":
                return "sample_origin_region";
            case "Sample Subregion":
                return "sample_origin_subregion";
            case "Sample Intermediate region":
                return "sample_origin_intermediate_region";
            case "Sample Country":
                return "sample_origin_country";
            case "Sample City":
                return "sample_origin_city";
            case "Haplotype":
                return "haplotype_current_versions";
            case "Haplotype Quality":
                return "quality_haplotype_current_version";
            case "Haplogroup":
                return "haplogroup_current_versions";
            case "Macro Haplogroup":
                return "macro_haplogroup";
            case "Percentage of N":
                return "percentage_n";
            case "Completeness":
                return "completeness";
            case "Sequence versions":
                return "sequence_versions";
            case "Sequence version comments":
                return "comments_sequence_version";
            case "General comments":
                return "comments";
            case "mitoBenchID":
                return "meta_info_id";

        }

        return colNameDB;
    }
}
