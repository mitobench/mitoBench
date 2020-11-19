package io.fileConversionPGDSpider;
import ch.unibe.iee.cmpg.pgdspider.gui.PGDSpiderGUI;


/**
 * Created by neukamm on 14.03.17.
 */
public class SpiderCoversion {

    public SpiderCoversion() {

      start();

    }

    public void start() {

        PGDSpiderGUI pgdSpiderGUI = new PGDSpiderGUI();
        System.out.println("Starting PGDSpider");
        pgdSpiderGUI.setVisible(true);

    }

}
