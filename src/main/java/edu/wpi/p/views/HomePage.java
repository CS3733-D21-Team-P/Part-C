package edu.wpi.p.views;

import edu.wpi.p.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class HomePage {

  @FXML
  private void advanceScene(ActionEvent e) {

  }

  public void languageInterpretersBtn(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/LanguageInterpreterServiceRequest.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void medicineDeliveryServiceBtn(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/MedicineDeliveryServiceRequest.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void laundryServicesBtn(ActionEvent actionEvent) {
  }

  public void externalPatientTransportationBtn(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ExternalpatienttransportationRequest.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
