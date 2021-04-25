package edu.wpi.p.views.servicerequests;

import edu.wpi.p.App;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Observable;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class ExternalpatienttransportationRequest {




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
  public RadioButton AmbulanceBtn;
  @FXML
  public RadioButton HelicopterBtn;
  @FXML
  public RadioButton PlaneBtn;
  @FXML
  public javafx.scene.control.DatePicker DatePicker;
  @FXML
  public TextField doctorSignature;


  ObservableList<String> hospitalList = FXCollections
          .observableArrayList("A hospital", "B hospital", "C  hospital");


  @FXML
  private void initialize(){
    currentHospital.setValue("A hospital");
    currentHospital.setItems(hospitalList);
    endHospital.setValue("A hospital");
    endHospital.setItems(hospitalList);
  }



  public void cancelPressed(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void submitPressed(ActionEvent actionEvent) {
    final String firstName = firstNameText.getText();
    final String lastName = lastNameText.getText();
    final String roomNumber = currentRoomNumText.getText();
    final String endRoomNumber = endRoomNumText.getText();
    final Object vehicle = Vehicle.getSelectedToggle().toString();
    final LocalDate datePicker = DatePicker.getValue();
    final String hour = hourText.getText();
    final String min = minuteText.getText();
    final String detail = detailText.getText();
    final String doctorSig = doctorSignature.getText();
    ServiceRequest sR = new ServiceRequest(doctorSig, roomNumber, firstName+lastName + "_" + roomNumber, "External patient transportation");
    DBServiceRequest dbServiceRequest = new DBServiceRequest();
    dbServiceRequest.addServiceRequest(sR);

    System.out.println("Patien Name = " + firstName + lastName
            + "\nCurrent Hospital: " + currentHospital.getSelectionModel().getSelectedItem()
            + "\nRoom Number: " + roomNumber
            + "\nEnd Hospital: " + endHospital.getSelectionModel().getSelectedItem()
            + "\nRoom Number: " + endRoomNumber
            + "\nVehicle: " + vehicle
            + "\nDate: " + datePicker
            + "\nTime: " + hour + ":" + min
            + "\nDetail: " + detail

    );

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
