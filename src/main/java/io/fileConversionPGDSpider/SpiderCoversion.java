package io.fileConversionPGDSpider;


import java.io.IOException;

/**
 * Created by neukamm on 14.03.17.
 */
public class SpiderCoversion {

    public SpiderCoversion() throws IOException, InterruptedException {

      start();

    }

    public void start() throws IOException, InterruptedException {

        String dirpath = System.getProperty("user.dir") +  "/jar/PGDSpider2.jar";
        //String dirpath = this.getClass().getResource("/jar/PGDSpider2.jar").toExternalForm();
        //ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "java", "-jar", dirpath.split(":")[1]});
        ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "java", "-jar", dirpath});
        Process process = processBuilder.start();
        process.waitFor();
    }

}
