package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import java.io.IOException;

import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.views.Toolbar;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LanguageInterpreterServiceRequest extends GenericServiceRequest {
  @FXML
  public JFXTextField doctorSignature;
  @FXML
  private JFXTextField interpLanguage;
  @FXML
  private JFXTextField interpLoc;
  @FXML
  private JFXTextField interpDetails;
  @FXML
  private JFXButton submit;
  @FXML
  private JFXButton back;

  public static String[] fields = {"Language", "Location", "Extra Details", "Signature"};

  public LanguageInterpreterServiceRequest() {
    super();
    super.name = "Language Interpreter Request";
  }
  @FXML
  public void initialize() {
    super.locationProperty = interpLoc.textProperty();

    this.values.put("Language", interpLanguage.textProperty());
    this.values.put("Location", interpLoc.textProperty());
    this.values.put("Extra Details", interpDetails.textProperty());
    this.values.put("Signature", doctorSignature.textProperty());
  }
  @FXML
  private void advanceScene(ActionEvent e) {
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
