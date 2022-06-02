package Fx.file;

import Fx.ProjectApp;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    public TextField userNameField;
    public PasswordField passwordField;

    public void login(ActionEvent actionEvent) throws NoSuchAlgorithmException {

        ProjectApp.Login(userNameField.getText(), passwordField.getText());
    }
}
