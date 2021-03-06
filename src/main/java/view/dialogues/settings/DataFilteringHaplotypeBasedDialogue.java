package view.dialogues.settings;

import Logging.LogClass;
import filtering.FilterData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import statistics.HaplotypeStatistics;
import view.MitoBenchWindow;


/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringHaplotypeBasedDialogue extends ATabpaneDialogue {
    private final MitoBenchWindow mitoBenchWindow;
    private TextField field_mutation;
    private TextField field_distance;
    private Button btn_apply;

    public DataFilteringHaplotypeBasedDialogue(String title, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);
        dialogGrid.setId("mutationFilterDialogue");
        mitoBenchWindow = mito;
        addComponents();
        addListener();

    }

    private void addComponents() {
        Label label_enterMutation = new Label("Enter Haplotype");
        label_enterMutation.setId("label_enterMutation");

        field_mutation = new TextField();
        field_mutation.setId("field_mutation");

        Label label_distance = new Label("Get all data with distance ");
        field_distance = new TextField("0");
        field_distance.setId("field_distance");

        btn_apply = new Button("Apply filter");
        btn_apply.setId("btnApplyMutDialogue");

        dialogGrid.add(label_enterMutation, 0,0,1,1);
        dialogGrid.add(field_mutation,1,0,1,1);
        dialogGrid.add(label_distance, 0,2,1,1);
        dialogGrid.add(field_distance, 1,2,1,1);
        dialogGrid.add(btn_apply, 0, 3,2,1);


    }

    private void addListener() {

        btn_apply.setOnAction(t -> {

            HaplotypeStatistics mutationStatistics = new HaplotypeStatistics(logClass,mitoBenchWindow.getPrimaryStage());

            mutationStatistics.calculateHaplotypeFrequencies(mitoBenchWindow.getTreeController().getTree().getMutations_per_hg(),
                    mitoBenchWindow.getTableControllerUserBench().getTableColumnByName("Haplogroup"),
                    mitoBenchWindow.getTableControllerUserBench().getTable(),
                    mitoBenchWindow.getTreeController());

            FilterData filterData = new FilterData(
                    mitoBenchWindow.getTreeController().getTree().getMutations_per_hg(),
                    mitoBenchWindow.getTableControllerUserBench(),
                    mutationStatistics.getHgs_per_mutation_of_current_data(),
                    logClass);
            try {
                filterData.filterHaplotype(field_mutation.getText().split(","),   field_distance.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mitoBenchWindow.getTabpane_statistics().getTabs().remove(getTab());
        });


    }

}