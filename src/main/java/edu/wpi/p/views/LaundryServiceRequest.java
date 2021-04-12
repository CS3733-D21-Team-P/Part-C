package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
    public JFXTextField timeHourP;
    public JFXTextField timeMinuteP;
    public JFXTextField timeHourD;
    public JFXTextField timeMinuteD;
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
