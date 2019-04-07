package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.*;

public class GeoMapDisplay extends ATabpaneDialogue {


    private RadioButton rb_location_sampling;
    private RadioButton rb_location_sample;
    private RadioButton rb_location_TMA;
    private Label textField_sample_info;

    public GeoMapDisplay(String title, int size, LogClass logClass) {
        super(title, logClass);
        addComponents(size);
    }

    private void addComponents(int size) {


        ToggleGroup group = new ToggleGroup();

        rb_location_sampling = new RadioButton("Display sampling location");
        rb_location_sampling.setToggleGroup(group);

        rb_location_sample = new RadioButton("Display sample location");
        rb_location_sample.setToggleGroup(group);
        rb_location_sample.setSelected(true);

        rb_location_TMA = new RadioButton("Display TMA inferred location");
        rb_location_TMA.setToggleGroup(group);

        textField_sample_info = new Label();
        textField_sample_info.setText("# of samples: " + size );

        dialogGrid.add(rb_location_sample, 1,0,1,1);
        dialogGrid.add(rb_location_sampling, 1,1,1,1);
        dialogGrid.add(rb_location_TMA, 1,2,1,1);
        dialogGrid.add(new Separator(), 0,3,3,1);
        dialogGrid.add(textField_sample_info, 1,4,1,1);

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

    public Label getTextField_sample_info() {
        return textField_sample_info;
    }
}
