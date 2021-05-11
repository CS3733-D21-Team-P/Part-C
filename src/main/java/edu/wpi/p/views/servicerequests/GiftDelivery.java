package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class GiftDelivery extends GenericServiceRequest{
    @FXML
    public JFXTextField name;
    @FXML
    public JFXTextField phoneNumber;
    @FXML
    public JFXTextField address;
    @FXML
    public JFXButton back;
    @FXML
    public JFXButton submit;
    @FXML
    public JFXTextField recipient;
    @FXML
    public JFXCheckBox giftBox;
    @FXML
    public JFXCheckBox giftRibbon;
    @FXML
    public JFXTextArea detailText;
    @FXML
    public JFXButton homeButton;
    @FXML
    public JFXButton pathButton;
    @FXML
    public JFXButton editButton;
    @FXML
    public JFXButton serviceButton;
    @FXML
    public JFXButton covidButton;

    public static String[] fields = {"Name", "Phone Number", "Delivery Address", "Recipient", "Gift Box", "Gift Ribbon", "Details"};


    public GiftDelivery() {
        super();
        super.name = "Gift Delivery";
    }


    @FXML
    public void initialize() {
        super.locationProperty = address.textProperty();
        StringProperty giftBoxString = new SimpleStringProperty("No");
        giftBox.armedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                giftBoxString.setValue("Yes");
            }
            else {
                giftBoxString.setValue("No");
            }
        });
        StringProperty giftRibbonString = new SimpleStringProperty("No");
        giftRibbon.armedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                giftRibbonString.setValue("Yes");
            }
            else {
                giftRibbonString.setValue("No");
            }
        });
        this.values.put("Name", name.textProperty());
        this.values.put("Phone Number", phoneNumber.textProperty());
        this.values.put("Delivery Address", address.textProperty());
        this.values.put("Recipient", recipient.textProperty());
        this.values.put("Gift Box", giftBoxString);
        this.values.put("Gift Ribbon", giftRibbonString);
        this.values.put("Details", detailText.textProperty());
    }


    public void advanceScene(ActionEvent actionEvent) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void submitForm(ActionEvent actionEvent) {
//        final String Name = name.getText();
//        final String Add = address.getText();
//        ServiceRequest sR = new ServiceRequest(Name, Add, Name + "_" + Add, "Gift Delivery");
//        DBServiceRequest dbServiceRequest = DBServiceRequest.getInstance();
//        dbServiceRequest.addServiceRequest(sR);
        super.submitPressed(actionEvent);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public void homeButtonAc(ActionEvent actionEvent){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//    public void pathButtonAc(ActionEvent actionEvent){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/PathfindingMap.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//    public void editButtonAc(ActionEvent actionEvent){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/EditMap.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//    public void serviceButtonAc(ActionEvent actionEvent){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//    public void covidButtonAc(ActionEvent actionEvent){
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("..."));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
}
