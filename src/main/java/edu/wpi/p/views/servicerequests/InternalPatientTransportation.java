package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class InternalPatientTransportation extends GenericServiceRequest {
    @FXML
    public JFXTextField drName;
    @FXML
    public JFXTextField currRoom;
    @FXML
    public JFXTextField patientName;
    @FXML
    public JFXTextField destination;
    @FXML
    public JFXTimePicker time;
    @FXML
    public JFXButton back;
    @FXML
    public JFXButton submit;

    public static String[] fields = {"Doctor Name", "Current Room", "Patient Name", "Desired Transport Time", "Desired Transport Location"};

    public InternalPatientTransportation() {
        super();
        super.name = "Internal Patient Transportation";
    }

    @FXML
    public void initialize() {
        this.locationProperty = currRoom.textProperty();

        this.values.put("Doctor Name", drName.textProperty());
        this.values.put("Current Room", currRoom.textProperty());
        this.values.put("Patient Name", patientName.textProperty());
        this.values.put("Desired Transport Time", time.valueProperty());
        this.values.put("Desired Transport Location", destination.textProperty());
    }
    @FXML
    private void submitForm(ActionEvent e) {
        super.submitPressed(e);
//        final String doctor = drName.getText();
//        final String location = currRoom.getText();
//        final String patient = patientName.getText();
//        final String destinationText = destination.getText();
//        final String pickUpTime = time.toString();
//
//
//
//        ServiceRequest sR = new ServiceRequest(doctor, location, "Name" + "_" + location, "Internal Patient Transportation");
//        DBServiceRequest dbServiceRequest = DBServiceRequest.getInstance();
//        dbServiceRequest.addServiceRequest(sR);



        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
