package analysis.fstcalculation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsefulFunctions {


    /**
     * This method calculates all the number of '1's in a List
     * @param list of integer
     * @return number of '1's
     */
    public int count1(List<Integer> list){
        int count = 0;
        for(int i : list){
            if(i==1)
                count++;
        }
        return count;
    }


    /**
     * This method counts the occurrences of all elements in an char array.
     *
     * @param chararray
     * @return
     */
    public Map<Character, Integer> count(char[] chararray) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character character : chararray) {
            Integer characterCount = map.get(character);
            if(characterCount == null) {
                characterCount = 0;
            }
            characterCount++;
            map.put(character, characterCount);
        }

        return map;
    }

    /**
     * This method rounds a double value on n digits.
     * @param value
     * @param numberOfDigitsAfterDecimalPoint
     * @return double rounded
     */
    public double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal.doubleValue();
    }



    /**
     * Transpose matrix.
     *
     * @param matrix
     * @return
     */
    public double[][] transposeMatrix(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] transposedMatrix = new double[n][m];

        for(int x = 0; x < n; x++)
        {
            for(int y = 0; y < m; y++)
            {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
    }
}
