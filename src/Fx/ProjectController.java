package Fx;

import core.baseEntities.Const;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class ProjectController {

    public Pane MainContent;
    public MenuItem Login;
    public MenuItem Logout;
    public Label CurrentUser;

    @FXML
    protected void dummy(){
        //ProjectApp.Show(ProjectApp.users);
        //ProjectApp.Show(ProjectApp.pois);
        //ProjectApp.Show(ProjectApp.tags);
        //ProjectApp.Show(ProjectApp.nodes);
        //ProjectApp.Show(ProjectApp.ways);
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.login);
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
    }

    @FXML
    public void loadText(ActionEvent actionEvent) throws IOException, ParseException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");
        directoryChooser.setInitialDirectory(new File(Const.inputPath)); // abrir no diretorio input
        File dir = directoryChooser.showDialog(null);

        Login.setDisable(false);
        Main.loadText(dir.getPath()); // le o ficheiro de texto caminho onde esta a pasta
        ProjectApp.Update();
    }

    @FXML
    public void loadBin(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");
        directoryChooser.setInitialDirectory(new File(Const.inputPath));
        File dir = directoryChooser.showDialog(null);

        Login.setDisable(false);
        Main.loadBin(dir.getPath());
        ProjectApp.Update();
    }

    @FXML
    public void saveText(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save folder");
        directoryChooser.setInitialDirectory(new File(Const.outputPath));

        Login.setDisable(true);
        Main.now();
        ProjectApp.Update();
    }

    @FXML
    public void saveBin(ActionEvent actionEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save folder");
        directoryChooser.setInitialDirectory(new File(Const.outputPath));

        Login.setDisable(true);
        Main.binNow();
        ProjectApp.Update();
    }

    @FXML
    void onAboutMenu(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Feito por Diana Marques e Tiago Tavares", ButtonType.OK);
        alert.setHeaderText("About");
        alert.setTitle("About");
        alert.showAndWait();
    }

    @FXML
    void onExitMenu(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    // mostra o conteudo do user (tabela)
    public void showUsers(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.users);
    }
    public void showPois(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.pois);
    }
    public void showTags(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.tags);
    }
    public void showNodes(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.nodes);
    }
    public void showWays(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.ways);
    }
    public void showGraph(ActionEvent actionEvent)  {
        ProjectApp.Show(ProjectApp.graph);
    }
}
