package Logging;

import org.apache.log4j.*;

import java.io.File;

/**
 * Created by neukamm on 25.02.17.
 */
public class LogClass {

    public LogClass(){}


    /**
     * Set up logger properties.
     * Use file logger.properties for changes.
     */

    public void setUp(){
        String configFile = System.getProperty("user.dir") + File.separator + "src/main/resources/logger.properties";
        PropertyConfigurator.configure(configFile);
    }

    /**
     * Return logger for specific Class
     * @param c
     * @return
     */

    public Logger getLogger(Class c){
        // creates a custom logger and log messages
        return Logger.getLogger(c);
    }


    /**
     * Update logger configuration.
     * i.e. set user specific file path
     * @param logFile
     */

    public void updateLog4jConfiguration(String logFile) {
        System.setProperty("logfile.name", logFile);// + File.separator + "mitobench_log_" + dateFormat.format(date) + ".log");
    }
}
