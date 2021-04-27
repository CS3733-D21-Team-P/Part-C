package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MedicineDeliverySR extends Toolbar {
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
    private JFXCheckBox pat;
    @FXML
    private JFXCheckBox doc;
    @FXML
    private JFXCheckBox nurse;

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
    public void doctorClick(ActionEvent e){
        //doc is the one writing the form

    }
    @FXML
    public void nurseClick(ActionEvent e){
        //nurse is the one writing the form

    }
    @FXML
    public void patientClick(ActionEvent e){
        //patient is the one writing the form

    }
    @FXML
    private void submitForm(ActionEvent e) {
        final String medName = medicineName.getText();
        final String medAmt = medicineAmount.getText();
        final String location = locationArea.getText();
        final String medReason = medicineReason.getText();
        final String medInfo = medicineInfo.getText();
        final String docSign = doctorSign.getText();



        ServiceRequest sR = new ServiceRequest(docSign, location, "Name" + "_" + location, "Medicine Delivery");
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.addServiceRequest(sR);



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
