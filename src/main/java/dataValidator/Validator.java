package dataValidator;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Validator {

    private List<String> country = Arrays.asList("Algeria","Egypt", "Libya", "Morocco", "Sudan", "Tunisia", "Western Sahara",
            "British Indian Ocean Territory", "Burundi", "Comoros", "Djibouti", "Eritrea", "Ethiopia", "French Southern Territories",
            "Kenya", "Madagascar", "Malawi", "Mauritius", "Mayotte", "Mozambique", "Reunion", "Rwanda", "Seychelles", "Somalia",
            "South Sudan", "Uganda", "United Republic of Tanzania", "Zambia", "Zimbabwe", "Angola", "Cameroon", "Central African Republic",
            "Chad", "Congo", "Democratic Republic of the Congo", "Equatorial Guinea", "Gabon", "Sao Tome and Principe", "Botswana",
            "Eswatini", "Lesotho", "Namibia", "South Africa", "Benin", "Burkina Faso", "Cape Verde", "Ivory Coast", "Gambia",
            "Ghana", "Guinea", "Guinea-Bissau", "Liberia", "Mali", "Mauritania", "Niger", "Nigeria", "Saint Helena", "Senegal",
            "Sierra Leone", "Togo", "Anguilla", "Antigua and Barbuda", "Aruba", "Bahamas", "Barbados", "Bonaire/Sint Eustatius/Saba",
            "British Virgin Islands", "Cayman Islands", "Cuba", "Curacao", "Dominica", "Dominican Republic", "Grenada", "Guadeloupe",
            "Haiti", "Jamaica", "Martinique", "Montserrat", "Puerto Rico", "Saint Barthelemy", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Martin (French Part)", "Saint Vincent and the Grenadines", "Sint Maarten (Dutch part)", "Trinidad and Tobago",
            "Turks and Caicos Islands", "United States Virgin Islands", "Belize", "Costa Rica", "El Salvador", "Guatemala",
            "Honduras", "Mexico", "Nicaragua", "Panama", "Argentina", "Bolivia (Plurinational State of)", "Bouvet Island",
            "Brazil", "Chile", "Colombia", "Ecuador", "Falkland Islands (Malvinas)", "French Guiana", "Guyana", "Paraguay",
            "Peru", "South Georgia and the South Sandwich Islands", "Suriname", "Uruguay", "Venezuela (Bolivarian Republic of)",
            "Bermuda", "Canada", "Greenland", "Saint Pierre and Miquelon", "United States of America", "Antarctica", "Kazakhstan",
            "Kyrgyzstan", "Tajikistan", "Turkmenistan", "Uzbekistan", "China", "China/Hong Kong Special Administrative Region",
            "China/Macao Special Administrative Region", "Taiwan", "Democratic Peoples Republic of Korea", "Japan", "Mongolia",
            "Republic of Korea", "Brunei Darussalam", "Cambodia", "Indonesia", "Lao People s Democratic Republic", "Malaysia",
            "Myanmar", "Philippines", "Singapore", "Thailand", "Timor-Leste", "Viet Nam", "Afghanistan", "Bangladesh", "Bhutan",
            "India", "Iran (Islamic Republic of)", "Maldives", "Nepal", "Pakistan", "Sri Lanka", "Armenia", "Azerbaijan", "Bahrain",
            "Cyprus", "Georgia", "Iraq", "Israel", "Jordan", "Kuwait", "Lebanon", "Oman", "Qatar", "Saudi Arabia", "State of Palestine",
            "Syrian Arab Republic", "Turkey", "United Arab Emirates", "Yemen", "Belarus", "Bulgaria", "Czechia", "Hungary",
            "Poland", "Republic of Moldova", "Romania", "Russian Federation", "Slovakia", "Ukraine", "Aland Islands", "Guernsey",
            "Jersey", "Denmark", "Estonia", "Faroe Islands", "Finland", "Iceland", "Ireland", "Isle of Man", "Latvia", "Lithuania",
            "Norway", "Svalbard and Jan Mayen Islands", "Sweden", "United Kingdom of Great Britain and Northern Ireland", "Albania",
            "Andorra", "Bosnia and Herzegovina", "Croatia", "Gibraltar", "Greece", "Holy See", "Italy", "Malta", "Montenegro",
            "Portugal", "San Marino", "Kosovo", "Serbia", "Slovenia", "Spain", "The former Yugoslav Republic of Macedonia", "Austria",
            "Belgium", "France", "Germany", "Liechtenstein", "Luxembourg", "Monaco", "Netherlands", "Switzerland", "Australia",
            "Christmas Island", "Cocos (Keeling) Islands", "Heard Island and McDonald Islands", "New Zealand", "Norfolk Island",
            "Fiji", "New Caledonia", "Papua New Guinea", "Solomon Islands", "Vanuatu", "Guam", "Kiribati", "Marshall Islands",
            "Micronesia (Federated States of)", "Nauru", "Northern Mariana Islands", "Palau", "United States Minor Outlying Islands",
            "American Samoa", "Cook Islands", "French Polynesia", "Niue", "Pitcairn", "Samoa", "Tokelau", "Tonga", "Tuvalu", "Wallis and Futuna Islands",
            "DZA","EGY","LBY","MAR","SDN","TUN","ESH","IOT","BDI","COM","DJI","ERI","ETH","ATF","KEN","MDG","MWI","MUS",
            "MYT","MOZ","REU","RWA","SYC","SOM","SSD","UGA","TZA","ZMB","ZWE","AGO","CMR","CAF","TCD","COG","COD","GNQ",
            "GAB","STP","BWA","SWZ","LSO","NAM","ZAF","BEN","BFA","CPV","CIV","GMB","GHA","GIN","GNB","LBR","MLI","MRT",
            "NER","NGA","SHN","SEN","SLE","TGO","AIA","ATG","ABW","BHS","BRB","BES","VGB","CYM","CUB","CUW","DMA","DOM",
            "GRD","GLP","HTI","JAM","MTQ","MSR","PRI","BLM","KNA","LCA","MAF","VCT","SXM","TTO","TCA","VIR","BLZ","CRI",
            "SLV","GTM","HND","MEX","NIC","PAN","ARG","BOL","BVT","BRA","CHL","COL","ECU","FLK","GUF","GUY","PRY","PER",
            "SGS","SUR","URY","VEN","BMU","CAN","GRL","SPM","USA","ATA","KAZ","KGZ","TJK","TKM","UZB","CHN","HKG","MAC",
            "TWN","PRK","JPN","MNG","KOR","BRN","KHM","IDN","LAO","MYS","MMR","PHL","SGP","THA","TLS","VNM","AFG","BGD",
            "BTN","IND","IRN","MDV","NPL","PAK","LKA","ARM","AZE","BHR","CYP","GEO","IRQ","ISR","JOR","KWT","LBN","OMN",
            "QAT","SAU","PSE","SYR","TUR","ARE","YEM","BLR","BGR","CZE","HUN","POL","MDA","ROU","RUS","SVK","UKR","ALA",
            "GGY","JEY","DNK","EST","FRO","FIN","ISL","IRL","IMN","LVA","LTU","NOR","SJM","SWE","GBR","ALB","AND","BIH",
            "HRV","GIB","GRC","VAT","ITA","MLT","MNE","PRT","SMR","XKX","SRB","SVN","ESP","MKD","AUT","BEL","FRA","DEU",
            "LIE","LUX","MCO","NLD","CHE","AUS","CXR","CCK","HMD","NZL","NFK","FJI","NCL","PNG","SLB","VUT","GUM","KIR",
            "MHL","FSM","NRU","MNP","PLW","UMI","ASM","COK","PYF","NIU","PCN","WSM","TKL","TON","TUV","WLF");

    private List<String> region = Arrays.asList("africa", "americas", "antarctica", "asia", "europe", "oceania");
    private List<String> subregion = Arrays.asList("northern africa", "sub-saharan africa", "latin america and the caribbean",
            "northern america", "central asia", "eastern asia", "south-eastern asia", "southern asia", "western asia",
            "eastern europe", "northern europe", "southern europe", "western europe", "australia and new zealand",
            "melanesia", "micronesia", "polynesia");


    private List<String> intermediate_region = Arrays.asList("eastern africa", "middle africa", "southern africa",
            "western africa", "caribbean", "central america", "south america", "channel islands");

    private List<String> publication_type = Arrays.asList("paper","peerprint","direct submission to genbank","direct submission to mitoDB","article");
    private List<String> publication_status = Arrays.asList("published","protected","private","in press",
            "in preparation","submitted","unpublished");
    private List<String> sequencing_platform = Arrays.asList("illumina","454","sanger","nanopore","pacbio", "affymetrix");


    private String log_missing_sequences="";
    private String log_missing_columns="";
    private String log_mandatory_fields ="";
    private String log_incorrect_format = "";
    private String log_missing_value = "";
    private String string_accession_default = "";

    private String logileTxt = "";

    private int count_sequences=0;
    private int count_meta=0;
    private boolean uploadPossible;


    public boolean validate(String data_template, List<String> fastaheaders, String log_sequence_corretness, String mt_sequences_filepath) {
        BufferedReader br = null;
        String line;
        String delimiter = ",";
        HashMap<String, Integer> attribute_index_map = new HashMap<>();
        count_sequences=fastaheaders.size();

        logileTxt += "Data validation report based on files:\n" + mt_sequences_filepath + "\n" + data_template + "\n\n";


        // parse header line
        try {

            br = new BufferedReader(new FileReader(data_template));

            String[] headerLine_array = br.readLine().replace("##", "").split(delimiter);
            List<String> headerList = Arrays.asList(headerLine_array);

            // get indexes
            int index_doi = headerList.indexOf("doi");
            attribute_index_map.put("doi", index_doi);

            int index_author = headerList.indexOf("author");
            attribute_index_map.put("author", index_author);

            int index_publication_date = headerList.indexOf("publication_date");
            attribute_index_map.put("publication_date", index_publication_date);

            int index_title = headerList.indexOf("title");
            attribute_index_map.put("title", index_title);

            int index_journal = headerList.indexOf("journal");
            attribute_index_map.put("journal", index_journal);

            int index_publication_type = headerList.indexOf("publication_type");
            attribute_index_map.put("publication_type", index_publication_type);

            int index_publication_status = headerList.indexOf("publication_status");
            attribute_index_map.put("publication_status", index_publication_status);

            int index_publication_comments = headerList.indexOf("publication_comments");
            attribute_index_map.put("publication_comments", index_publication_comments);

            int index_accession_id = headerList.indexOf("accession_id");
            attribute_index_map.put("accession_id", index_accession_id);

            int index_tissue_sampled = headerList.indexOf("tissue_sampled");
            attribute_index_map.put("tissue_sampled", index_tissue_sampled);

            int index_sampling_date = headerList.indexOf("sampling_date");
            attribute_index_map.put("sampling_date", index_sampling_date);

            int index_sequencing_platform = headerList.indexOf("sequencing_platform");
            attribute_index_map.put("sequencing_platform", index_sequencing_platform);

            int index_enrichment_method = headerList.indexOf("enrichment_method");
            attribute_index_map.put("enrichment_method", index_enrichment_method);

            int index_extraction_protocol = headerList.indexOf("extraction_protocol");
            attribute_index_map.put("extraction_protocol", index_extraction_protocol);

            int index_mean_coverage = headerList.indexOf("mean_coverage");
            attribute_index_map.put("mean_coverage", index_mean_coverage);

            int index_std_dev_coverage = headerList.indexOf("std_dev_coverage");
            attribute_index_map.put("std_dev_coverage", index_std_dev_coverage);

            int index_minimum_coverage = headerList.indexOf("minimum_coverage");
            attribute_index_map.put("minimum_coverage", index_minimum_coverage);

            int index_maximum_coverage = headerList.indexOf("maximum_coverage");
            attribute_index_map.put("maximum_coverage", index_maximum_coverage);

            int index_calibrated_date_range_from = headerList.indexOf("calibrated_date_range_from");
            attribute_index_map.put("calibrated_date_range_from", index_calibrated_date_range_from);

            int index_calibrated_date_range_to = headerList.indexOf("calibrated_date_range_to");
            attribute_index_map.put("calibrated_date_range_to", index_calibrated_date_range_to);

            int index_C14_age_BP = headerList.indexOf("c14_age_bp");
            attribute_index_map.put("c14_age_bp", index_C14_age_BP);

            int index_indirect_contextual_date = headerList.indexOf("indirect_contextual_date");
            attribute_index_map.put("indirect_contextual_date", index_indirect_contextual_date);

            int index_indirect_contextual_date_from = headerList.indexOf("indirect_contextual_date_from");
            attribute_index_map.put("indirect_contextual_date_from", index_indirect_contextual_date_from);

            int index_indirect_contextual_date_to = headerList.indexOf("indirect_contextual_date_to");
            attribute_index_map.put("indirect_contextual_date_to", index_indirect_contextual_date_to);

            int index_radiocarbon_lab_code = headerList.indexOf("radiocarbon_lab_code");
            attribute_index_map.put("radiocarbon_lab_code", index_radiocarbon_lab_code);

            int index_dating_comments = headerList.indexOf("dating_comments");
            attribute_index_map.put("dating_comments", index_dating_comments);

            int index_reference_genome = headerList.indexOf("reference_genome");
            attribute_index_map.put("reference_genome", index_reference_genome);

            int index_starting_np = headerList.indexOf("starting_np");
            attribute_index_map.put("starting_np", index_starting_np);

            int index_ending_np = headerList.indexOf("ending_np");
            attribute_index_map.put("ending_np", index_ending_np);

            int index_sequence_versions = headerList.indexOf("sequence_versions");
            attribute_index_map.put("sequence_versions", index_sequence_versions);

            int index_comments_sequence_versions = headerList.indexOf("comments_sequence_version");
            attribute_index_map.put("comments_sequence_version", index_comments_sequence_versions);

            int index_haplogroup_originally_published = headerList.indexOf("haplogroup_originally_published");
            attribute_index_map.put("haplogroup_originally_published", index_haplogroup_originally_published);

            int index_data_type = headerList.indexOf("data_type");
            attribute_index_map.put("data_type", index_data_type);

            int index_labsample_id = headerList.indexOf("labsample_id");
            attribute_index_map.put("labsample_id", index_labsample_id);

            int index_sex = headerList.indexOf("sex");
            int index_age = headerList.indexOf("age");
            int index_population_purpose = headerList.indexOf("population_purpose");
            int index_access = headerList.indexOf("access");
            int index_population = headerList.indexOf("population");

            int index_geographic_info_tma_inferred_region = headerList.indexOf("geographic_info_tma_inferred_region");
            int index_geographic_info_tma_inferred_subregion = headerList.indexOf("geographic_info_tma_inferred_subregion");
            int index_geographic_info_tma_inferred_intermediate_region = headerList.indexOf("geographic_info_tma_inferred_intermediate_region");
            int index_geographic_info_tma_inferred_country = headerList.indexOf("geographic_info_tma_inferred_country");
            int index_geographic_info_tma_inferred_city = headerList.indexOf("geographic_info_tma_inferred_city");
            int index_geographic_info_tma_inferred_latitude = headerList.indexOf("geographic_info_tma_inferred_latitude");
            int index_geographic_info_tma_inferred_longitude = headerList.indexOf("geographic_info_tma_inferred_longitude");

            int index_sample_origin_region = headerList.indexOf("sampling_region");
            int index_sample_origin_subregion = headerList.indexOf("sampling_subregion");
            int index_sample_origin_intermediate_region = headerList.indexOf("sampling_intermediate_region");
            int index_sample_origin_country = headerList.indexOf("sampling_country");
            int index_sample_origin_city = headerList.indexOf("sampling_city");
            int index_sample_origin_latitude = headerList.indexOf("sampling_latitude");
            int index_sample_origin_longitude = headerList.indexOf("sampling_longitude");

            int index_sampling_region = headerList.indexOf("sample_origin_region");
            int index_sampling_subregion = headerList.indexOf("sample_origin_subregion");
            int index_sampling_intermediate_region = headerList.indexOf("sample_origin_intermediate_region");
            int index_sampling_country = headerList.indexOf("sample_origin_country");
            int index_sampling_city = headerList.indexOf("sample_origin_city");
            int index_sampling_latitude = headerList.indexOf("sample_origin_latitude");
            int index_sampling_longitude = headerList.indexOf("sample_origin_longitude");

            int index_marriage_rules = headerList.indexOf("marriage_rules");
            int index_marriage_system = headerList.indexOf("marriage_system");
            int index_descent_system = headerList.indexOf("descent_system");
            int index_residence_system = headerList.indexOf("residence_system");
            int index_subsistence = headerList.indexOf("subsistence");
            int index_clan = headerList.indexOf("clan");
            int index_ethnicity = headerList.indexOf("ethnicity");
            int index_language = headerList.indexOf("language");
            int index_generations_to_tma = headerList.indexOf("generations_to_tma");

            int index_user_firstname = headerList.indexOf("user_firstname");
            int index_user_surname = headerList.indexOf("user_surname");
            int index_user_email = headerList.indexOf("user_email");
            int index_user_affiliation = headerList.indexOf("user_affiliation");

            // stat missing columns:

            if(index_doi == -1 )
                log_missing_columns += "DOI.\n";
            if(index_author == -1)
                log_missing_columns +="Author.\n";
            if(index_publication_date == -1)
                log_missing_columns +="Publication date.\n";
            if(index_title == -1)
                log_missing_columns +="Title.\n";
            if(index_journal == -1)
                log_missing_columns +="Journal.\n";
            if(index_publication_type == -1)
                log_missing_columns +="Publication type.\n";
            if(index_publication_status == -1)
                log_missing_columns +="Publication status.\n";
            if(index_publication_comments == -1)
                log_missing_columns +="Publication comment.\n";
            if(index_accession_id == -1)
                log_missing_columns +="Accession id.\n";
            if(index_tissue_sampled == -1)
                log_missing_columns +="Tissue sampled.\n";
            if(index_sampling_date == -1)
                log_missing_columns +="Sampling date.\n";
            if(index_sequencing_platform == -1)
                log_missing_columns +="Sequencing platform.\n";
            if(index_enrichment_method == -1)
                log_missing_columns +="Enrichment method.\n";
            if(index_extraction_protocol == -1)
                log_missing_columns +="Extraction protocol.\n";
            if(index_mean_coverage == -1)
                log_missing_columns +="Mean coverage.\n";
            if(index_std_dev_coverage == -1)
                log_missing_columns +="Std dev coverage.\n";
            if(index_minimum_coverage == -1)
                log_missing_columns +="Minimum coverage.\n";
            if(index_maximum_coverage == -1)
                log_missing_columns +="Maximum coverage.\n";
            if(index_calibrated_date_range_from == -1)
                log_missing_columns +="Calibrated date lower limit.\n";
            if(index_calibrated_date_range_to == -1)
                log_missing_columns +="Calibrated date upper limit.\n";
            if(index_C14_age_BP == -1)
                log_missing_columns +="C14 are BP.\n";
            if(index_indirect_contextual_date == -1)
                log_missing_columns +="Indirect contextual date.\n";
            if(index_indirect_contextual_date_from == -1)
                log_missing_columns +="Indirect contextual date from.\n";
            if(index_indirect_contextual_date_to == -1)
                log_missing_columns +="Indirect contextual date to.\n";
            if(index_radiocarbon_lab_code == -1)
                log_missing_columns +="Radiocarbon lab code .\n";
            if(index_dating_comments == -1)
                log_missing_columns +="Dating comment.\n";
            if(index_reference_genome == -1)
                log_missing_columns +="Reference genome.\n";
            if(index_publication_type == -1)
                log_missing_columns +="Reference type.\n";
            if(index_starting_np == -1)
                log_missing_columns +="Starting np.\n";
            if(index_ending_np == -1)
                log_missing_columns +="Ending np.\n";
            if(index_sequence_versions == -1)
                log_missing_columns +="Sequence versions.\n";
            if(index_haplogroup_originally_published == -1)
                log_missing_columns +="Haplogroup originally published.\n";
            if(index_data_type == -1)
                log_missing_columns +="Data type.\n";
            if(index_labsample_id == -1)
                log_missing_columns +="Labsample id.\n";
            if(index_sex == -1)
                log_missing_columns +="Sex.\n";
            if(index_age == -1)
                log_missing_columns +="Age.\n";
            if(index_population_purpose == -1)
                log_missing_columns +="Population purpose.\n";
            if(index_access == -1)
                log_missing_columns +="Access.\n";
            if(index_population == -1)
                log_missing_columns +="Population.\n";
            if(index_geographic_info_tma_inferred_region == -1)
                log_missing_columns +="Geographic info tma inferred region.\n";
            if(index_geographic_info_tma_inferred_subregion == -1)
                log_missing_columns +="Geographic info tma inferred subregion.\n";
            if(index_geographic_info_tma_inferred_intermediate_region == -1)
                log_missing_columns +="Geographic info tma inferred intermediate region.\n";
            if(index_geographic_info_tma_inferred_country == -1)
                log_missing_columns +="Geographic info tma inferred country.\n";
            if(index_geographic_info_tma_inferred_city == -1)
                log_missing_columns +="Geographic info tma inferred city.\n";
            if(index_geographic_info_tma_inferred_latitude == -1)
                log_missing_columns +="Geographic info tma inferred latitude.\n";
            if(index_geographic_info_tma_inferred_longitude == -1)
                log_missing_columns +="Geographic info tma inferred longitude.\n";
            if(index_sample_origin_region == -1)
                log_missing_columns +="Sample origin region.\n";
            if(index_sample_origin_subregion == -1)
                log_missing_columns +="Sample origin subregion.\n";
            if(index_sample_origin_intermediate_region == -1)
                log_missing_columns +="Sample origin intermediate region.\n";
            if(index_sample_origin_country == -1)
                log_missing_columns +="Sample origin country.\n";
            if(index_sample_origin_city == -1)
                log_missing_columns +="Sample origin city.\n";
            if(index_sample_origin_latitude == -1)
                log_missing_columns +="Sample origin latitude.\n";
            if(index_sample_origin_longitude == -1)
                log_missing_columns +="Sample origin longitude.\n";
            if(index_sampling_region == -1)
                log_missing_columns +="Sampling region.\n";
            if(index_sampling_subregion == -1)
                log_missing_columns +="Sampling subregion.\n";
            if(index_sampling_intermediate_region == -1)
                log_missing_columns +="Sampling intermediate region.\n";
            if(index_sampling_country == -1)
                log_missing_columns +="Sampling country.\n";
            if(index_sampling_city == -1)
                log_missing_columns +="Sampling city.\n";
            if(index_sampling_latitude == -1)
                log_missing_columns +="Sampling latitude.\n";
            if(index_sampling_longitude == -1)
                log_missing_columns +="Sampling longitude.\n";
            if(index_marriage_rules == -1)
                log_missing_columns +="Marriage rules.\n";
            if(index_marriage_system == -1)
                log_missing_columns +="Marriage system.\n";
            if(index_descent_system == -1)
                log_missing_columns +="Descent system.\n";
            if(index_residence_system == -1)
                log_missing_columns +="Residence system.\n";
            if(index_subsistence == -1)
                log_missing_columns +="Subsistence.\n";
            if(index_clan == -1)
                log_missing_columns +="Clan.\n";
            if(index_ethnicity == -1)
                log_missing_columns +="Ethnicity.\n";
            if(index_language == -1)
                log_missing_columns +="Language.\n";
            if(index_generations_to_tma == -1)
                log_missing_columns +="Generations to tma.\n";
            if(index_user_firstname == -1)
                log_missing_columns +="User first name.\n";
            if(index_user_surname == -1)
                log_missing_columns +="User surname.\n";
            if(index_user_email == -1)
                log_missing_columns +="User email.\n";
            if(index_user_affiliation == -1)
                log_missing_columns +="User affiliation.\n";



            // iterate over all lines and check correct format and missing value

            while ((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.startsWith("##")){
                    count_meta++;


                    String[] line_splitted = line.split(",", headerLine_array.length);
                    // does sequence exists?
                    String accession = line_splitted[index_accession_id];

                    //log_mandatory_fields += "Entry to accession ID: " + accession + "\n";

                    // log_incorrect_format += "--------------------------------------------------\n" + accession + ":\n";
                    log_missing_value += "--------------------------------------------------\n" + accession + ":\n";
                    log_mandatory_fields += "--------------------------------------------------\n" + accession + ":\n";
                    string_accession_default += "--------------------------------------------------\n" + accession + ":\n";


                    if(!fastaheaders.contains(accession.split("\\.")[0])){
                        log_missing_sequences += "Sequence with this accession ID: "+ accession +" does not exist in fasta file.\n";
                        uploadPossible=false;
                    }

                    // the mandatory fields has to be present:
                    if(line_splitted[index_accession_id] == null || line_splitted[index_accession_id].equals("")){
                        log_mandatory_fields += "\tAccession ID must be set! (mandatory field)\n";
                    }
                    if(line_splitted[index_publication_status] == null || line_splitted[index_publication_status].equals("")){
                        log_mandatory_fields += "\tPublication status must be set! (mandatory field)\n";
                    }
                    if(line_splitted[index_author] == null || line_splitted[index_author].equals("")){
                        log_mandatory_fields += "\tAuthor must be set! (mandatory field)\n";
                    }
                    if(line_splitted[index_data_type] == null || line_splitted[index_data_type].equals("")){
                        log_mandatory_fields += "\tData type must be set! (mandatory field)\n";
                    }
                    if(line_splitted[index_user_email] == null || line_splitted[index_user_email].equals("")){
                        log_mandatory_fields += "\tUser Email must be set! (mandatory field)\n";
                    }

                    // test presence and correctness-----------------------------------------------------------------------------------------------


                    if(index_doi != -1){
                        String doi = line_splitted[index_doi].toLowerCase().trim();
                        if(doi.equals("")){
                            log_missing_value += "\tDoi is missing.\n";
                        } else if(!doi.contains(".")){
                            log_incorrect_format += "Accession: "+ accession + "\tDoi is not in correct format: " + doi + "\n";
                        }
                    }

                    if(index_reference_genome != -1){
                        String reference_genome = line_splitted[index_reference_genome].toLowerCase().trim();
                        if(reference_genome.equals("")){
                            log_missing_value += "\tReference genome is missing.\n";
                        } else if(!reference_genome.equals("rsrs") && !reference_genome.equals("rcrs") ){
                            log_incorrect_format += "Accession: "+ accession + "\tReference genome  is not in correct format: " + reference_genome + "\n";
                        }
                    }

                    if(index_mean_coverage != -1){
                        String number = line_splitted[index_mean_coverage];
                        if(number.equals("")){
                            log_missing_value += "\tMean coverage is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tMean coverage is not in correct format: " + number + "\n";
                            }
                        }
                    }

                    if(index_std_dev_coverage != -1){
                        String number = line_splitted[index_std_dev_coverage];
                        if(number.equals("")){
                            log_missing_value += "\tCoverage standard deviation is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tCoverage standard deviation is not in correct format: " + number + "\n";
                            }
                        }
                    }

                    if(index_minimum_coverage != -1){
                        String number = line_splitted[index_minimum_coverage];
                        if(number.equals("")){
                            log_missing_value += "\tMinimum coverage is missing\n";
                        } else if(!isStringInt(number)){
                            log_incorrect_format += "Accession: "+ accession + "\tMinimum coverage is not in correct format: " + number + "\n";
                        }
                    }

                    if(index_maximum_coverage != -1){
                        String number = line_splitted[index_maximum_coverage];
                        if(number.equals("")){
                            log_missing_value += "\tmaximum coverage is missing\n";
                        } else if(!isStringInt(number)){
                            log_incorrect_format += "Accession: "+ accession + "\tmaximum coverage is not in correct format: " + number + "\n";
                        }
                    }

                    if(index_starting_np != -1){
                        String number = line_splitted[index_starting_np];
                        if(number.equals("")){
                            log_missing_value += "\tStarting position is missing\n";
                        } else if(!isStringInt(number)){
                            log_incorrect_format += "Accession: "+ accession + "\tStarting position is not in correct format: " + number + "\n";
                        }
                    }

                    if(index_ending_np != -1){
                        String number = line_splitted[index_ending_np];
                        if(number.equals("")){
                            log_missing_value += "\tEnding position is missing\n";
                        } else if(!isStringInt(number)){
                            log_incorrect_format += "Accession: "+ accession + "\tEnding position is not in correct format: " + number + "\n";
                        }
                    }

                    if(index_data_type != -1){
                        String data_type = line_splitted[index_data_type].toLowerCase().trim();
                        if(!data_type.equals("fullmt") && !data_type.equals("wholegenome")){
                            log_incorrect_format += "Accession: "+ accession + "\tData type is not in correct format: " + data_type + "\n";
                        }
                    }

                    if(index_age != -1){
                        String age = line_splitted[index_age].toLowerCase().trim();
                        if (age.equals("")){
                            log_missing_value += "\tAge is missing.\n";
                        } else if(!isStringInt(age) && !age.equals("adult") && !age.equals("neonate") && !age.equals("subadult")
                                && !age.equals("infant") && !age.contains("-") && !age.contains("+")){
                            log_incorrect_format += "Accession: "+ accession + "\tAge is not in correct format: " + age + "\n";
                        }
                    }


                    if(index_sex != -1){
                        String sex = line_splitted[index_sex].toLowerCase().trim();
                        if (sex.equals("")){
                            log_missing_value += "\tSex is missing.\n";
                        } else if(!sex.equals("f") && !sex.equals("m") && !sex.equals("u")){
                            log_incorrect_format += "Accession: "+ accession + "\tSex is not in correct format: " + sex + "\n";
                        }
                    }

                    if(index_population_purpose != -1){
                        String population_purpose = line_splitted[index_population_purpose].toLowerCase().trim();
                        if (population_purpose.equals("")){
                            log_missing_value += "\tPopulation purpose is missing.\n";
                        } else if(!population_purpose.equals("yes") && !population_purpose.equals("no")){
                            log_incorrect_format += "Accession: "+ accession + "\tPopulation purpose is not in correct format: " + population_purpose + "\n";
                        }
                    }

                    if(index_access != -1){
                        String access = line_splitted[index_access].toLowerCase().trim();
                        if (access.equals("")){
                            log_missing_value += "\tAccess is missing.\n";
                        } else if(!access.equals("public")){
                            log_incorrect_format += "Accession: "+ accession + "\tAccess is not in correct format: " + access + "\n";
                        }
                    }

                    // --------------------------------------------------------------------------------------------------------------------
                    // GEOGRAPHIC INFO - tma INFERRED

                    if(index_geographic_info_tma_inferred_region != -1){
                        String geographic_info_tma_inferred_region = line_splitted[index_geographic_info_tma_inferred_region].toLowerCase().trim();
                        if (geographic_info_tma_inferred_region.equals("")) {
                            log_missing_value += "\tGeographic info tma inferred region is missing.\n";
                        } else if(!region.contains(geographic_info_tma_inferred_region) && !isStringInt(geographic_info_tma_inferred_region)){
                            log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred region is not in correct format: " + geographic_info_tma_inferred_region + "\n";
                        }
                    }

                    if(index_geographic_info_tma_inferred_subregion != -1){
                        String geographic_info_tma_inferred_subregion = line_splitted[index_geographic_info_tma_inferred_subregion].toLowerCase().trim();
                        if (geographic_info_tma_inferred_subregion.equals("")) {
                            log_missing_value += "\tGeographic info tma inferred subregion is missing.\n";
                        } else if(!subregion.contains(geographic_info_tma_inferred_subregion)&& !isStringInt(geographic_info_tma_inferred_subregion)){
                            log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred subregion is not in correct format: " + geographic_info_tma_inferred_subregion + "\n";
                        }
                    }

                    if(index_geographic_info_tma_inferred_intermediate_region != -1){
                        String geographic_info_tma_inferred_intermediate_region = line_splitted[index_geographic_info_tma_inferred_intermediate_region].toLowerCase().trim();
                        if (geographic_info_tma_inferred_intermediate_region.equals("")) {
                            log_missing_value += "\tGeographic info tma inferred intermediate region is missing.\n";
                        } else if(!intermediate_region.contains(geographic_info_tma_inferred_intermediate_region) && !isStringInt(geographic_info_tma_inferred_intermediate_region)){
                            log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred intermediate region is not in correct format: " + geographic_info_tma_inferred_intermediate_region + "\n";
                        }
                    }


                    if(index_geographic_info_tma_inferred_country != -1){
                        String geographic_info_tma_inferred_country = line_splitted[index_geographic_info_tma_inferred_country];
                        geographic_info_tma_inferred_country = geographic_info_tma_inferred_country.trim();
                        if (geographic_info_tma_inferred_country.equals("")) {
                            log_missing_value += "\tGeographic info tma inferred country is missing.\n";
                        } else if(!country.contains(geographic_info_tma_inferred_country) && !isStringInt(geographic_info_tma_inferred_country)){
                            log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred country is not in correct format: " + geographic_info_tma_inferred_country + "\n";
                        }
                    }

                    if(index_geographic_info_tma_inferred_latitude != -1){
                        String number = line_splitted[index_geographic_info_tma_inferred_latitude];
                        if(number.equals("")){
                            log_missing_value += "\tGeographic info tma inferred latitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred latitude is not in correct format: " + number + "\n";
                            }
                        }
                    }

                    if(index_geographic_info_tma_inferred_longitude != -1){
                        String number = line_splitted[index_geographic_info_tma_inferred_longitude];
                        if(number.equals("")){
                            log_missing_value += "\tGeographic info tma inferred longitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tGeographic info tma inferred longitude is not in correct format: " + number + "\n";
                            }
                        }
                    }


                    // --------------------------------------------------------------------------------------------------------------------
                    // GEOGRAPHIC INFO - SAMPLE


                    if(index_sample_origin_region != -1){
                        String sample_origin_region = line_splitted[index_sample_origin_region].toLowerCase().trim();
                        if (sample_origin_region.equals("")) {
                            log_missing_value += "\tSample origin region is missing.\n";
                        } else if(!region.contains(sample_origin_region) && !isStringInt(sample_origin_region)){
                            log_incorrect_format += "Accession: "+ accession +"\tSample origin region is not in correct format: " + sample_origin_region + "\n";
                        }
                    }


                    if(index_sample_origin_subregion != -1){
                        String sample_origin_subregion = line_splitted[index_sample_origin_subregion].toLowerCase().trim();
                        if (sample_origin_subregion.equals("")) {
                            log_missing_value += "\tSample origin subregion is missing.\n";
                        } else if(!subregion.contains(sample_origin_subregion) && !isStringInt(sample_origin_subregion)){
                            log_incorrect_format += "Accession: "+ accession + "\tSample origin subregion is not in correct format: "+ sample_origin_subregion + "\n";
                        }
                    }

                    if(index_sample_origin_intermediate_region != -1){
                        String sample_origin_intermediate_region = line_splitted[index_sample_origin_intermediate_region].toLowerCase().trim();
                        if (sample_origin_intermediate_region.equals("")) {
                            log_missing_value += "\tSample origin intermediate region is missing.\n";
                        } else if(!intermediate_region.contains(sample_origin_intermediate_region) && !isStringInt(sample_origin_intermediate_region)){
                            log_incorrect_format += "Accession: "+ accession + "\tSample origin intermediate region is not in correct format: " + sample_origin_intermediate_region + "\n";
                        }
                    }

                    if(index_sample_origin_country != -1){
                        String sample_origin_country = line_splitted[index_sample_origin_country];
                        if (sample_origin_country.equals("")) {
                            log_missing_value += "\tSample origin country is missing.\n";
                        } else if(!country.contains(sample_origin_country) && !isStringInt(sample_origin_country)){
                            log_incorrect_format += "Accession: "+ accession + "\tSample origin country is not in correct format: " + sample_origin_country + "\n";
                        }
                    }


                    if(index_sample_origin_latitude != -1){
                        String number = line_splitted[index_sample_origin_latitude];
                        if(number.equals("")){
                            log_missing_value += "\tSample origin latitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tSample origin latitude is not in correct format: " + number + "\n";
                            }
                        }
                    }

                    if(index_sample_origin_longitude != -1){
                        String number = line_splitted[index_sample_origin_longitude];
                        if(number.equals("")){
                            log_missing_value += "\tSample origin longitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tSample origin longitude is not in correct format: " + number + "\n";
                            }
                        }
                    }


                    // --------------------------------------------------------------------------------------------------------------------
                    // GEOGRAPHIC INFO - SAMPLING

                    if(index_sampling_region != -1){
                        String sampling_region = line_splitted[index_sampling_region].toLowerCase().trim();
                        if (sampling_region.equals("")) {
                            log_missing_value += "\tSampling region is missing.\n";
                        } else if(!region.contains(sampling_region) && !isStringInt(sampling_region)){
                            log_incorrect_format += "Accession: "+ accession + "\tSampling region is not in correct format: "+ sampling_region + "\n";
                        }
                    }

                    if(index_sampling_subregion != -1){
                        String sampling_subregion = line_splitted[index_sampling_subregion].toLowerCase().trim();
                        if (sampling_subregion.equals("")) {
                            log_missing_value += "\tSampling subregion is missing.\n";
                        } else if(!subregion.contains(sampling_subregion) && !isStringInt(sampling_subregion)){
                            log_incorrect_format += "Accession: "+ accession + "\tSampling subregion is not in correct format: "+ sampling_subregion + "\n";
                        }
                    }


                    if(index_sampling_intermediate_region != -1){
                        String sampling_intermediate_region = line_splitted[index_sampling_intermediate_region].toLowerCase().trim();
                        if (sampling_intermediate_region.equals("")) {
                            log_missing_value += "\tSampling intermediate region is missing.\n";
                        } else if(!intermediate_region.contains(sampling_intermediate_region) && !isStringInt(sampling_intermediate_region)){
                            log_incorrect_format += "Accession: "+ accession + "\tSampling intermediate region is not in correct format: "+ sampling_intermediate_region + "\n";
                        }
                    }

                    if(index_sampling_country != -1){
                        String sampling_contry = line_splitted[index_sampling_country];
                        if (sampling_contry.equals("")) {
                            log_missing_value += "\tSampling country is missing.\n";
                        } else if(!country.contains(sampling_contry) && !isStringInt(sampling_contry)){
                            log_incorrect_format += "Accession: "+ accession + "\tSampling country is not in correct format: "+ sampling_contry + "\n";
                        }
                    }

                    if(index_sampling_latitude != -1){
                        String number = line_splitted[index_sampling_latitude];
                        if(number.equals("")){
                            log_missing_value += "\tSampling latitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tSampling latitude is not in correct format: " + number + "\n";
                            }
                        }
                    }

                    if(index_sampling_longitude != -1){
                        String number = line_splitted[index_sampling_longitude];
                        if(number.equals("")){
                            log_missing_value += "\tSampling longitude is missing.\n";
                        } else {
                            try {
                                Double.parseDouble(number);
                            } catch(NumberFormatException e) {
                                log_incorrect_format += "Accession: "+ accession + "\tSampling longitude is not in correct format: " + number + "\n";
                            }
                        }
                    }


                    // --------------------------------------------------------------------------------------------------------------------
                    if(index_generations_to_tma != -1){
                        String number = line_splitted[index_generations_to_tma];
                        if(number.equals("")){
                            log_missing_value += "\tGenerations to tma is missing\n";
                        } else if(!isStringInt(number)){
                            log_incorrect_format += "Accession: "+ accession + "\tGenerations to tma is not in correct format: " + number + "\n";
                        }
                    }


                    if(index_publication_status != -1){
                        String publication_status = line_splitted[index_publication_status].toLowerCase().trim();
                        if(publication_status.equals("")){
                            log_missing_value += "\tPublication status is missing.\n";
                        } else if(!publication_status.equals("published") && !publication_status.equals("protected")
                                && !publication_status.equals("private") && !publication_status.equals("in press")
                                && !publication_status.equals("in preparation") && !publication_status.equals("submitted") &&
                                !publication_status.equals("unpublished")){
                            log_incorrect_format += "Accession: "+ accession + "\tPublication status is not in correct format: " + publication_status + "\n";
                        }
                    }

                    if(index_publication_date != -1){
                        if(line_splitted[index_publication_date].equals("")){
                            log_missing_value += "\tPublication date is missing.\n";
                        } else if(!isDateValid(line_splitted[index_publication_date])){
                            log_incorrect_format += "Accession: "+ accession + "\tPublication date is not in correct format: " + line_splitted[index_publication_date] + "\n";
                        }
                    }

                    if(index_sampling_date != -1){

                        if(line_splitted[index_sampling_date].equals("")){
                            log_missing_value += "\tSampling date is missing.\n";
                        } else if(!isDateValid(line_splitted[index_sampling_date])){
                            log_incorrect_format += "Accession: "+ accession + "\tSampling date is not in correct format: " + line_splitted[index_sampling_date] + "\n";
                        }
                    }

                    if(index_reference_genome != -1){
                        String reference_genome = line_splitted[index_reference_genome].toLowerCase().trim();
                        if(reference_genome.equals("")){
                            log_missing_value += "\tReference genome is missing.\n";
                        } else if(!reference_genome.equals("rsrs") && !reference_genome.equals("rcrs")){
                            log_incorrect_format += "Accession: "+ accession + "\tReference genome is not in correct format: " + reference_genome + "\n";
                        }
                    }

                    if(index_user_email != -1){
                        String user_email = line_splitted[index_user_email].toLowerCase().trim();
                        if(!user_email.contains("@") && !user_email.contains(".")){
                            log_incorrect_format += "Accession: "+ accession + "\tUser email is not in correct format: " + user_email + "\n";
                        }
                    }
                    if(index_publication_type != -1){
                        String publicationType = line_splitted[index_publication_type].toLowerCase().trim();
                        if(publicationType.equals("")){
                            log_missing_value += "\tPublication type is missing.\n";
                        } else if(!publication_type.contains(publicationType.toLowerCase().trim())){
                            log_incorrect_format += "Accession: "+ accession + "\tPublication type is not in correct format: " + publicationType + "\n";
                        }
                    }

                    if(index_publication_status != -1){
                        String publicationStatus = line_splitted[index_publication_status].toLowerCase().trim();
                        if(publicationStatus.equals("")){
                            log_missing_value += "\tPublication status is missing.\n";
                        } else if(!publication_status.contains(publicationStatus.toLowerCase().trim())){
                            log_incorrect_format += "Accession: "+ accession + "\tPublication status is not in correct format: " + publicationStatus + "\n";
                        }
                    }


                    if(index_sequencing_platform != -1){
                        String publicationPlatform = line_splitted[index_sequencing_platform].toLowerCase().trim();
                        if(publicationPlatform.equals("")){
                            log_missing_value += "\tSequencing platform is missing.\n";
                        } else if(!sequencing_platform.contains(publicationPlatform)){
                            log_incorrect_format += "Accession: "+ accession + "\tSequencing platform is not in correct format: " + publicationPlatform + "\n";
                        }
                    }

                    if(index_calibrated_date_range_from != -1){
                        String calibrated_date_range_from = line_splitted[index_calibrated_date_range_from].toLowerCase().trim();
                        if(calibrated_date_range_from.equals("")){
                            log_missing_value += "\tCalibrated date lower limit is missing.\n";
                        } else if(!isStringInt(calibrated_date_range_from)){
                            log_incorrect_format += "Accession: "+ accession + "\tCalibrated date lower limit is not in correct format: " + calibrated_date_range_from + "\n";
                        }
                    }


                    if(index_calibrated_date_range_to != -1){
                        String calibrated_date_range_to = line_splitted[index_calibrated_date_range_to].toLowerCase().trim();
                        if(calibrated_date_range_to.equals("")){
                            log_missing_value += "\tCalibrated date upper limit is missing.\n";
                        } else if(!isStringInt(calibrated_date_range_to)){
                            log_incorrect_format += "Accession: "+ accession + "\tCalibrated date upper limit is not in correct format: " + calibrated_date_range_to + "\n";
                        }
                    }

                    if(index_C14_age_BP != -1){
                        String c14_age_bp = line_splitted[index_C14_age_BP].toLowerCase().trim();
                        if(c14_age_bp.equals("")){
                            log_missing_value += "\tC14 date BP is missing.\n";
                        } else if(!c14_age_bp.contains("+-")){
                            log_incorrect_format += "Accession: "+ accession + "\tC14 date BP is not in correct format: " + c14_age_bp + "\n";
                        }
                    }

                }
            }


            //logfile.write("Missing attributes (columns) in the meta information file: \n");

            if(log_incorrect_format.equals("") && log_sequence_corretness.equals("")){
                logileTxt +="\nAll values are correct. Upload possible.\n\n";
                uploadPossible = true;

            } else if(!log_incorrect_format.equals("") && log_sequence_corretness.equals("")){
                logileTxt +="Upload not possible. Please check your data! \n\n Incorrect values:\n" + log_incorrect_format;
                uploadPossible = false;

            } else if(log_incorrect_format.equals("") && !log_sequence_corretness.equals("")){
                logileTxt +="Upload not possible. Please check your data! \n\n Incorrect sequences:\n" + log_sequence_corretness;
                uploadPossible = false;

            } else if(count_meta != count_sequences) {

                logileTxt +="Upload not possible. Number of sequences and meta data does not match!";
                uploadPossible = false;

            } else {
                logileTxt +="Upload not possible. Please check your data! \n\n Incorrect values:\n" + log_incorrect_format;
                logileTxt +="Incorrect sequences:\n" + log_sequence_corretness;
                uploadPossible = false;
            }

            logileTxt +="\n=======================================================\n";


            if(log_missing_columns.equals("")){
                logileTxt +="No columns missing\n";
            } else {
                logileTxt +="Missing columns:\n" + log_missing_columns;
            }

            logileTxt +="\n=========================================================\n";

            if(log_mandatory_fields.equals(string_accession_default)){
                logileTxt +="All mandatory fields set\n";
            } else {
                logileTxt +="Missing mandatory entry:\n" + log_mandatory_fields;
            }

            logileTxt +="\n=========================================================\n";

            if(log_missing_sequences.equals("")&& count_meta == count_sequences){
                logileTxt +="\nNo fasta sequences missing" + "\n# Sequences: "
                        + count_sequences + "\n# Meta info: " + count_meta + "\n";
            } else if (count_meta > count_sequences){
                logileTxt +="\nError!! Number of sequences and meta data does not match!\nNumber of sequences in fasta file: " + count_sequences;
                logileTxt +="\nNumber of meta information in csv file: " + count_meta + "\n";
                uploadPossible = false;
            } else if(count_meta < count_sequences){
                logileTxt +="\nWarning!! Number of sequences and meta data does not match!\nNumber of sequences in fasta file: " + count_sequences;
                logileTxt +="\nNumber of meta information in csv file: " + count_meta + "\n";
                uploadPossible = true;
            } else {
                logileTxt +="Missing fasta sequences:\n" + log_missing_sequences + "\nNumber of sequences in fasta file: " + count_sequences + "\nNumber of meta information in csv file: " + count_meta + "\n";
            }
            logileTxt +="\n=========================================================\n";



            if(log_missing_value.equals(string_accession_default)){
                logileTxt +="No missing values\n";
            } else {
                logileTxt +="Missing (but not mandatory) values:\n" + log_missing_value;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // this will only be reached if file cannot be read
        return false;
    }

    private boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    private boolean isDateValid(String date) {

        if(!isStringInt(date)){
            return false;
        } else if(Integer.parseInt(date) >  Calendar.getInstance().get(Calendar.YEAR)){
            return false;
        } else {
            return true;
        }
    }

    public boolean isUploadPossible() {
        return uploadPossible;
    }

    public void resetLogs() {
        log_missing_sequences = "";
        log_missing_columns = "";
        log_mandatory_fields = "";
        log_incorrect_format = "";
        log_missing_value = "";
        string_accession_default = "";
        logileTxt = "";

        count_sequences = 0;
        count_meta = 0;
        uploadPossible = false;
    }


    public void writeLogFile(String out, String fileNameWithoutExt) throws IOException {

        BufferedWriter logfile;
        // init writer
        if(uploadPossible){
            logfile = new BufferedWriter(new FileWriter(out + "logfiles" + File.separator +
                    "PASSED_" + fileNameWithoutExt + "_logfile.txt"));

        } else {
            logfile = new BufferedWriter(new FileWriter(out + "logfiles" + File.separator +
                    "FAILED_" + fileNameWithoutExt + "_logfile.txt"));

        }

        logfile.write(logileTxt);
        logfile.close();

    }

    public List<String> getCountry() {
        return country;
    }

    public List<String> getRegion() {
        return region;
    }

    public List<String> getSubregion() {
        return subregion;
    }

    public List<String> getIntermediate_region() {
        return intermediate_region;
    }

    public List<String> getPublication_type() {
        return publication_type;
    }

    public List<String> getPublication_status() {
        return publication_status;
    }

    public List<String> getSequencing_platform() {
        return sequencing_platform;
    }

    public String getLog_missing_sequences() {
        return log_missing_sequences;
    }

    public String getLog_missing_columns() {
        return log_missing_columns;
    }

    public String getLog_mandatory_fields() {
        return log_mandatory_fields;
    }

    public String getLog_incorrect_format() {
        return log_incorrect_format;
    }

    public String getLog_missing_value() {
        return log_missing_value;
    }

    public String getString_accession_default() {
        return string_accession_default;
    }

    public String getLogfileTxt() {
        return logileTxt;
    }

    public int getCount_sequences() {
        return count_sequences;
    }

    public int getCount_meta() {
        return count_meta;
    }
}
