package edu.wpi.p.views;

import edu.wpi.p.App;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SanitationServiceRequest {

    @FXML
    public Label title;
    @FXML
    public Label fullName;
    @FXML
    public Label roomNumber;
    @FXML
    public Label typeOfService;
    @FXML
    public Label description;
    @FXML
    public ChoiceBox typeOfServiceBox;
    @FXML
    public TextField fullNameText;
    @FXML
    public TextField roomNumberText;
    @FXML
    public TextField descriptionText;


    public void cancelPressed(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void submitPressed(ActionEvent actionEvent) {
        final String fullNameValue = fullNameText.getText();
        final String roomNumberValue = roomNumberText.getText();
        final String descriptionValue = description.getText();
    }
}
