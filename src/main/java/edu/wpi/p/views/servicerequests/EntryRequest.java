package edu.wpi.p.views.servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.HomePage;
import edu.wpi.p.views.Toolbar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class EntryRequest extends Toolbar {
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

    public void nextAc(){
        approved = false;
        final String n = name.getText();
        final String location = loc.getText();
        final String r = reason.getText();

        ServiceRequest sR = new ServiceRequest(n, location, "Name" + "_" + location, "Entry Request");
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.addServiceRequest(sR);
        request = sR;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/CovidPage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateApproved(){
        if(request != null){
            if(request.getCompleted()){
                HomePage.approved = true;
            }
        }
    }
}
