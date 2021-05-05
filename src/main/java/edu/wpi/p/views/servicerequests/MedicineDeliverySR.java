package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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

public class MedicineDeliverySR extends GenericServiceRequest {
    @FXML
    private JFXTextField medicineName;
    @FXML
    private JFXTextField medicineAmount;
    @FXML
    private JFXTextField locationArea;
    @FXML
    private JFXTextArea medicineReason;
    @FXML
    private JFXTextArea medicineInfo;
    @FXML
    private JFXTextField doctorSign;
    @FXML
    private JFXRadioButton pat;
    @FXML
    private JFXRadioButton doc;
    @FXML
    private JFXRadioButton nurse;

    public static String[] fields = {"Prescriber", "Medicine", "Amount", "Location", "Reason", "Additional Details", "Doctor Signature"};

    public MedicineDeliverySR() {
        super();
        super.name = "Medicine Delivery Request";
    }

    @FXML
    public void initialize() {
        super.locationProperty = locationArea.textProperty();
        StringProperty prescriberString = createJFXRadioButtonStringProperty(pat, doc, nurse);
        this.values.put("Prescriber", prescriberString);
        this.values.put("Medicine", medicineName.textProperty());
        this.values.put("Amount", medicineAmount.textProperty());
        this.values.put("Location", locationArea.textProperty());
        this.values.put("Reason", medicineReason.textProperty());
        this.values.put("Additional Details", medicineInfo.textProperty());
        this.values.put("Doctor Signature", doctorSign.textProperty());


    }
    @FXML
    private void homePageButton(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
