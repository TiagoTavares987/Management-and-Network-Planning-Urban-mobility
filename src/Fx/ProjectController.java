package Fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ProjectController {

    public Pane MainContent;
    public AnchorPane Menu;

    @FXML
    protected void dummy(){
        //box.getChildren().addAll(new Label("zesxrdctfvygb"));
        ProjectApp.Show(ProjectApp.users);

        //Pane asd = new Pane();
        //asd.getChildren().add();
    }

    @FXML
    protected void startClick() throws IOException {
        //ProjectApp.Show(ProjectApp.start);
        ProjectApp.Show(ProjectApp.users);
    }
    @FXML
    protected void abcClick() throws IOException {
        //ProjectApp.Show(ProjectApp.abc);
        ProjectApp.Show(ProjectApp.users);
    }

    public void showUsers(ActionEvent actionEvent) {
        ProjectApp.Show(ProjectApp.users);
    }

    /*void onOpenText(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            PolicyUtils.IO.(selectedFile.getAbsolutePath());
        }
        updateGraphGroup();
        updateCachesTable();
        updateUsersTable();
    }

    @FXML
    void onOpenBin(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Binary File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            PolicyUtils.IO.readBinFile(selectedFile.getAbsolutePath());
        }
        updateGraphGroup();
        updateCachesTable();
        updateUsersTable();
    }

    @FXML
    void onSaveText(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile != null) {
            (selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void onSaveBin(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Binary File");
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile != null) {
           PolicyUtils.IO.writeBinFile(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void onExitMenu(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }*/
}
