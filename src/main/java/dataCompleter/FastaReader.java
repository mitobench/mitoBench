package dataCompleter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FastaReader {

    private String [] description;
    private String [] sequence;
    private String log_sequence_corretness="";
    private boolean correct=true;
    private boolean correct_chars=true;
    private HashMap<String, String> sequenceMap;

    public FastaReader(String filename)
    {
        readSequenceFromFile(filename);

    }

    void readSequenceFromFile(String file)
    {
        List desc= new ArrayList();
        List seq = new ArrayList();
        try{
            BufferedReader in     = new BufferedReader( new FileReader( file ) );
            StringBuffer   buffer = new StringBuffer();
            String         line   = in.readLine();

            if( line == null )
                throw new IOException( file + " is an empty file" );

            if( line.charAt( 0 ) != '>' )
                throw new IOException( "First line of " + file + " should start with '>'" );
            else
                desc.add(line.replace(">",""));
            for( line = in.readLine().trim(); line != null; line = in.readLine() )
            {
                if( line.length()>0 && line.charAt( 0 ) == '>' )
                {
                    String sequence = buffer.toString();
                    if (buffer.toString().contains("-")){
                        correct=false;
                    } else if(!isSequenceValid(buffer.toString())){
                        correct_chars=false;
                    }
                    seq.add(buffer.toString());
                    buffer = new StringBuffer();
                    desc.add(line.replace(">",""));
                    if(!correct && correct_chars){
                        log_sequence_corretness += "Sequence '" + line.replace(">","") + "' contains '-'\n" + sequence + "\n";
                        correct=true;
                    } else if(correct && !correct_chars){
                        log_sequence_corretness += "Sequence '" + line.replace(">","") + "' contains not allowed symbols.\n" + sequence + "\n";
                        correct_chars=true;
                    } else if(!correct && !correct_chars){
                        log_sequence_corretness += "Sequence '" + line.replace(">","") + "' contains '-' and not allowed symbols. \n" + sequence + "\n";
                        correct_chars=true;
                        correct=true;                    }

                } else
                    buffer.append( line.trim() );
            }
            if( buffer.length() != 0 )
                seq.add(buffer.toString());
        } catch(IOException e) {
            System.out.println("Error when reading "+file);
            e.printStackTrace();
        }

        description = new String[desc.size()];
        sequence = new String[seq.size()];
        for (int i=0; i< seq.size(); i++)
        {
            String acc = (String) desc.get(i);
            description[i] = acc.split(" ")[0].split("\\.")[0].trim();
            sequence[i]=(String) seq.get(i);
        }

    }

    private boolean isSequenceValid(String seq) {

        String specialCharacters = "[" + "ACGTUWSMKRYBDHVNZacgtuwsmkrybdhvnz"+ "]+" ;

        if (seq.matches(specialCharacters))
            return true;
        else
            return false;


    }


    public void parseFasta(){

        sequenceMap = new HashMap<>();

        for (int i=0; i< sequence.length; i++)
        {
            sequenceMap.put(description[i], sequence[i].trim());

        }
    }

    //return first sequence as a String
    public HashMap<String, String> getSequenceMap(){ return sequenceMap;}

    //return all description as List
    public List<String> getDescription(){return Arrays.asList(description);}

    public String getLog_sequence_corretness(){
        return log_sequence_corretness;
    }

}
