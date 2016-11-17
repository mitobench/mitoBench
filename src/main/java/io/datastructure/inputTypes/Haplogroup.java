package io.datastructure.inputTypes;

/**
 * Created by peltzer on 17/11/2016.
 */

public class Haplogroup {
    String mainType = "";
    String subtype = "";

    public Haplogroup(String completeGroup) {
        this.mainType = mainTypeParser(completeGroup);
        this.subtype = subTypeParser(completeGroup);
    }

    private String subTypeParser(String completeGroup) {
        if (completeGroup.length() > 1) {
            int shift = completeGroup.lastIndexOf(this.mainType);
            return completeGroup.substring(shift + mainType.length());
        } else {
            return "";
        }
    }

    private String mainTypeParser(String completeGroup) {
        String haploGroup = completeGroup.replaceAll("[^A-Z]", "-");
        String haploId = completeGroup.replaceAll("[^0-9]", "-");

        String[] haploIdArray = haploId.split("-");
        for (String id : haploIdArray) {
            if (!id.isEmpty()) {
                haploId = id;
                break;
            }
        }

        String jabba = null;
        try {
            jabba = haploGroup.split("-")[0] + haploId.replace("-", "");
        } catch (Exception e) {
            System.err.println("Haplogroup splitting incorrect: " + completeGroup);
        }
        return jabba;
    }


    public String getMainType() {
        return mainType;
    }

    public String getSubtype() {
        return subtype;
    }
}

