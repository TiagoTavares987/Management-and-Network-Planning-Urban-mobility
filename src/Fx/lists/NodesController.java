package Fx.lists;

import Fx.Main;
import core.entities.Localization;
import core.entities.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class NodesController {
    public TableView<Node> nodesTable;
    public TableColumn nodeIdCol;
    public TableColumn nodeNameCol;
    public TableColumn nodeLatCol;
    public TableColumn nodeLonCol;
    public TextField nodeNameField;
    public Spinner nodeLatField;
    public Spinner nodeLonField;

    public void onNodeAdd(ActionEvent actionEvent) {
        String name = nodeNameField.getText();
        Localization latitude = (Localization) nodeLatField.getValue();
        Localization longitude = (Localization) nodeLonField.getValue();

        //Main.nodeManager.newNode(name, );
        updateTable();
    }

    public void onNodeRemove(ActionEvent actionEvent) throws Exception {
        Node node = nodesTable.getSelectionModel().getSelectedItem();
        if(node == null)
            return;

        Main.nodeManager.deleteNode(node.getId(), false);
        updateTable();
    }

    public void onNodeEdit(ActionEvent actionEvent) {
        Node node = nodesTable.getSelectionModel().getSelectedItem();
        String name = nodeNameField.getText();
        Localization latitude = (Localization) nodeLatField.getValue();
        Localization longitude = (Localization) nodeLonField.getValue();

        //Main.nodeManager.editNode(node.getId(), name, );
        updateTable();
    }

    public void updateTable(){
        nodesTable.getItems().clear();
        ArrayList<Node> list = Main.nodeManager.GetAll();
        nodesTable.getItems().addAll(list);
    }
}
