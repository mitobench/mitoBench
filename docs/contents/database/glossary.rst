.. _glossary-label:

Glossary
--------- 


This is the detailed description if databse glossary. A more detailed version can be found here:
https://docs.google.com/spreadsheets/d/18BsU3wdWvpE5emqy7TUBUO5Si-m-X368D1b-E4s_n5g/edit?usp=sharing

*******
Sample
*******

**haplotype_current_versions**
Haplotype information is automatically generated while importing the data to mitoDB and
is based on the current version of haplogrep2 and phylotree. It also will be updated if new versions of the
tools are released.

**haplogroup_originally_published**
Haplogroup information given in the original publication. This information has to be set by the user.

**haplogroup_current_versions**
Haplogroup information is automatically generated while importing the data to mitoDB and
is based on the current version of haplogrep2 and phylotree. This will be updated if new versions of the
tools are released.

**macro_haplogroup**
Nomenclature according to the 25 categories PhyloTree defined (www.phylotree.org). Set automatically.

**data_type**
Full genome / MT genome (just mt so far)

**accession_ID**
ID from other database / service, e.g. NCBI / Genbank / GeneOmnibus / ...

**labSample_ID**
ID of the sample, given in the lab / by the sample owner. This information can be used to avoid duplicated

**sex**
The sex of the individual.  (M = male, F = female, U = undefined)

**age**
Age at death (ancient sample) / age at time of sampling (for living individuals)

**population_purpose**
Was this sample used for population analysis?

**access**
What is the publication status of the sample? So far, we will just allow public data.

******************************
Ethnical info of sample
******************************

**language**
Information about the language (glottocode http://glottolog.org/glottolog?iso=chc#12/34.9685/279.1143)

**generations_to_TMA**
Number of generations to TMA (Terminal Maternal Ancestor)

**geographic_info_TMA_inferred_latitude**
Geographic location (latitude) inferred from TMA.

**geographic_info_TMA_inferred_longitude**
Geographic location (longitude) inferred from TMA.

**geographic_info_TMA_inferred_geographic_area_m49**
More detailed information about geographic location of TMA.

**geographic_info_TMA_inferred_subregion_m49**
More detailed information about geographic location of TMA.

**geographic_info_TMA_inferred_country_m49**
More detailed information about geographic location of TMA.

**geographic_info_TMA_inferred_city**
More detailed information about geographic location of TMA.

**marriage_rules**
Marriage rule practiced in this ethnicity.

**marriage_system**
Type of marriage (https://en.wikipedia.org/wiki/Types_of_marriages#Types_of_marriages)

**descent_system**
Descent system.

**residence_system**
Social norms that define, where the couple lives after marriage

**subsistence**
The action or fact of maintaining or supporting oneself, especially at a minimal level.

**clan**
Clan to which the individual corresponds.

**ehnicity**
Ethnic group to which the samples individual corresponds.

********************
Population
********************

**population**
"Describe the sample with a meaningful identification. We are aware of the difficulty of defining a population, but we
want to try to describe the samples as detailed as possible. Please use either ethnolinguistic or geographic criteria.
Use broad descriptors for medical cohorts. Refer to the terms used in the original publication if possible.
Consider avoiding denigratory terms or culturally sensitive terms. Please use english descriptors. "

********************
Publication
********************

**doi**
digital object identifier of the publication

**author**
First author of the paper, following the PubMed citation style. Surname followed by first capital letter of given name(s), separated by space. Several given names are not separated. Full list of authors can be accessed via doi or title/date of the publication.

**publication_date**
publication year / date of submission.

**title**
Title of the publication.

**journal**
In which journal was the data published?

**reference_type**
What kind of publication is it?

**publication_status**
published or not?

**publication_comments**
can link information to a newer version.

********************
Technical Info
********************
**mt_sequence**
Sequence itself, given as separate fasta file, or included in the excel upload file.
The sequence has to follow the GenBank submission guidelines.

**percentage of N's**
Percentage of the genome reconstructed

**completeness**
How complete is the sequence? We set a threshold for modern data to 99%, for ancient data to 98%

**tissue_sampled**
Which tissue was sampled?

**sampling_date**
Year when the sample was sampled.

**sequencing_platform**
Sequencing platform that was ues to sequence the samples.

**enrichment_method**
Enrichment method that was used.

**extraction_protocol**
Extraction protocol that was used.

**minimum_coverage**
Minimal depth of coverage of the reference genome

**maximum_coverage**
Maximal depth of coverage of the reference genome

**mean_coverage**
Mean depth of coverage (fold) of the reference genome.
(QualiMap can be used to get this information out of the bam file.)

**std_dev_coverage**
Standard deviation of depth of coverage.
(QualiMap can be used to get this information out of the bam file.)

**calibrated_date**
Calibrated date, defined by archaeologist i.e., specified as year / range of years based on
before christ (cal BC) and/or anno domini (cal AD)

**radio_carbon_date**
Uncalibrated radiocarbon date. This date is given as year before present (BP, present is defined as 1950)
with a certain confidence interval (indicated with +/-).

**reference_genome**
Reference genome used to calculate Haplotypes.

**starting_np**
Start position of the mtDNA (interesting for incomplete data)
--> linked to the used reference genome

**ending_np**
End position of the mtDNA (interesting for incomplete data)
--> linked to the used reference genome


****************************************
Geographical info of sampling place
****************************************

**sampling_latitude**
Latitude of sampling location

**sampling_longitude**
Longitude of sampling location

**sampling_geographic_area_m49**
Geographic area where the sample was taken (m49)

**sampling_subregion_m49**
Subregion where the sample was taken (m49)

**sampling_country_m49**
Country where the sample was taken (m49)

**sampling_city**
Specified as airport code (http://airportsbase.org/)

**sampling_comments**
Is the location the actual sampling location, just the capital of the country or the country?


****************************************
Geographical info of sample origin
****************************************

**sample_origin_latitude**
Latitude of sample origin.

**sample_origin_longitude**
Longitude of sample origin

**sample_origin_geographic_area_m49**
Geographic area where the sample originate from (m49)

**sample_origin_subregion_m49**
Subregion where the sample originate from (m49)

**sample_origin_country_m49**
Country where the sample originate from (m49)

**sample_origin_city**
Specified as airport code (http://airportsbase.org/)


**********
User Info
**********

**user_alias**
Alias given by the system (Set automatically)

**user_first_name**
First/given name of user

**user_surname**
Surname of user

**user_email**
Email adress of user

**user_affiliation**
Affiliation of user
