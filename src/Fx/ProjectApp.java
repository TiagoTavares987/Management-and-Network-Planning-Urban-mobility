package Fx;

import Fx.file.LoginController;
import Fx.lists.*;
import Fx.tsp.GraphController;
import business.managers.UserManager;
import business.utils.Utils;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;

public class ProjectApp extends Application {
    private static ObservableList<Node> mainContentChildren;

    private static ProjectController projectController;
    private static UsersController usersController;
    private static PoisController poisController;
    private static TagsController tagsController;
    private static NodesController nodesController;
    private static WaysController waysController;
    private static GraphController graphController;

    // resize na janela
    private ChangeListener<Number> resize = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            graphController.updateGraph();
        }
    };

    public static Scene main;

    public static Scene login;
    public static Scene users;

    public static Scene pois;
    public static Scene tags;
    public static Scene nodes;
    public static Scene ways;
    public static Scene graph;

    @Override
    public void start(Stage stage) throws Exception {
        Main.main(null);

        double width = 320;
        double height = 240;

        FXMLLoader loader = new FXMLLoader(ProjectApp.class.getResource("project.fxml"));
        main = new Scene(loader.load(), width, height);
        projectController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("file/login.fxml"));
        login = new Scene(loader.load(), width, height);

        loader = new FXMLLoader(ProjectApp.class.getResource("lists/users.fxml"));
        users = new Scene(loader.load(), width, height);
        usersController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("lists/pois.fxml"));
        pois = new Scene(loader.load(), width, height);
        poisController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("lists/tags.fxml"));
        tags = new Scene(loader.load(), width, height);
        tagsController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("lists/nodes.fxml"));
        nodes = new Scene(loader.load(), width, height);
        nodesController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("lists/ways.fxml"));
        ways = new Scene(loader.load(), width, height);
        waysController = loader.getController();

        loader = new FXMLLoader(ProjectApp.class.getResource("tsp/graph.fxml"));
        graph = new Scene(loader.load(), width, height);
        graphController = loader.getController();

        stage.setTitle("Projeto LP2 e AED2");
        stage.setScene(main);
        stage.show();

        // dar resize à tela
        Pane mainContent = (Pane) main.lookup("#MainContent");
        mainContentChildren = mainContent.getChildren();
        mainContent.widthProperty().addListener(resize);
        mainContent.heightProperty().addListener(resize);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void Show(Scene scene){
        mainContentChildren.clear();
        mainContentChildren.addAll(scene.getRoot());
    }

    public static void Login(String username, String password) throws NoSuchAlgorithmException {

        if(Main.userManager.ValidateLogin(username, Utils.getHash(password))){
            Show(graph);
            projectController.CurrentUser.setText(UserManager.getCurrentUser().Username);
            projectController.Login.setVisible(false);
            projectController.Logout.setVisible(true);
        }
    }

    public static void Update(){
        usersController.updateTable();
        poisController.updateTable();
        tagsController.updateTable();
        nodesController.updateTable();
        waysController.updateTable();
        graphController.updateGraph();
        Show(graph);
    }
}
