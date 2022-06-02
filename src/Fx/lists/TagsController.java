package Fx.lists;

import Fx.Main;
import core.entities.Tag;
import core.entities.User;
import core.entities.Way;
import core.enums.UserType;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class TagsController {
    public TableView<Tag> tagsTable;
    public TableColumn tagIdCol;
    public TableColumn tagDescriptionCol;
    public TableColumn tagExtraInfoCol;
    public TextField tagDescriptionField;
    public TextField tagExtraInfoField;

    public void onTagAdd(ActionEvent actionEvent) throws Exception {
        String descricao = tagDescriptionField.getText();
        String extraInfo = tagExtraInfoField.getText();

        Main.tagManager.newTag(descricao);
        //Main.tagManager.addExtraInfo();
        updateTable();
    }

    public void onTagRemove(ActionEvent actionEvent) throws Exception {
        Tag tag = tagsTable.getSelectionModel().getSelectedItem();
        if(tag == null)
            return;

        Main.tagManager.deleteTag(tag.getId(), true);
        updateTable();
    }

    public void onTagEdit(ActionEvent actionEvent) throws Exception {
        Tag tag = tagsTable.getSelectionModel().getSelectedItem();
        String descricao = tagDescriptionField.getText();

        Main.tagManager.editTag(tag.getId(), descricao);
        updateTable();
    }
    public void updateTable(){
        tagsTable.getItems().clear();
        ArrayList<Tag> list = Main.tagManager.GetAll();
        tagsTable.getItems().addAll(list);
    }
}
