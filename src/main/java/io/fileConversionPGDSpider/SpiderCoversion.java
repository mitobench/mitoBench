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
        String dirpath = System.getProperty("user.dir");
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", dirpath+"/jar/PGDSpider2.jar");
        Process process = processBuilder.start();
        process.waitFor();
    }

}
