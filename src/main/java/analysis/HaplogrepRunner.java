package analysis;

import java.io.IOException;

/**
 * Created by neukamm on 07.07.17.
 */
public class HaplogrepRunner {

    public HaplogrepRunner() throws IOException, InterruptedException {

        start();

    }

    public void start() throws IOException, InterruptedException {
        String dirpath = this.getClass().getResource("/jar/haplogrep-2.1.1.jar").toExternalForm();
        ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "java", "-jar", dirpath.split(":")[1]});
        Process process = processBuilder.start();
        process.waitFor();
    }

}
