package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.userstate.User;
import edu.wpi.p.views.servicerequests.EntryRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import org.sqlite.core.DB;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CovidSurvey {
    @FXML private Pane clippoID;
    @FXML private ClippoController clippoIDController;
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
    public JFXTextField name;
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
    public ServiceRequest request;

    @FXML public void initialize(){clippoIDController.setPage("covidSurvey"); }

    @FXML
    public void submitPressed(ActionEvent actionEvent)
    {
        final String n = name.getText();
        ServiceRequest userEntryRequest = getCurrentUserCovidEntryRequest();
        if (userEntryRequest == null) {
            return;
        }

        addCheckBoxDetailsToServiceRequest(userEntryRequest, firstText, firstCheckBox);
        addCheckBoxDetailsToServiceRequest(userEntryRequest, secondText, secondCheckBox);
        addCheckBoxDetailsToServiceRequest(userEntryRequest, thirdText, thirdCheckBox);
        addCheckBoxDetailsToServiceRequest(userEntryRequest, fourthText, fourthCheckBox);
        addCheckBoxDetailsToServiceRequest(userEntryRequest, fifthText, fifthCheckBox);
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        dbServiceRequest.updateServiceRequest(userEntryRequest);

        if (firstCheckBox.isSelected() || secondCheckBox.isSelected() || thirdCheckBox.isSelected() || fourthCheckBox.isSelected())
        {
            AlertBox.displayCOVID("COVID Survey Results", "Potential COVID Risk. Please Use Emergency Entrance.");
//            ServiceRequest sR = new ServiceRequest(n, "Emergency Entrance", "Name" + "_" + "Emergency Entrance", "COVID Survey");
//            DBServiceRequest dbServiceRequest = new DBServiceRequest();
//            dbServiceRequest.addServiceRequest(sR);
//            request = sR;
        }
        else{
            AlertBox.displayCOVID("COVID Survey Results", "No Potential COVID Risk. Please Use 75 Francis Street Entrance.");
//            ServiceRequest sR = new ServiceRequest(n, "75 Francis", "Name" + "_" + "75 Francis", "COVID Survey");
//            DBServiceRequest dbServiceRequest = new DBServiceRequest();
//            dbServiceRequest.addServiceRequest(sR);
//            request = sR;
        }

//        HomePage.approved = true;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addCheckBoxDetailsToServiceRequest(ServiceRequest request, Text detailText, CheckBox checkBox) {
        request.addDetail(detailText.getText(), checkBox.isSelected() ? "Yes" : "No");
    }

    private ServiceRequest getCurrentUserCovidEntryRequest() {
        List<ServiceRequest> covidEntryRequests = getCovidEntryServiceRequests();
        for(ServiceRequest covidRequest : covidEntryRequests) {
            String id = covidRequest.getDetailsMap().get("UserID");
            if (id != null && id.equals(User.getInstance().getId())) {
                return covidRequest;
            }
        }
        return null;
    }

    private List<ServiceRequest> getCovidEntryServiceRequests() {
        DBServiceRequest dbServiceRequest = new DBServiceRequest();
        List<ServiceRequest> requestList = dbServiceRequest.getServiceRequests();
        String entryRequestName = "Covid Entry Request";
        return requestList.stream().filter(serviceRequest -> serviceRequest.getName().equals(entryRequestName)).collect(Collectors.toList());
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
