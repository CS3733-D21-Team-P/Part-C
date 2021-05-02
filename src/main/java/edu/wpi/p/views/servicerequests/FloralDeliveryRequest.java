package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.*;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.Toolbar;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FloralDeliveryRequest extends GenericServiceRequest {
    @FXML
    public JFXTextField name;
    @FXML
    public JFXTextField currRoom;
    @FXML
    public JFXRadioButton roses;
    @FXML
    public JFXRadioButton tulips;
    @FXML
    public JFXRadioButton painBush;
    @FXML
    public JFXRadioButton small;
    @FXML
    public JFXRadioButton medium;
    @FXML
    public JFXRadioButton large;
    @FXML
    public JFXColorPicker color;
    @FXML
    public JFXTextArea message;
    @FXML
    public JFXButton back;
    @FXML
    public JFXButton submit;

    public FloralDeliveryRequest() {
        super();
        super.name = "Floral Delivery Request";
    }
    @FXML
    public void initialize() {
        StringProperty flowerProperty = createJFXRadioButtonStringProperty(roses, tulips, painBush);
        StringProperty sizeProperty = createJFXRadioButtonStringProperty(small, medium, large);
        this.locationProperty = currRoom.textProperty();
        this.values.put("Name", name.textProperty());
        this.values.put("Room Number", currRoom.textProperty());
        this.values.put("Flowers", flowerProperty);
        this.values.put("Size", sizeProperty);
        this.values.put("Message", message.textProperty());
        this.values.put("Color", color.valueProperty());
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
}
