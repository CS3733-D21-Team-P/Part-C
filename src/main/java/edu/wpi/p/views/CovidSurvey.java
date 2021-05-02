package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.p.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import java.io.IOException;

public class CovidSurvey {
    @FXML
    public Text firstText;
    @FXML
    public Text secondText;
    @FXML
    public Text thirdText;
    @FXML
    public Text fourthText;
    @FXML
    public Text fifthText;
    @FXML
    public CheckBox firstCheckBox;
    @FXML
    public CheckBox secondCheckBox;
    @FXML
    public CheckBox thirdCheckBox;
    @FXML
    public CheckBox fourthCheckBox;
    @FXML
    public CheckBox fifthCheckBox;
    @FXML
    public JFXButton submitButton;
    @FXML
    public JFXButton cancelButton;
    public boolean approved = false;

    @FXML
    private void submitPressed(ActionEvent actionEvent)
    {
        if (firstCheckBox.isSelected() || secondCheckBox.isSelected() || thirdCheckBox.isSelected() || fourthCheckBox.isSelected())
        {
            AlertBox.display("COVID Survey Results", "Potential COVID Risk. Please Use Emergency Entrance.");    // this is temporary
        }
        else{
            AlertBox.display("COVID Survey Results", "No Potential COVID Risk. Please Use 75 Francis Street Entrance.");
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancelPressed(ActionEvent actionEvent)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
