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

public class FoodDeliveryRequest extends GenericServiceRequest {
    @FXML
    public JFXTextField name;
    @FXML
    public JFXTextField currRoom;
    @FXML
    public JFXRadioButton regular;
    @FXML
    public JFXRadioButton vegetarian;
    @FXML
    public JFXRadioButton vegan;
    @FXML
    public JFXTextArea accommodations;
    @FXML
    public JFXButton back;
    @FXML
    public JFXButton submit;

    public static final String[] fields = {"Name", "Room", "Food Type", "Accommodations"};

    public FoodDeliveryRequest() {
        super();
        super.name = "Food Delivery Request";
    }

    @FXML
    public void initialize() {
        this.locationProperty = currRoom.textProperty();
        StringProperty foodType = createJFXRadioButtonStringProperty(regular, vegetarian, vegan);
        this.values.put("Name", name.textProperty());
        this.values.put("Room", currRoom.textProperty());
        this.values.put("Food Type", foodType);
        this.values.put("Accommodations", accommodations.textProperty());
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
