package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.userstate.User;
import edu.wpi.p.views.HomePage;
import edu.wpi.p.views.Toolbar;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class EntryRequest extends GenericServiceRequest {
    public JFXTextField name;
    public JFXTextField loc;
    public JFXTimePicker time;
    public JFXTextArea reason;
    public JFXButton next;
    public JFXButton back;
    public boolean approved;
    public static ServiceRequest request;

    public String getName() {
        return name.getText();
    }

    public void setName(JFXTextField name) {
        this.name = name;
    }

    public String getLoc() {
        return loc.getText();
    }

    public void setLoc(JFXTextField loc) {
        this.loc = loc;
    }

    public static String[] fields = {"Name", "Location", "Time", "Reason For Visit"};
    public EntryRequest() {
        super();
        super.name = "Covid Entry Request";
    }

    @FXML
    public void initialize() {
        super.locationProperty = loc.textProperty();

        this.values.put("Name", name.textProperty());
        this.values.put("Location", loc.textProperty());
        this.values.put("Time", time.valueProperty());
        this.values.put("Reason For Visit", reason.textProperty());
        this.values.put("UserID", new SimpleStringProperty(User.getInstance().getId()));
    }

    public void nextAc(){
        approved = false;
        super.submitPressed(null);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/CovidPage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateApproved(){
//        if(request != null){
//            if(request.getCompleted()){
//                HomePage.approved = true;
//            }
//        }
    }
}
