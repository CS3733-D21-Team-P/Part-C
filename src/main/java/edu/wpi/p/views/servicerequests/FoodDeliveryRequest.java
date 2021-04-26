package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FoodDeliveryRequest extends Toolbar {
    @FXML
    public JFXTextField name;
    @FXML
    public JFXTextField currRoom;
    @FXML
    public JFXCheckBox regular;
    @FXML
    public JFXCheckBox vegetarian;
    @FXML
    public JFXCheckBox vegan;
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
