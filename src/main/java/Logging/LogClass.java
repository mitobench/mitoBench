package Logging;

import org.apache.log4j.*;

import java.net.URL;

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
        URL url = this.getClass().getResource("/logger.properties");
        PropertyConfigurator.configure(url.getFile());
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
        System.setProperty("logfile.name", logFile);
    }
}
