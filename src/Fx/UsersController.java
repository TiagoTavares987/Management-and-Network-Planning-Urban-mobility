package Fx;

import business.managers.UserManager;
import core.entities.User;
import core.entities.UserPoi;
import core.enums.UserType;
import edu.princeton.cs.algs4.ST;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class UsersController {

    public TableView<User> usersTable;
    public TableColumn userIdCol;
    public TableColumn userNameCol;
    public TableColumn userTypeCol;
    public TextField userNameField;
    public ComboBox userTypeCombo;
    public String basic;
    public String admin;

    public void startClick(ActionEvent actionEvent) {
    }

    public void onUserAdd(ActionEvent actionEvent) {
        String name = userNameField.getText();
        String type = (String) userTypeCombo.getSelectionModel().getSelectedItem();

        Main.userManager.newUser(name, UserType.valueOf(type));
        updateTable();
    }

    public void onUserRemove(ActionEvent actionEvent) {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null)
            return;

        Main.userManager.DeleteUser(user.getId());
        ST<Integer, UserPoi> pois = user.getPois();
        for (Integer poi : pois)
            Main.userManager.deletePoi(user.getId(), poi);

        updateTable();
    }

    public void onUserEdit(ActionEvent actionEvent) throws Exception {
        User user = usersTable.getSelectionModel().getSelectedItem();
        String name = userNameField.getText();
        String type = (String) userTypeCombo.getSelectionModel().getSelectedItem();

        Main.userManager.editUser(user.getId(), name, user.Password, UserType.valueOf(type));
        updateTable();
    }

    public void updateTable(){
        usersTable.getItems().clear();
        ArrayList<User> list = Main.userManager.GetAll();
        usersTable.getItems().addAll(list);
    }


}
