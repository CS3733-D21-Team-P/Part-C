package edu.wpi.p.views;

import com.jfoenix.controls.*;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LaundryServiceRequest {

    public JFXTextField fullName;
    public JFXTextField pickUpLocation;
    public JFXDatePicker pickUpDate;
    public JFXDatePicker dropOffDate;
    public JFXTimePicker pickupTime;
    public JFXTimePicker dropoffTime;
    public JFXTextArea specialInstructions;
    public JFXButton back;
    public JFXButton submit;

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

        final String fullname = fullName.getText();
        final String location = pickUpLocation.getText();
        final String specialInstruction = specialInstructions.getText();


        ServiceRequest sR = new ServiceRequest(fullname, location, fullname + "_" + location, "Laundry");
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
