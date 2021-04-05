package edu.wpi.p.views;

import edu.wpi.p.App;
import java.io.IOException;
import java.util.Observable;

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
  public ChoiceBox currentHospital;
  @FXML
  public TextField currentRoomNumText;
  @FXML
  public ChoiceBox endHospital;
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


  ObservableList<String> hospitalList = FXCollections
          .observableArrayList("A hospital", "B hospital", "C  hospital");


  @FXML
  private void initialize(){
    currentHospital.setValue("A hospital");
    currentHospital.setItems(hospitalList);
    endHospital.setValue("A hospital");
    endHospital.setItems(hospitalList);
  }

  @FXML
  private RadioButton AmbulanceBtn;
  @FXML
  private RadioButton HelicopterBtn;
  @FXML
  private RadioButton PlaneBtn;


  public void cancelPressed(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/LanguageInterpreterServiceRequest.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void submitPressed(ActionEvent actionEvent) {
    final String fristName = firstNameText.getText();
    final String lastName = lastNameText.getText();
    final String roomNumber = currentRoomNumText.getText();
    final String endRoomNumber = endRoomNumText.getText();


    System.out.println("Patien Name = " + fristName + lastName
            + "\nCurrent Hospital: " + currentHospital.getSelectionModel().getSelectedItem()
            + "\nRoom Number: " + roomNumber
            + "\nEnd Hospital: " + endHospital.getSelectionModel().getSelectedItem()
            + "\nRoom Number: " + endRoomNumber



    );


  }

  public void detailHelpPressed(ActionEvent actionEvent) {
  }

  public void roomHelpPressed(ActionEvent actionEvent) {
  }

  public void nameHelpPressed(ActionEvent actionEvent) {
  }



}
