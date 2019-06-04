package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import statistics.HaploStatistics;

import java.util.Arrays;


/**
 * Created by neukamm on 10.01.17.
 */
public class HGStatisticsPopupDialogue extends AHGDialogue {

    public HGStatisticsPopupDialogue(String title, LogClass LOGClass){
        super(title, LOGClass);
        dialogGrid.setId("statistics_popup");

    }



}
