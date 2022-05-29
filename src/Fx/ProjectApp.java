package Fx;

import core.entities.User;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProjectApp extends Application {
    private static ObservableList<Node> mainContentChildren;


    private static UsersController usersController;


    public static Scene users;

    @Override
    public void start(Stage stage) throws Exception {
        Main.main(null);

        double width = 320;
        double height = 240;

        FXMLLoader loader = new FXMLLoader(ProjectApp.class.getResource("project.fxml"));
        Scene scene = new Scene(loader.load(), width, height);

        loader = new FXMLLoader(ProjectApp.class.getResource("users.fxml"));
        users = new Scene(loader.load(), width, height);
        usersController = loader.getController();

        stage.setTitle("Projeto LP2 e AED2");
        stage.setScene(scene);
        stage.show();
        mainContentChildren = ((Pane)scene.lookup("#MainContent")).getChildren();




        User user = new User();
        user.Username = "qwertyui";
        user.Password = "qwertyui";
        user = Main.userManager.SaveUser(user);

        ArrayList<User> list = Main.userManager.GetAll();


        usersController.updateTable();


    }

    public static void main(String[] args) {
        launch();
    }

    //public static void Show(Scene scene) throws IOException {
    //    double with = mainStage.getWidth();
    //    double height = mainStage.getHeight();

    //    mainStage.setScene(scene);

    //    mainStage.setWidth(with);
    //    mainStage.setHeight(height);
    //    mainStage.show();
    //}

    public static void Show(Scene scene){
        mainContentChildren.clear();
        mainContentChildren.addAll(scene.getRoot());
    }
}
