package phyloTreeParser;

import javafx.scene.control.TreeItem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 17.11.16.
 */
public class PhylotreeParserCSV {


    private static BufferedReader bfr ;
    private static FileReader fr;
    private  static TreeItem<String> finalTree = null;

//    public static void main(String[] args) throws IOException {
//        //need this for main access
//        if(args.length != 1) {
//            System.err.println("You have to specify the input HTML object first!");
//            System.exit(-1);
//        } else {
//            File f = new File(args[0]);
//            PhylotreeParserCSV phyp = new PhylotreeParserCSV();
//        }
//    }


//    public PhylotreeParserCSV(File input) throws IOException {
//        parseFile(input);
//        //TODO write output ?
//    }


    private static void parseFile() throws IOException {
        File file = new File("src/mtdnacsv.csv");
        //We require a CSV file as input, get this by storign the HTML table (single file), open it in Excel as HTML -> save as CSV and you're done!
        //The ";" array size determines where to place a file correctly in our Tree
        fr = new FileReader(file);
        bfr = new BufferedReader(fr);

        ArrayList<String> entries = new ArrayList<String>();
        String currline = "";

        int startindex = 0;

        while((currline = bfr.readLine()) != null){
            //Ignore first 18 lines, ignore lines consisting solely of ";;;;;;;;;;;;;;;;;;;;;;;;;;"
            if((startindex < 18) || (currline.equals(";;;;;;;;;;;;;;;;;;;;;;;;;;"))){
                startindex++;
                continue;
            } else {
                entries.add(currline);
            }
        }


        /**
         * We have now cleared items here in the list and can start creating our tree.
         * There are three cases for tree traversal:
         *
         * - keep current level in the index, add other nodes to the current parent, repeat till end or level changes
         * - if level up -> add children to former node, start new parent node
         * - if level down -> continue going down, add to parent of current node
         * - ideally recursive function or something like this (!)
         */

        TreeItem rootItem = new TreeItem("RSRS");
        rootItem.setExpanded(true);
        List<TreeItem> tree_items = new ArrayList<TreeItem>();
        tree_items.add(rootItem);


        // iterate post-order through tree
        int currIndex = 0;
        int formerIndex = 0;

        for(String array : entries) {
            if(array.length()!=0){
                currIndex = getLevel(array);
                String haplogroup = getHaplogroup(array);
                if(haplogroup.equals("L0d")){
                    System.out.println("");
                }
                TreeItem<String> item = new TreeItem<>(haplogroup);

                if(currIndex == 0) { // can only happen in the initialization phase (for L0, and L1'2'3'4'5'6')
                    rootItem.getChildren().add(item);
                    // update tree_item, set only rootItem
                    List<TreeItem> back_me_up = tree_items;
                    tree_items = updateIndices(back_me_up,1); // Update our "pointer" list
                    tree_items.add(item);
                    formerIndex = currIndex;
                    continue;
                }

                if (currIndex > formerIndex) { //then we are going down one level
                    tree_items.get(tree_items.size()-1).getChildren().add(item);
                    tree_items.add(item);
                    formerIndex = currIndex;

                } else if (currIndex == formerIndex) { //then we are in the same level with our sibling node
                    tree_items.get(tree_items.size()-1).getParent().getChildren().add(item);
                    tree_items.add(item);

                } else if (currIndex < formerIndex) { // then we are done traversing and have to go one level up again
                    formerIndex = currIndex;
                    List<TreeItem> back_me_up = tree_items;
                    tree_items = updateIndices(back_me_up,currIndex+1); // Update our "pointer" list
                    tree_items.get(tree_items.size()-1).getChildren().add(item);
                    tree_items.add(item);
                }




            }

        }


        finalTree = rootItem;
    }

    /**
     * Method returns the leading ";" symbols count of each line on search. This is used to determine where we are in our CSV tree in the end.
     * @param s
     * @return
     */
    public static int getLevel(String s){
        if(!s.startsWith(";")){
            System.out.println("");
        }
        int level = 0;
        for (int i = 0; i <= s.length(); i++){
            if(s.length()!=0 && s.charAt(i) == ';'){
                level++;
            } else {
                return level;
            }
        }
        return level;
    }

    /**
     * Method to return hpalogroup string
     * @param s
     * @return
     */
    public static String getHaplogroup(String s){
        String tmp = s.replaceFirst("^;*","");
        String[] splitted = tmp.split(";");
        return splitted[0];


    }

    /**
     * Method to update index array.
     * Deletes all indices which are not needed anymore.
     *
     * @param index_array
     * @param level
     * @return
     */
    public static List<TreeItem> updateIndices(List<TreeItem> index_array, int level){
        return index_array.subList(0,level); //sublist is (inclusive, exclusive)

    }



    public static TreeItem<String> getFinalTree() throws IOException{
        parseFile();
        return finalTree;
    }

}
