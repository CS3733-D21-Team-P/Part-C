package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.p.App;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Observable;
import java.util.ResourceBundle;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SanitationServiceRequest implements Initializable {

    @FXML
    public Label title;
    @FXML
    public Label fullName;
    @FXML
    public Label roomNumber;
    @FXML
    public Label typeOfSanitation;
    @FXML
    public Label description;
    @FXML
    public JFXTextField fullNameText;
    @FXML
    public JFXTextField roomNumberText;
    @FXML
    public JFXButton submitButton;
    @FXML
    public JFXButton helpButton;
    @FXML
    public JFXButton cancelButton;
    @FXML
    public JFXTextField additionalInfoText;
    @FXML
    public JFXComboBox<Label> typeOfSanitationBox;
    public void initialize(URL url, ResourceBundle rb)
    {
        typeOfSanitationBox.getItems().add(new Label("Standard Room Clean"));
        typeOfSanitationBox.getItems().add(new Label("Heavy Room Clean"));
        typeOfSanitationBox.getItems().add(new Label("Trash Disposal"));
        typeOfSanitationBox.getItems().add(new Label("General Janitorial"));

        RequiredFieldValidator validator = new RequiredFieldValidator();

        fullNameText.getValidators().add(validator);
        validator.setMessage("No Input Given");
        fullNameText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue)
                {
                    fullNameText.validate();
                }
            }
        });

        roomNumberText.getValidators().add(validator);
        validator.setMessage("No Input Given");
        roomNumberText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue)
                {
                    roomNumberText.validate();
                }
            }
        });
    }

    public void cancelPressed(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void submitPressed(ActionEvent actionEvent) {
        final String fullNameValue = fullNameText.getText();
        final String roomNumberValue = roomNumberText.getText();
        final String descriptionValue = description.getText();
        final String typeOfSanitation = typeOfSanitationBox.getTypeSelector();
        ServiceRequest sR = new ServiceRequest(fullNameValue, roomNumberValue, fullNameValue + "_" + roomNumberValue, "Sanitation");
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.addServiceRequest(sR);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



}
