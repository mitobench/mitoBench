package dataCompleter;

public class Statistics {


    public double calculatePercentageOfN(String sequence) {

        int count_n = 0;
        for(char c : sequence.toCharArray()){
            if(c == 'N'){
                count_n++;
            }
        }
        // round to 2 digits
        return Math.round((count_n/(double)sequence.length()) * 100.0) / 100.0;
    }

}
