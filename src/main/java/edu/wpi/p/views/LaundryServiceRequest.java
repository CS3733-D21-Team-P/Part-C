package edu.wpi.p.views;

import com.jfoenix.controls.*;
import edu.wpi.p.App;
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
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
