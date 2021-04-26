package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.*;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FloralDeliveryRequest extends Toolbar {
    @FXML
    public JFXTextField name;
    @FXML
    public JFXTextField currRoom;
    @FXML
    public JFXCheckBox roses;
    @FXML
    public JFXCheckBox tulips;
    @FXML
    public JFXCheckBox painBush;
    @FXML
    public JFXCheckBox small;
    @FXML
    public JFXCheckBox medium;
    @FXML
    public JFXCheckBox large;
    @FXML
    public JFXColorPicker color;
    @FXML
    public JFXTextArea message;
    @FXML
    public JFXButton back;
    @FXML
    public JFXButton submit;

    @FXML
    private void submitForm(ActionEvent e) {
        final String doctor = name.getText();
        final String location = currRoom.getText();


        ServiceRequest sR = new ServiceRequest(doctor, location, "Name" + "_" + location, "Floral Delivery Request");
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
