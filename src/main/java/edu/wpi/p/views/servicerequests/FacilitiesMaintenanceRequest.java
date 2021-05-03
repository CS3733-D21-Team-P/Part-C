package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FacilitiesMaintenanceRequest extends GenericServiceRequest {

    public JFXTextField name;
    public JFXTextField loc;
    public JFXTextField facility;
    public JFXTextArea description;
    public JFXButton back;
    public JFXButton submit;

    public static final String[] fields = {"Name", "Facility", "Description"};

    @FXML
    public void initialize() {
        super.name = "Facilities Maintenance Request";
        this.locationProperty = loc.textProperty();
        this.values.put("Name", name.textProperty());
        this.values.put("Facility", facility.textProperty());
        this.values.put("Description", description.textProperty());
    }

    @FXML
    private void submitForm(ActionEvent e) {
       super.submitPressed(e);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
