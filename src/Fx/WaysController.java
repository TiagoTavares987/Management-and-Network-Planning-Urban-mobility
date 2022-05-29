package Fx;

import core.entities.User;
import core.entities.UserPoi;
import core.entities.Way;
import core.enums.UserType;
import edu.princeton.cs.algs4.ST;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class WaysController {
    public TableView<Way> waysTable;
    public TableColumn wayIdCol;
    public TableColumn wayNameCol;
    public TableColumn wayStartCol;
    public TableColumn wayEndCol;
    public TextField wayNameField;
    public TextField wayStartField;
    public TextField wayEndField;

    public void onWayAdd(ActionEvent actionEvent) throws Exception {
        String name = wayNameField.getText();
        Integer start = Integer.valueOf(wayStartField.getText());
        Integer end = Integer.valueOf(wayStartField.getText());

        Main.wayManager.newWay(name, start, end);
        updateTable();
    }

    public void onWayRemove(ActionEvent actionEvent) throws Exception {
        Way way = waysTable.getSelectionModel().getSelectedItem();
        if(way == null)
            return;

        Main.wayManager.deleteWay(way.getId(), true);
        updateTable();
    }

    public void onWayEdit(ActionEvent actionEvent) throws Exception {
        Way way = waysTable.getSelectionModel().getSelectedItem();
        String name = wayNameField.getText();
        Integer start = Integer.valueOf(wayStartField.getText());
        Integer end = Integer.valueOf(wayStartField.getText());

        Main.wayManager.editWay(way.getId(), name, start, end);
        updateTable();
    }

    public void updateTable(){
        waysTable.getItems().clear();
        ArrayList<Way> list = Main.wayManager.GetAll();
        waysTable.getItems().addAll(list);
    }
}
