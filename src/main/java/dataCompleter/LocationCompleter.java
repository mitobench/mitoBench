package dataCompleter;

import uk.recurse.geocoding.reverse.ReverseGeocoder;

import java.util.*;

public class LocationCompleter {


    private ReverseGeocoder geocoder;
    private List<String> header;
    private String[] entry;
    private int index_sample_origin_latitude;
    private int index_sample_origin_longitude;
    private int index_sample_origin_region;
    private int index_sample_origin_subregion;
    private int index_sample_origin_intermediate_region;
    private int index_sample_origin_country;

    private int index_sampling_latitude;
    private int index_sampling_longitude;
    private int index_sampling_region;
    private int index_sampling_subregion;
    private int index_sampling_intermediate_region;
    private int index_sampling_country;

    private int index_TMA_inferrred_latitude;
    private int index_TMA_inferrred_longitude;
    private int index_TMA_inferrred_region;
    private int index_TMA_inferrred_subregion;
    private int index_TMA_inferrred_intermediate_region;
    private int index_TMA_inferrred_country;

    private String[][] m49_list;
    // private String[][] m49_list2;


    private Map<String, Locale> localeMap;



    public LocationCompleter(){

        initCountryCodeMapping();
        this.geocoder = new ReverseGeocoder();
        fillListISO3();
        // fillListName();
    }


    private void initCountryCodeMapping() {
        String[] countries = Locale.getISOCountries();
        localeMap = new HashMap<>(countries.length);
        for (String country : countries) {
            Locale locale = new Locale("", country);
            localeMap.put(locale.getISO3Country().toUpperCase(), locale);
        }
    }

    private void fillListISO3() {
        // array: sub region, intermediate region, continent/region

        m49_list = new String[][] {new String[]{"DZA,EGY,LBY,MAR,SDN,TUN,ESH","Northern Africa","", "Africa"},
                new String[]{"IOT," +
                        "BDI," +
                        "COM," +
                        "DJI," +
                        "ERI," +
                        "ETH," +
                        "ATF," +
                        "KEN," +
                        "MDG," +
                        "MWI," +
                        "MUS," +
                        "MYT," +
                        "MOZ," +
                        "REU," +
                        "RWA," +
                        "SYC," +
                        "SOM," +
                        "SSD," +
                        "UGA," +
                        "TZA," +
                        "ZMB," +
                        "ZWE", "Sub-Saharan Africa", "Eastern Africa", "Africa"},
                new String[]{"AGO," +
                        "CMR," +
                        "CAF," +
                        "TCD," +
                        "COG," +
                        "COD," +
                        "GNQ," +
                        "GAB" +
                        "STP", "Sub-Saharan Africa", "Middle Africa", "Africa"},
                new String[]{"BWA," +
                        "SWZ," +
                        "LSO," +
                        "NAM," +
                        "ZAF", "Sub-Saharan Africa", "Southern Africa", "Africa"},
                new String[]{"BEN," +
                        "BFA," +
                        "CPV," +
                        "CIV," +
                        "GMB," +
                        "GHA," +
                        "GIN," +
                        "GNB," +
                        "LBR," +
                        "MLI," +
                        "MRT," +
                        "NER," +
                        "NGA," +
                        "SHN," +
                        "SEN," +
                        "SLE," +
                        "TGO", "Sub-Saharan Africa", "Western Africa", "Africa"},
                new String[]{"AIA," +
                        "ATG," +
                        "ABW," +
                        "BHS," +
                        "BRB," +
                        "BES," +
                        "VGB," +
                        "CYM," +
                        "CUB," +
                        "CUW," +
                        "DMA," +
                        "DOM," +
                        "GRD," +
                        "GLP," +
                        "HTI," +
                        "JAM," +
                        "MTQ," +
                        "MSR," +
                        "PRI," +
                        "BLM," +
                        "KNA," +
                        "LCA," +
                        "MAF," +
                        "VCT," +
                        "SXM," +
                        "TTO," +
                        "TCA," +
                        "VIR","Latin America and the Caribbean", "Caribbean", "Americas"},
                new String[]{"BLZ," +
                        "CRI," +
                        "SLV," +
                        "GTM," +
                        "HND," +
                        "MEX," +
                        "NIC," +
                        "PAN","Latin America and the Caribbean", "Central America","Americas"},
                new String[]{"ARG," +
                        "BOL," +
                        "BVT," +
                        "BRA," +
                        "CHL," +
                        "COL," +
                        "ECU," +
                        "FLK," +
                        "GUF," +
                        "GUY," +
                        "PRY," +
                        "PER," +
                        "SGS," +
                        "SUR," +
                        "URY," +
                        "VEN","Latin America and the Caribbean", "South America", "Americas"},
                new String[]{"BMU," +
                        "CAN," +
                        "GRL," +
                        "SPM," +
                        "USA","Northern America", "", "Americas"},
                new String[]{"KAZ," +
                        "KGZ," +
                        "TJK," +
                        "TKM," +
                        "UZB","Central Asia", "", "Asia"},
                new String[]{"CHN," +
                        "HKG," +
                        "MAC," +
                        "PRK," +
                        "JPN," +
                        "MNG," +
                        "KOR","Eastern Asia", "", "Asia"},
                new String[]{"BRN," +
                        "KHM," +
                        "IDN," +
                        "LAO," +
                        "MYS," +
                        "MMR," +
                        "PHL," +
                        "SGP," +
                        "THA," +
                        "TLS," +
                        "VNM","South-eastern Asia", "", "Asia"},
                new String[]{"AFG," +
                        "BGD," +
                        "BTN," +
                        "IND," +
                        "IRN," +
                        "MDV," +
                        "NPL," +
                        "PAK," +
                        "LKA","Southern Asia", "", "Asia"},
                new String[]{"ARM," +
                        "AZE," +
                        "BHR," +
                        "CYP," +
                        "GEO," +
                        "IRQ," +
                        "ISR," +
                        "JOR," +
                        "KWT," +
                        "LBN," +
                        "OMN," +
                        "QAT," +
                        "SAU," +
                        "PSE," +
                        "SYR," +
                        "TUR," +
                        "ARE," +
                        "YEM","Western Asia", "", "Asia"},
                new String[]{"BLR," +
                        "BGR," +
                        "CZE," +
                        "HUN," +
                        "POL," +
                        "MDA," +
                        "ROU," +
                        "RUS," +
                        "SVK," +
                        "UKR","Eastern Europe", "", "Europe"},
                new String[]{"ALA," + "DNK," +
                        "EST," +
                        "FRO," +
                        "FIN," +
                        "ISL," +
                        "IRL," +
                        "IMN," +
                        "LVA," +
                        "LTU," +
                        "NOR," +
                        "SJM," +
                        "SWE," +
                        "GBR","Northern Europe", "", "Europe"},
                new String[]{"GGY,JEY,SAR","Northern Europe", "Channel Islands", "Europe"},
                new String[]{"ALB," +
                        "AND," +
                        "BIH," +
                        "HRV," +
                        "GIB," +
                        "GRC," +
                        "VAT," +
                        "ITA," +
                        "MLT," +
                        "MNE," +
                        "PRT," +
                        "SMR," +
                        "SRB," +
                        "SVN," +
                        "ESP," +
                        "MKD","Southern Europe", "", "Europe"},
                new String[]{"AUT," +
                        "BEL," +
                        "FRA," +
                        "DEU," +
                        "LIE," +
                        "LUX," +
                        "MCO," +
                        "NLD," +
                        "CHE","Western Europe", "", "Europe"},
                new String[]{"AUS," +
                        "CXR," +
                        "CCK," +
                        "HMD," +
                        "NZL," +
                        "NFK","Australia and New Zealand", "", "Oceania"},
                new String[]{"FJI," +
                        "NCL," +
                        "PNG," +
                        "SLB," +
                        "VUT","Melanesia", "", "Oceania"},
                new String[]{"GUM," +
                        "KIR," +
                        "MHL," +
                        "FSM," +
                        "NRU," +
                        "MNP," +
                        "PLW," +
                        "UMI","Micronesia", "", "Oceania"},
                new String[]{"ASM," +
                        "COK," +
                        "PYF," +
                        "NIU," +
                        "PCN," +
                        "WSM," +
                        "TKL," +
                        "TON," +
                        "TUV," +
                        "WLF","Polynesia", "", "Oceania"}
        };

    }

    public void setHeader(String[] header) {

        this.header = Arrays.asList(header);
    }

    public void setEntry(String[] entry) {

        this.entry = entry;
    }

    public void setIndexes() {


        this.index_sample_origin_latitude = header.indexOf("sample_origin_latitude");
        this.index_sample_origin_longitude = header.indexOf("sample_origin_longitude");
        this.index_sample_origin_region = header.indexOf("sample_origin_region");
        this.index_sample_origin_subregion = header.indexOf("sample_origin_subregion");
        this.index_sample_origin_intermediate_region = header.indexOf("sample_origin_intermediate_region");
        this.index_sample_origin_country = header.indexOf("sample_origin_country");

        this.index_sampling_latitude = header.indexOf("sampling_latitude");
        this.index_sampling_longitude = header.indexOf("sampling_longitude");
        this.index_sampling_region = header.indexOf("sampling_region");
        this.index_sampling_subregion = header.indexOf("sampling_subregion");
        this.index_sampling_intermediate_region = header.indexOf("sampling_intermediate_region");
        this.index_sampling_country = header.indexOf("sampling_country");

        this.index_TMA_inferrred_latitude = header.indexOf("geographic_info_tma_inferred_latitude");
        this.index_TMA_inferrred_longitude = header.indexOf("geographic_info_tma_inferred_longitude");
        this.index_TMA_inferrred_region = header.indexOf("geographic_info_tma_inferred_region");
        this.index_TMA_inferrred_subregion = header.indexOf("geographic_info_tma_inferred_subregion");
        this.index_TMA_inferrred_intermediate_region = header.indexOf("geographic_info_tma_inferred_intermediate_region");
        this.index_TMA_inferrred_country = header.indexOf("geographic_info_tma_inferred_country");


    }

    public String[] getCompletedInformation() {

        try {
            // complete sample origin geo
            String sample_lat = entry[this.index_sample_origin_latitude];
            String sample_long = entry[this.index_sample_origin_longitude];
            String sample_region = entry[this.index_sample_origin_region];
            String sample_subregion = entry[this.index_sample_origin_subregion];
            String sample_inter_region = entry[this.index_sample_origin_intermediate_region];
            String sample_country = entry[this.index_sample_origin_country];

            fillMissingGeoSample(sample_lat, sample_long,sample_subregion, sample_inter_region, sample_country);

            // complete sampling geo
            String sampling_lat = entry[this.index_sampling_latitude];
            String sampling_long = entry[this.index_sampling_longitude];
            String sampling_region = entry[this.index_sampling_region];
            String sampling_subregion = entry[this.index_sampling_subregion];
            String sampling_inter_region = entry[this.index_sampling_intermediate_region];
            String sampling_country = entry[this.index_sampling_country];

            fillMissingGeoSampling(sampling_lat, sampling_long,sampling_subregion, sampling_inter_region, sampling_country);

            // complete TMA inferred geo
            String sample_tma_lat = entry[this.index_TMA_inferrred_latitude];
            String sample_tma_long = entry[this.index_TMA_inferrred_longitude];
            String sample_tma_region = entry[this.index_TMA_inferrred_region];
            String sample_tma_subregion = entry[this.index_TMA_inferrred_subregion];
            String sample_tma_inter_region = entry[this.index_TMA_inferrred_intermediate_region];
            String sample_tma_country = entry[this.index_TMA_inferrred_country];

            fillMissingGeoTma(sample_tma_lat, sample_tma_long,sample_tma_subregion, sample_tma_inter_region, sample_tma_country);
        } catch (Exception e) {
            return entry;
        }


        return entry;
    }


    private void fillMissingGeoSample(String lat, String lon, String subregion, String inter_region, String country){

        if(!lat.equals("") && !lon.equals("")){
            fillBasedOnLatLong(Double.parseDouble(lat), Double.parseDouble(lon), index_sample_origin_country, index_sample_origin_region,
                    index_sample_origin_subregion, index_sample_origin_intermediate_region);

        } else if(!country.equals("")){
            fillBasedOnCountry(country, index_sample_origin_region, index_sample_origin_subregion, index_sample_origin_intermediate_region, index_sample_origin_country);

        } else if(!inter_region.equals("")){
            fillBasedOnIntermediate(inter_region, index_sample_origin_subregion, index_sample_origin_region);

        } else if(!subregion.equals("")){
            fillBasedOnSubregion(subregion, index_sample_origin_region);
        }
    }


    private void fillMissingGeoSampling(String lat, String lon, String subregion, String inter_region, String country){

        if(!lat.equals("") && !lon.equals("")){
            fillBasedOnLatLong(Double.parseDouble(lat), Double.parseDouble(lon), index_sampling_country, index_sampling_region,
                    index_sampling_subregion, index_sampling_intermediate_region);

        } else if(!country.equals("")){
            fillBasedOnCountry(country, index_sampling_region, index_sampling_subregion, index_sampling_intermediate_region, index_sampling_country);

        } else if(!inter_region.equals("")){
            fillBasedOnIntermediate(inter_region, index_sampling_subregion, index_sampling_region);

        } else if(!subregion.equals("")){
            fillBasedOnSubregion(subregion, index_sampling_region);
        }


    }


    private void fillMissingGeoTma(String lat, String lon, String subregion, String inter_region, String coun){

        if(!lat.equals("") && !lon.equals("")){
            //System.out.println("Lat Long given, calculate everything else.");
            fillBasedOnLatLong(Double.parseDouble(lat), Double.parseDouble(lon), index_TMA_inferrred_country, index_TMA_inferrred_region,
                    index_TMA_inferrred_subregion, index_TMA_inferrred_intermediate_region);

        } else if(!coun.equals("")){
            //System.out.println("Country given.");
            fillBasedOnCountry(coun, index_TMA_inferrred_region, index_TMA_inferrred_subregion, index_TMA_inferrred_intermediate_region, index_TMA_inferrred_country);

        } else if(!inter_region.equals("")){
            //System.out.println("Intermediate region given.");
            fillBasedOnIntermediate(inter_region, index_TMA_inferrred_subregion, index_TMA_inferrred_region);

        } else if(!subregion.equals("")){
            //System.out.println("Subregion region given.");
            fillBasedOnSubregion(subregion, index_TMA_inferrred_region);
        }

    }



    private void fillBasedOnLatLong(double lat, double lon, int index_country, int index_region,
                                    int index_subregion, int index_intermediate_region){

        geocoder.getCountry(lat, lon).ifPresent(country -> {

            if( country.iso3()==null){
                entry[index_country] = "";
            } else {
                if(country.iso3().equals("USA"))
                    entry[index_country] = "USA";
                else
                    entry[index_country] = country.name();
            }

            if( parseContinent(country.continent())==null){
                entry[index_region]= "";
            } else {
                entry[index_region] = parseContinent(country.continent());
            }

            if( getSubRegion(country.iso3())==null){
                entry[index_subregion] = "";
            } else {
                entry[index_subregion] = getSubRegion(country.iso3());
            }

            if( getIntermediateRegion(country.iso3())==null){
                entry[index_intermediate_region] = "";
            } else {
                entry[index_intermediate_region] = getIntermediateRegion(country.iso3());
            }

        });
    }


    private void fillBasedOnCountry(String country, int index_region, int index_subregion, int index_intermediate_region, int index_country){
        entry[index_region] = parseContinent(getContinent(country));
        entry[index_subregion] = getSubRegion(country);
        entry[index_intermediate_region] = getIntermediateRegion(country);
        if(localeMap.keySet().contains(country))
            entry[index_country] = localeMap.get(country).getDisplayCountry();
    }

    private void fillBasedOnIntermediate(String intermediate, int index_subregion, int index_region){

        for(int i = 0; i < m49_list.length; i++){
            if(Arrays.asList(m49_list[i][2].split(",")).contains(intermediate)){
                entry[index_subregion] = m49_list[i][1];
                entry[index_region] = m49_list[i][3];
            }
        }
    }

    private void fillBasedOnSubregion(String subregion, int index_region){

        for(int i = 0; i < m49_list.length; i++){
            if(Arrays.asList(m49_list[i][1].split(",")).contains(subregion)){
                entry[index_region] = m49_list[i][3];
                break;
            }
        }
    }



    private String getIntermediateRegion(String country) {

        if(country.length() != 3){
            country = convertCountryNameToIsoCode(country);
        }

        for(int i = 0; i < m49_list.length; i++){
            if(Arrays.asList(m49_list[i][0].split(",")).contains(country)){
                return m49_list[i][2];
            }
        }


        return null;
    }


    private String getSubRegion(String country) {

        if(country.length() != 3){
            country = convertCountryNameToIsoCode(country);
        }

        for(int i = 0; i < m49_list.length; i++){
            List<String> list = Arrays.asList(m49_list[i][0].split(","));
            if(list.contains(country)){
                return m49_list[i][1];
            }
        }
        return null;
    }


    private String getContinent(String country){

        if(country.length() != 3){
            country = convertCountryNameToIsoCode(country);
        }

        for(int i = 0; i < m49_list.length; i++){
            if(Arrays.asList(m49_list[i][0].split(",")).contains(country)){
                return m49_list[i][3];
            }
        }
        return null;
    }

    private String parseContinent(String continent) {
        if(continent!=null){

            if(continent.equals("EU")){
                return "Europe";
            } else if(continent.equals("AF")){
                return "Africa";
            } else if(continent.equals("SA") || continent.equals("NA")){
                return "Americas";
            } else if(continent.equals("AN")){
                return "Antarctica";
            } else if(continent.equals("AS")){
                return "Asia";
            } else if(continent.equals("OC")){
                return "Oceania";
            }
        }
        return continent;
    }

    private String convertCountryNameToIsoCode(String countryName){

        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes){

            Locale locale = new Locale("", countryCode);
            String iso = locale.getISO3Country();
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();

            if(name.equals(countryName)){
                return iso;
            }
        }
        return null;
    }

}
