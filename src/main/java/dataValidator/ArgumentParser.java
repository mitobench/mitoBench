package dataValidator;

import org.apache.commons.cli.*;

public class ArgumentParser {


    private String[] args;
    private static final String CLASS_NAME = "User option parser";
    private String meta;
    private String fasta;


    public ArgumentParser(String[] args) {
        this.args = args;
        parse();
    }


    private void parse() {
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show this help page");
        Options options = new Options();
        options.addOption("h", "help", false, "show this help page");
        options.addOption(OptionBuilder.withLongOpt("input")
                .withArgName("INPUT")
                .withDescription("The input meta data file (csv file)")
                .isRequired()
                .hasArg()
                .create("i"));
        options.addOption(OptionBuilder.withLongOpt("fasta")
                .withArgName("FASTA")
                .withDescription("The fasta file with MT DNA sequences")
                .hasArg()
                .create("f"));


        HelpFormatter helpformatter = new HelpFormatter();
        CommandLineParser parser = new BasicParser();

        if (args.length != 4) {
            helpformatter.printHelp(CLASS_NAME, options);
            System.exit(0);
        }

        try {
            CommandLine cmd = parser.parse(helpOptions, args);
            if (cmd.hasOption('h')) {
                helpformatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }
        } catch (ParseException e1) {
        }

        try {
            CommandLine cmd = parser.parse(options, args);

            // input files

            if (cmd.hasOption('i')) {
                meta = cmd.getOptionValue('i');
            }
            if (cmd.hasOption('f')) {
                fasta = cmd.getOptionValue('f');
            }

        } catch (ParseException e) {
            helpformatter.printHelp(CLASS_NAME, options);
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }


    public String getMeta() {
        return meta;
    }

    public String getFasta() {
        return fasta;
    }
}
