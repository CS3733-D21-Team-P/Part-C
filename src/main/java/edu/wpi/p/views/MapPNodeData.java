package edu.wpi.p.views;

import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.io.IOException;

public class MapPNodeData {

    @FXML
    private Button addNodeBtn;
    @FXML
    private Button editNodeBtn;
    @FXML
    private Button deleteNodeBtn;
    @FXML
    private TableView nodeDataTableView;
    @FXML
    private Button homeButton;
    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addNodeAc(ActionEvent actionEvent) {
    }

    public void editNodeAc(ActionEvent actionEvent) {
    }

    public void deleteNodeAc(ActionEvent actionEvent) {
    }
}
