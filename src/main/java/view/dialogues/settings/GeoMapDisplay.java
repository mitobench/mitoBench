package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class GeoMapDisplay extends ATabpaneDialogue {


    private RadioButton rb_location_sampling;
    private RadioButton rb_location_sample;
    private RadioButton rb_location_TMA;

    public GeoMapDisplay(String title, LogClass logClass) {
        super(title, logClass);
        addComponents();
    }

    private void addComponents() {


        ToggleGroup group = new ToggleGroup();

        rb_location_sampling = new RadioButton("Display sampling location");
        rb_location_sampling.setToggleGroup(group);

        rb_location_sample = new RadioButton("Display sample location");
        rb_location_sample.setToggleGroup(group);
        rb_location_sample.setSelected(true);

        rb_location_TMA = new RadioButton("Display TMA inferred location");
        rb_location_TMA.setToggleGroup(group);


        dialogGrid.add(rb_location_sample, 0,0,1,1);
        dialogGrid.add(rb_location_sampling, 0,1,1,1);
        dialogGrid.add(rb_location_TMA, 0,2,1,1);

    }

    public RadioButton getRb_location_sampling() {
        return rb_location_sampling;
    }

    public RadioButton getRb_location_sample() {
        return rb_location_sample;
    }

    public RadioButton getRb_location_TMA() {
        return rb_location_TMA;
    }

}
