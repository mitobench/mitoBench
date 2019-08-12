package analysis.fstcalculation;

import java.util.ArrayList;
import java.util.List;

public class Permutation {

    List<int[]> used_indices;

    public Permutation(){
        this.used_indices = new ArrayList<>();
    }

    public List[] permuteSamples(List<String> sample1, List<String> sample2){

        List<String> sample1_copy = new ArrayList<>(sample1);
        List<String> sample2_copy = new ArrayList<>(sample2);

        int randomNumSample1 = 0 + (int)(Math.random() * sample1_copy.size());
        int randomNumSample2 = 0 + (int)(Math.random() * sample2_copy.size());

        if(!used_indices.contains(new int[]{randomNumSample1, randomNumSample2})){
            // System.out.println("Permutation index: " + randomNumSample1 + " |  " + randomNumSample2);
            String seq1 = sample1_copy.get(randomNumSample1);
            String seq2 = sample2_copy.get(randomNumSample2);

            sample1_copy.remove(randomNumSample1);
            sample2_copy.remove(randomNumSample2);

            sample1_copy.add(seq2);
            sample2_copy.add(seq1);

            return new List[]{sample1_copy, sample2_copy};

        }
        return null;
    }

    public void clear(){
        used_indices.clear();
    }
}
