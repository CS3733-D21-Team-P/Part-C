package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class LanguageInterpreterServiceRequest {

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
    final String lang = interpLanguage.getText();
    final String loc = interpLoc.getText();
    final String details = interpDetails.getText();

    System.out.println("submitting form for interpreter services");
    System.out.println("language = " + lang + "\nloc = " + loc + "\ndetails = " + details);
  }
}
