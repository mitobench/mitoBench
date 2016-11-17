package view.tree;

/**
 * Created by neukamm on 09.11.16.
 */

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.GroupBuilder;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.xml.sax.SAXException;
import view.charts.BarPlotHaplo;
import view.table.TableSelectionFilter;
import view.table.TableManager;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TreeHaploChooser {

    private VBox searchPane;
    private Rectangle2D boxBounds = new Rectangle2D(500, 300, 600, 480);
    private double ACTION_BOX_HGT = 30;
    private StackPane downArrow = StackPaneBuilder.create().style("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M0 0 L1 0 L.5 1 Z\";").maxHeight(10).maxWidth(15).build();
    private StackPane upArrow = StackPaneBuilder.create().style("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M0 1 L1 1 L.5 0 Z\";").maxHeight(10).maxWidth(15).build();
    private Label searchLbl = LabelBuilder.create().text("Tree View").graphic(downArrow).contentDisplay(ContentDisplay.RIGHT).build();
    private SimpleBooleanProperty isExpanded = new SimpleBooleanProperty();
    private Rectangle clipRect;
    private Timeline timelineUp;
    private Timeline timelineDown;
    private String[] seletcion_haplogroups;
    private TableManager tableManager;
    private BarPlotHaplo barPlotHaplo;
    private List<TreeItem<String>> foundItem;
    private TextField searchFieldListHaplogroup;

    public TreeHaploChooser(StackPane root, TableManager tableManager, BarPlotHaplo barPlotHaplo) throws IOException, SAXException, ParserConfigurationException {

        this.tableManager = tableManager;
        this.barPlotHaplo = barPlotHaplo;

        configureSearch(root);
        setAnimation();


        isExpanded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> paramObservableValue, Boolean paramT1, Boolean paramT2) {
                if(paramT2){
                    // To expand
                    timelineDown.play();
                    searchLbl.setGraphic(upArrow);

                }else{
                    // To close
                    timelineUp.play();
                    searchLbl.setGraphic(downArrow);
                }
            }
        });


    }



    /**
     * Method to configure the search pane.
     * @param
     */
    private void configureSearch(StackPane root) throws IOException, SAXException, ParserConfigurationException{
        searchPane = new VBox();
        //searchPane.autosize();
        searchPane.setAlignment(Pos.TOP_LEFT);

        StackPane sp1 = new StackPane();
        sp1.setPadding(new Insets(10));
        sp1.setAlignment(Pos.TOP_LEFT);
        sp1.setStyle("-fx-background-color:#333333,#EAA956;-fx-background-insets:0,1.5;-fx-opacity:.92;-fx-background-radius:0px 0px 0px 5px;");
        sp1.setPrefSize(boxBounds.getWidth(), boxBounds.getHeight()-ACTION_BOX_HGT);
        //sp1.autosize();

        Button applyBtn = new Button("Display selection");
        //TextField search = new TextField("Enter haplogroup");
        searchFieldListHaplogroup = new TextField();
        searchFieldListHaplogroup.setPrefSize(50,10);


        TreeHaplo tree = new TreeHaplo("Haplo tree");
        tree.addStructure();


        /*

                        get selection button


         */
        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                ObservableList<TreeItem<String>> itemSelection =  tree.getTree().getSelectionModel().getSelectedItems();
                seletcion_haplogroups = new String[itemSelection.size()];
                for(int i = 0; i < itemSelection.size(); i++){
                    seletcion_haplogroups[i] = itemSelection.get(i).getValue();
                }
                // close tree view
                timelineUp.play();
                searchLbl.setGraphic(downArrow);
                togglePaneVisibility();
                tree.getTree().getSelectionModel().clearSelection();


                // parse selection to tablefilter
                TableSelectionFilter tableFilter = new TableSelectionFilter();

                if (seletcion_haplogroups.length !=0) {
                    tableFilter.filter(tableManager, seletcion_haplogroups);
                    barPlotHaplo.addData("data selection", tableManager.getDataHist());
                }
            }
        });




        /*

                       search field filter


         */


        searchFieldListHaplogroup.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    // parse haplogroup list, get all corresponding table entries (subgroups included)
                    // and show results in table
                    getAllSubgroups(searchFieldListHaplogroup.getText().split(","));
                }
            }
        });


//        search.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable,
//                                String oldValue, String newValue) {
//
//
//
//                foundItem = new ArrayList<TreeItem<String>>();
//
//                //searchAndUpdateTreeItem(tree.getRootItem(), newValue, foundItem, tree.getTree());
//            }
//        });

        Label infolabel = new Label("Please select haplogroups either in the tree \nor specify a list:");
        infolabel.setMinSize(30, 50);

        Label haploLabel = new Label("Comma separated list of haplogroups:");
        infolabel.setMinSize(20, 30);


        VBox hb2 = VBoxBuilder.create().children(infolabel, tree.getTree(),applyBtn, haploLabel,searchFieldListHaplogroup).build();
        sp1.getChildren().addAll(hb2);

        StackPane sp2 = new StackPane();
        sp2.setPrefSize(100, ACTION_BOX_HGT);
        sp2.getChildren().add(searchLbl);
        sp2.setStyle("-fx-cursor:hand;-fx-background-color:#EAA956;-fx-border-width:0px 1px 1px 1px;-fx-border-color:#333333;-fx-opacity:.92;-fx-border-radius:0px 0px 5px 5px;-fx-background-radius:0px 0px 5px 5px;");
        sp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent paramT) {
                togglePaneVisibility();
            }
        });

        searchPane.getChildren().addAll(GroupBuilder.create().children(sp1).build(), GroupBuilder.create().children(sp2).build());
        root.getChildren().add(GroupBuilder.create().children(searchPane).build());


    }


    private void setAnimation(){
		/* Initial position setting for Top Pane*/
        clipRect = new Rectangle();
        clipRect.setWidth(boxBounds.getWidth());
        clipRect.setHeight(ACTION_BOX_HGT);
        clipRect.translateYProperty().set(boxBounds.getHeight()-ACTION_BOX_HGT);
        searchPane.setClip(clipRect);
        searchPane.translateYProperty().set(-(boxBounds.getHeight()-ACTION_BOX_HGT));

		/* Animation for bouncing effect. */
        final Timeline timelineDown1 = new Timeline();
        timelineDown1.setCycleCount(2);
        timelineDown1.setAutoReverse(true);
        final KeyValue kv1 = new KeyValue(clipRect.heightProperty(), (boxBounds.getHeight()-15));
        final KeyValue kv2 = new KeyValue(clipRect.translateYProperty(), 15);
        final KeyValue kv3 = new KeyValue(searchPane.translateYProperty(), -15);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(100), kv1, kv2, kv3);
        timelineDown1.getKeyFrames().add(kf1);

		/* Event handler to call bouncing effect after the scroll down is finished. */
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                timelineDown1.play();
            }
        };

        timelineDown = new Timeline();
        timelineUp = new Timeline();

        /* Animation for scroll down. */
        timelineDown.setCycleCount(1);
        timelineDown.setAutoReverse(true);
        final KeyValue kvDwn1 = new KeyValue(clipRect.heightProperty(), boxBounds.getHeight());
        final KeyValue kvDwn2 = new KeyValue(clipRect.translateYProperty(), 0);
        final KeyValue kvDwn3 = new KeyValue(searchPane.translateYProperty(), 0);
        final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), onFinished, kvDwn1, kvDwn2, kvDwn3);
        timelineDown.getKeyFrames().add(kfDwn);

		/* Animation for scroll up. */
        timelineUp.setCycleCount(1);
        timelineUp.setAutoReverse(true);
        final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), ACTION_BOX_HGT);
        final KeyValue kvUp2 = new KeyValue(clipRect.translateYProperty(), boxBounds.getHeight()-ACTION_BOX_HGT);
        final KeyValue kvUp3 = new KeyValue(searchPane.translateYProperty(), -(boxBounds.getHeight()-ACTION_BOX_HGT));
        final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3);
        timelineUp.getKeyFrames().add(kfUp);
    }



    /**
     * Method to toggle the search pane visibility.
     */
    private void togglePaneVisibility(){
        if(isExpanded.get()){
            isExpanded.set(false);
        }else{
            isExpanded.set(true);
        }
    }


//    /**
//     * search in tree for specific tree item and return it
//     * @param item
//     * @param name
//     * @return
//     */
//    private void searchAndUpdateTreeItem(TreeItem<String> item, String name, List<TreeItem<String>> treeItemSelection, TreeView tree) {
//
//        if(item.getValue().startsWith(name)){
//            //treeItemSelection.add(item);
//            item.setExpanded(true);
//            // This line is the not-so-clearly documented magic.
//            int row = tree.getRow( item );
//
//            // Now the row can be selected.
//            tree.getSelectionModel().select( row );
//            //return item; // hit!
//        }
//
//        // continue on the children:
//        TreeItem<String> result = null;
//        for(TreeItem<String> child : item.getChildren()){
//            searchAndUpdateTreeItem(child, name, treeItemSelection, tree);
//            //item.setExpanded(true);
//            /*if(result != null)
//                treeItemSelection.add(item);
//                //return result; // hit!
//                */
//        }
//
//        //no hit:
//        //item.getParent().getChildren().removeAll();
//
//    }


    /**
     * get all sub-groups of haplo
     * @param haplo_list
     */
    private void getAllSubgroups(String[] haplo_list){
        List<String> haplo_list_extended = new ArrayList<String>();
        for(String haplo : haplo_list){
            getSingleSubgroups(haplo);
        }

    }

    private List<String> getSingleSubgroups(String haplo){
        List<String> haplo_list = new ArrayList<String>();



        return haplo_list;


    }


}