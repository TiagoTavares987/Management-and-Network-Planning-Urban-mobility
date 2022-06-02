package Fx.lists;

import Fx.Main;
import core.entities.Localization;
import core.entities.Poi;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class PoisController {
    public TableView<Poi> poisTable;
    public TableColumn poiIdCol;
    public TableColumn poiNameCol;
    public TableColumn poiDescriptionCol;
    public TableColumn poiLatCol;
    public TableColumn poiLonCol;
    public TextField poiNameField;
    public TextField poiDescriptionField;
    public Spinner poiLatField;
    public Spinner poiLonField;

    public void onPoiAdd(ActionEvent actionEvent) {
        String name = poiNameField.getText();
        String descricao = poiDescriptionField.getText();
        Localization latitude = (Localization) poiLatField.getValue();
        Localization longitude = (Localization) poiLonField.getValue();


       //Main.poiManager.newPoi(name, descricao, );

        updateTable();
    }

    public void onPoiRemove(ActionEvent actionEvent) throws Exception {
        Poi poi = poisTable.getSelectionModel().getSelectedItem();
        if(poi == null)
            return;

        Main.poiManager.deletePoi(poi.getId(), true);
        updateTable();
    }

    public void onPoiEdit(ActionEvent actionEvent) {
        Poi poi = poisTable.getSelectionModel().getSelectedItem();
        String name = poiNameField.getText();
        String descricao = poiDescriptionField.getText();

        //Main.poiManager.editPoi(poi.getId(), name, descricao, );
        updateTable();
    }

    public void updateTable(){
        poisTable.getItems().clear();
        ArrayList<Poi> list = Main.poiManager.GetAll();
        poisTable.getItems().addAll(list);
    }
}
