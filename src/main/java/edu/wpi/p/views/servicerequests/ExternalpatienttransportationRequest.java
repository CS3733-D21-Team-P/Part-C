package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.p.App;

import java.io.IOException;
import java.time.LocalDate;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class ExternalpatienttransportationRequest extends GenericServiceRequest {

    @FXML
    public Label patienNameLabel;
    @FXML
    public Label currentHospitalLabel;
    @FXML
    public Label titleLabel;
    @FXML
    public Label currentRoomNumberLabel;
    @FXML
    public ComboBox currentHospital;
    @FXML
    public TextField currentRoomNumText;
    @FXML
    public ComboBox endHospital;
    @FXML
    public Label endHospitalLabel;
    @FXML
    public Label endRoomNumberLabel;
    @FXML
    public TextField endRoomNumText;
    @FXML
    public Label vehicleLabel;
    @FXML
    public CheckBox ambulance;
    @FXML
    public CheckBox helicopter;
    @FXML
    public CheckBox plane;
    @FXML
    public Label timeLabel;
    @FXML
    public TextField monthText;
    @FXML
    public Label slashLabel;
    @FXML
    public TextField dayText;
    @FXML
    public TextField yearText;
    @FXML
    public TextField hourText;
    @FXML
    public Label colonLabel;
    @FXML
    public TextField minuteText;
    @FXML
    public TextField firstNameText;
    @FXML
    public TextField lastNameText;
    @FXML
    public TextField detailText;
    @FXML
    public Label detailLabel;
    @FXML
    public Label slashLabel1;
    @FXML
    public ToggleGroup Vehicle;
    @FXML
    public JFXRadioButton AmbulanceBtn;
    @FXML
    public JFXRadioButton HelicopterBtn;
    @FXML
    public JFXRadioButton PlaneBtn;
    @FXML
    public javafx.scene.control.DatePicker DatePicker;
    @FXML
    public TextField doctorSignature;


    ObservableList<String> hospitalList = FXCollections
            .observableArrayList("A hospital", "B hospital", "C  hospital");


    public ExternalpatienttransportationRequest() {
        super();
        this.name = "External Patient Transport";

    }

    @FXML
    public void initialize() {
        currentHospital.setValue("A hospital");
        currentHospital.setItems(hospitalList);
        endHospital.setValue("A hospital");
        endHospital.setItems(hospitalList);

        StringProperty vehicleProperty = createJFXRadioButtonStringProperty(AmbulanceBtn, HelicopterBtn, PlaneBtn);
        this.locationProperty = currentRoomNumText.textProperty();
        this.values.put("First Name", firstNameText.textProperty());
        this.values.put("Last Name", lastNameText.textProperty());
        this.values.put("Room Number", currentRoomNumText.textProperty());
        this.values.put("End Room Number", endRoomNumText.textProperty());
        this.values.put("Vehicle", vehicleProperty);
        this.values.put("Date", DatePicker.valueProperty());
        this.values.put("Detail", detailText.textProperty());
        this.values.put("Doctor Signature", doctorSignature.textProperty());
    }


    @Override
    public void cancelPressed(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void submitPressed(ActionEvent actionEvent) {
        super.submitPressed(actionEvent);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void detailHelpPressed(ActionEvent actionEvent) {
    }

    public void roomHelpPressed(ActionEvent actionEvent) {
    }

    public void nameHelpPressed(ActionEvent actionEvent) {
    }


}
