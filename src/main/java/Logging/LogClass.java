package Logging;

import org.apache.log4j.*;
import view.MitoBenchWindow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by neukamm on 25.02.17.
 */
public class LogClass {
    public LogClass(){}

    public void setUp(){

        String fileName = "mitobench";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Date date = new Date();

        PatternLayout layout = new PatternLayout();
        String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
        layout.setConversionPattern(conversionPattern);

        // creates console appender
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();

        // creates file appender
        FileAppender fileAppender = new FileAppender();
        String file = fileName + "_" + dateFormat.format(date) + ".log";
        fileAppender.setFile(file);
        fileAppender.setLayout(layout);
        fileAppender.activateOptions();


        // configures the root logger
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.ALL);
        rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(fileAppender);


    }

    public Logger getLogger(Class c){
        // creates a custom logger and log messages
        return Logger.getLogger(c);
    }
}
