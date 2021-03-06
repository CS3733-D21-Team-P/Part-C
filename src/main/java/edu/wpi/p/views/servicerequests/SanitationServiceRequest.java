package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.p.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SanitationServiceRequest extends GenericServiceRequest implements Initializable {

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

    public static String[] fields = {"Full Name", "Room Number", "Type of Sanitation", "Additional Information"};

  public SanitationServiceRequest() {
    super();
    super.name = "Sanitation Request";
  }

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

        super.locationProperty = roomNumberText.textProperty();
        StringProperty typeString = new SimpleStringProperty("");
        typeOfSanitationBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            typeString.setValue(newValue.getText());
        });
        this.values.put("Full Name", fullNameText.textProperty());
        this.values.put("Room Number", roomNumberText.textProperty());
        this.values.put("Type of Sanitation", typeString);
        this.values.put("Additional Information", additionalInfoText.textProperty());

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
//        final String fullNameValue = fullNameText.getText();
//        final String roomNumberValue = roomNumberText.getText();
//        final String descriptionValue = description.getText();
//        final String typeOfSanitation = typeOfSanitationBox.getTypeSelector();
//        ServiceRequest sR = new ServiceRequest(fullNameValue, roomNumberValue, fullNameValue + "_" + roomNumberValue, "Sanitation");
//        DBServiceRequest dbServiceRequest = DBServiceRequest.getInstance();
//        dbServiceRequest.addServiceRequest(sR);
        super.submitPressed(actionEvent);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void homeButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void pathButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/PathfindingMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void editButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/EditMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void serviceButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void covidButtonAc(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("..."));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
