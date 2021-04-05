package edu.wpi.p.views;

import edu.wpi.p.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Scene1 {

  @FXML
  private void advanceScene(ActionEvent e) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/Scene2.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}