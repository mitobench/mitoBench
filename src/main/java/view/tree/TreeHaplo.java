package view.tree;

import javafx.application.Platform;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Created by neukamm on 09.11.16.
 */
public class TreeHaplo{

    private TreeItem<String> rootItem;
    private TreeView<String> tree;

    public TreeHaplo(String root){

        rootItem = new TreeItem<String> (root);
        rootItem.setExpanded(true);

    }

    public void addStructure(){
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i);
            rootItem.getChildren().add(item);
        }

        for (int i = 1; i < 3; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message_child" + i);
            rootItem.getChildren().get(0).getChildren().add(item);
        }

        for (int i = 1; i < 3; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message_child" + i);
            rootItem.getChildren().get(3).getChildren().add(item);
        }

        tree = new TreeView<String> (rootItem);
        tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Krass: Selection wird gelöscht, wenn auf falsches Item (Nicht leaf) geklickt wird
        // todo: make it more user-friedly if possible
        tree.getSelectionModel().selectedItemProperty().addListener((c, oldValue, newValue) -> {
            if (newValue != null && !newValue.isLeaf()) {
                Platform.runLater(() -> tree.getSelectionModel().clearSelection());
            }
        });
    }

    public TreeItem<String> getRootItem() {
        return rootItem;
    }

    public TreeView<String> getTree() {
        return tree;
    }
}
