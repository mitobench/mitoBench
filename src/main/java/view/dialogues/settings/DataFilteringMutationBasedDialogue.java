package view.dialogues.settings;

import Logging.LogClass;
import filtering.FilterData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.MitoBenchWindow;


/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringMutationBasedDialogue extends APopupDialogue {
    private final MitoBenchWindow mitoBenchWindow;
    private TextField field_mutation;
    private TextField field_distance;
    private Button btn_apply;

    public DataFilteringMutationBasedDialogue(String title, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);
        dialogGrid.setId("mutationFilterDialogue");
        mitoBenchWindow = mito;
        addComponents();
        addListener();
        show();

    }

    private void addComponents() {
        Label label_enterMutation = new Label("Enter Mutation");
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

        btn_apply.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FilterData filterData = new FilterData(
                        mitoBenchWindow.getTableControllerUserBench(),
                        mitoBenchWindow.getTreeController().getTree().getHgs_per_mutation());
                try {
                    filterData.filterMutation(field_mutation.getText().split(","));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                close();
            }
        });


    }

}
