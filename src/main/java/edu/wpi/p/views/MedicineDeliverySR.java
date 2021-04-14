package edu.wpi.p.views;

import edu.wpi.p.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MedicineDeliverySR {
    @FXML
    private TextField medicineName;
    @FXML
    private TextField medicineAmount;
    @FXML
    private TextField locationArea;
    @FXML
    private TextField medicineReason;
    @FXML
    private TextField medicineInfo;
    @FXML
    private TextField doctorSign;
    @FXML
    private CheckBox pat;
    @FXML
    private CheckBox doc;
    @FXML
    private CheckBox nurse;

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
    }
}
