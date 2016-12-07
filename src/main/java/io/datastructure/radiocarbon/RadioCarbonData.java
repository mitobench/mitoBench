package io.datastructure.radiocarbon;

import view.table.datatypes.IData;

/**
 * Created by peltzer on 05/12/2016.
 */
public class RadioCarbonData implements IData {
    private int upper_limit;
    private int lower_limit;
    private double average;

    //Type Handlers
    public static final int DEFAULT = 0;
    public static final int PARSE_C14_DATE_INFORMATION = 1;


    public RadioCarbonData(Double input) {
        this.average = input;
        this.upper_limit = 0;
        this.lower_limit = 0; // No upper/lower limits given in these cases
    }


    public RadioCarbonData(String toParse, int config) {
        parseC14Information(toParse);
    }


    private void parseC14Information(String toParseThis) {
        //ignore anything called "cal", "CAL" or similar
        String removed_cal = toParseThis.replaceAll("cal|CAL", "").trim();
        //check for AD/BC information
        if ((removed_cal.contains("BC") && removed_cal.contains("AD")) | (removed_cal.contains("ad") && removed_cal.contains("bc"))) { //Special case, we have -BC and +AD dates here
            String removed_adbc = removed_cal.replace("AD", "").replace("ad", "").replace("BC", "").replace("bc", "").trim().replaceAll(" ", "");
            String[] split = removed_adbc.split("-");//cal BC 44-cal AD 16
            lower_limit = -Integer.parseInt(split[0].trim());
            upper_limit = Integer.parseInt(split[1].trim());
            this.average = Math.abs(lower_limit - upper_limit) / 2 + lower_limit;
        } else if (removed_cal.contains("AD") | removed_cal.contains("ad")) {
            String removed_ad = removed_cal.replace("AD", "").replace("ad", "").trim();
            String[] split = removed_ad.split("-");
            try {
                lower_limit = Integer.parseInt(split[0].trim());
                upper_limit = Integer.parseInt(split[1].trim());
                this.average = lower_limit + ((upper_limit - lower_limit) / 2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (removed_cal.contains("BC") | removed_cal.contains("bc")) {
            String removed_bc = removed_cal.replace("BC", "").replace("bc", "").trim();
            String[] split = removed_bc.split("-");

            lower_limit = -Integer.parseInt(split[0].trim());
            upper_limit = -Integer.parseInt(split[1].trim());
            this.average = lower_limit + (Math.abs(lower_limit) - Math.abs(upper_limit)) / 2;
        }
    }


    public int getUpper_limit() {
        return upper_limit;
    }

    public int getLower_limit() {
        return lower_limit;
    }

    public double getAverage() {
        return average;
    }

    @Override
    public String getTableInformation() {
        return String.valueOf(average);
    }
}
