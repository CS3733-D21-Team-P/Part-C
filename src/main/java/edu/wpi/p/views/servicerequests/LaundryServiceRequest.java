package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.*;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.views.Toolbar;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class LaundryServiceRequest extends GenericServiceRequest {

    public JFXTextField fullName;
    public JFXTextField pickUpLocation;
    public JFXDatePicker pickUpDate;
    public JFXDatePicker dropOffDate;
    public JFXTimePicker pickupTime;
    public JFXTimePicker dropoffTime;
    public JFXRadioButton colorsBtn;
    public JFXRadioButton darksBtn;
    public JFXRadioButton delicatesBtn;
    public JFXRadioButton bulkyBtn;
    public JFXTextArea specialInstructions;
    public JFXButton back;
    public JFXButton submit;

    public static String[] fields = {"Full Name", "Pickup Location", "Pickup Date", "Pickup Time", "Dropoff Date", "Dropoff Time", "Service", "Special Instructions"};

    public LaundryServiceRequest() {
        super();
        super.name = "Laundry Service Request";
    }

    @FXML
    public void initialize() {
        super.locationProperty = pickUpLocation.textProperty();
        StringProperty serviceProperty = createJFXRadioButtonStringProperty(colorsBtn, darksBtn, delicatesBtn, bulkyBtn);
        this.values.put("Full Name", fullName.textProperty());
        this.values.put("Pickup Location", pickUpLocation.textProperty());
        this.values.put("Pickup Date", pickUpDate.valueProperty());
        this.values.put("Pickup Time", pickupTime.valueProperty());
        this.values.put("Dropoff Date", dropOffDate.valueProperty());
        this.values.put("Dropoff Time", dropoffTime.valueProperty());
        this.values.put("Service", serviceProperty);
        this.values.put("Special Instructions", specialInstructions.textProperty());
    }

    @FXML
    private void advanceScene(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void submitPress(ActionEvent e) {
        super.submitPressed(e);
//        final String fullname = fullName.getText();
//        final String location = pickUpLocation.getText();
//        final String specialInstruction = specialInstructions.getText();
//
//
//        ServiceRequest sR = new ServiceRequest(fullname, location, fullname + "_" + location, "Laundry");
//        DBServiceRequest dbServiceRequest = DBServiceRequest.getInstance();
//        dbServiceRequest.addServiceRequest(sR);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void pathButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/PathfindingMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void editButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/EditMap.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void serviceButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void covidButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("..."));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
