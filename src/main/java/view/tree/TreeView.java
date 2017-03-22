package view.tree;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.HaploTreeModel;

/**
 * Created by neukamm on 17.03.17.
 */
public class TreeView {

    private Label searchLbl;
    private Button applyBtn;
    private TextField searchFieldListHaplogroup;
    private VBox treeSearchPane;

    private Rectangle2D boxBounds = new Rectangle2D(500, 300, 600, 480);
    private HaploTreeModel tree;

    public TreeView(HaploTreeModel tree) {
        this.tree = tree;
        initView();

    }

    private void initView() {

        searchLbl = new Label("Haplo tree");
        searchLbl.setId("treeViewOpenCloseLabel");

        applyBtn = new Button("Apply filter");
        applyBtn.setId("treeviewApplyButton");

        searchFieldListHaplogroup = new TextField();
        searchFieldListHaplogroup.setId("treeviewSearchField");
        searchFieldListHaplogroup.setPrefSize(50,10);

        Label infolabel = new Label("Please select Haplogroups either " +
                "in the tree OR specify the desired Haplogroups as list:");
        infolabel.setMinSize(80, 80);

        Label haploLabel = new Label("Comma separated list of haplogroups:");
        infolabel.setMinSize(20, 30);

        treeSearchPane = new VBox();
        treeSearchPane.setId("treeView-inner-tree");
        treeSearchPane.setPadding(new Insets(10));
        treeSearchPane.setAlignment(Pos.TOP_LEFT);
        treeSearchPane.setStyle("-fx-background-color:#333333,#b1afb0;-fx-background-insets:0,1.5;-fx-opacity:.92;-fx-background-radius:0px 0px 0px 5px;");
        double ACTION_BOX_HGT = 30;
        treeSearchPane.setPrefSize(boxBounds.getWidth(), boxBounds.getHeight()- ACTION_BOX_HGT);
        treeSearchPane.setSpacing(10);

        treeSearchPane.getChildren().addAll(infolabel, tree.getTree(), haploLabel, searchFieldListHaplogroup , applyBtn);


    }


    public Label getSearchLbl() {
        return searchLbl;
    }

    public void setSearchLbl(Label searchLbl) {
        this.searchLbl = searchLbl;
    }

    public Button getApplyBtn() {
        return applyBtn;
    }

    public void setApplyBtn(Button applyBtn) {
        this.applyBtn = applyBtn;
    }

    public TextField getSearchFieldListHaplogroup() {
        return searchFieldListHaplogroup;
    }

    public void setSearchFieldListHaplogroup(TextField searchFieldListHaplogroup) {
        this.searchFieldListHaplogroup = searchFieldListHaplogroup;
    }

    public VBox getTreeSearchPane() {
        return treeSearchPane;
    }

    public void setTreeSearchPane(VBox treeSearchPane) {
        this.treeSearchPane = treeSearchPane;
    }
}
