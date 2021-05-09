package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import java.io.IOException;
import java.util.List;

import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DatabaseInterface;
import edu.wpi.p.userstate.User;
import edu.wpi.p.views.servicerequests.EntryRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePage {
  @FXML
  public ImageView imageView;
  @FXML
  public Label hospitalNameLabel;
  @FXML
  public Label phoneLabel;
  @FXML
  public Label addressLabel;
  @FXML
  public JFXTextField searchText;
  @FXML
  public JFXButton SRoption;
//  public Button languageInterpretersBtn;
//  @FXML
//  public Button medicineDeliveryServiceBtn;
//  @FXML
//  public Button laundryServicesBtn;
//  @FXML
//  public Button externalPatientTransportationBtn;
  @FXML
  public JFXButton nodeButton;
  @FXML
  public JFXButton edgeButton;
  @FXML
  public JFXButton pathButton;
  @FXML
  public JFXButton editButton;
  @FXML
  public JFXButton entryButton;


  public static boolean Isguest;
  public static boolean approved;
  public JFXButton requsetLog;
  public JFXButton userAccount;
  public JFXButton employeeButton;
  public ImageView covidIcon;
  public ImageView editIcon;
  public ImageView pathIcon;
  public ImageView serviceIcon;
  public JFXButton admin;

//  public JFXButton languageInterpretersBtn;
//  public JFXButton medicineDeliveryServiceBtn;
//  public JFXButton laundryServicesBtn;
//  public JFXButton externalPatientTransportationBtn;
//  public JFXButton sanitationServiceBtn;

  @FXML
  private void advanceScene(ActionEvent e) {

  }

  @FXML
  private void initialize(){
    DatabaseInterface.init();
    List<String> tableNames = DatabaseInterface.getTableNames();
    assert tableNames != null;
    if(!tableNames.contains("EDGES") || !tableNames.contains("NODES")) {
      try {
        CSVData nodeData = CSVHandler.readCSVFile("bwPnodes.csv");
        CSVData edgeData = CSVHandler.readCSVFile("bwPedges.csv");
        CSVDBConverter.tableFromCSVData(nodeData, edgeData);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
//    EntryRequest.updateApproved();
    // by default everything is visible for the admin
    // if they are an employee, hide the things an employee can't see
    User user = User.getInstance();
    if(user.getPermissions().equals("Employee")){
      editButton.setVisible(false);
      editIcon.setVisible(false);
      userAccount.setVisible(false);
      employeeButton.setVisible(false);
      admin.setVisible(false);
    }
    // if they are a guest, hide everything but the request entry button by default
    if(user.isGuest() | user.getPermissions().equals("Patient")){
      pathButton.setVisible(false);
      editButton.setVisible(false);
      editIcon.setVisible(false);
      SRoption.setVisible(false);
      serviceIcon.setVisible(false);
      requsetLog.setVisible(false);
      userAccount.setVisible(false);
      employeeButton.setVisible(false);
      pathIcon.setVisible(false);
      admin.setVisible(false);
    }
    // if they are an approved user, they can see the pathfinding button and icon
    if (approved){
      pathButton.setVisible(true);
      pathIcon.setVisible(true);
    }
  }


  public void entryButtonAc(ActionEvent actionEvent){
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/servicerequestsFXML/EntryRequest.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void requestLogAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/RequestLogPage.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void CovidButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/CovidPage.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void medicineDeliveryServiceBtn(ActionEvent actionEvent) {}

  public void SRoptionAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

//  public void medicineDeliveryServiceBtn(ActionEvent actionEvent) {
//    try {
//      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/MedicineDeliveryServiceRequest.fxml"));
//      App.getPrimaryStage().getScene().setRoot(root);
//    } catch (IOException ex) {
//      ex.printStackTrace();
//    }
//  }
//
//  public void laundryServicesBtn(ActionEvent actionEvent) {
//    try {
//      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/LaundryServiceRequest.fxml"));
//      App.getPrimaryStage().getScene().setRoot(root);
//    } catch (IOException ex) {
//      ex.printStackTrace();
//    }
//  }

//  public void externalPatientTransportationBtn(ActionEvent actionEvent) {
//    try {
//      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ExternalpatienttransportationRequest.fxml"));
//      App.getPrimaryStage().getScene().setRoot(root);
//    } catch (IOException ex) {
//      ex.printStackTrace();
//    }
//  }

  public void userAccountAc(ActionEvent actionEvent){
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/AccountLog.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
public void backToLoginAc(ActionEvent actionEvent){
  try {
    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/LoginPage.fxml"));
    App.getPrimaryStage().getScene().setRoot(root);
    User.getInstance().logout();
  } catch (IOException ex) {
    ex.printStackTrace();
  }
}
  public void pathButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/PathfindingMap.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void employeeButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/Employee.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }


  public void edgeButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/MapPEdgeData.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void nodeButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/MapPNodeData.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void editButtonAc(){
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/mapsFXML/EditMap.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void updateApproved(){

  }

    public void admin(ActionEvent actionEvent) {
      try {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/AdminConfig.fxml"));
        App.getPrimaryStage().getScene().setRoot(root);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

//  public void sanitationServiceBtn(ActionEvent actionEvent) {
//  }
}
