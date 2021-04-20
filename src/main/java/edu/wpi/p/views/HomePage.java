package edu.wpi.p.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.p.App;
import java.io.IOException;
import java.util.List;

import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.CSVDBConverter;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.DatabaseInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    if(!tableNames.contains("EDGES") || !tableNames.contains("NODES")) {
      try {
        CSVData nodeData = CSVHandler.readCSVFile("bwPnodes.csv");
        CSVData edgeData = CSVHandler.readCSVFile("bwPedges.csv");
        CSVDBConverter.tableFromCSVData(nodeData, edgeData);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  public void languageInterpretersBtn(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/LanguageInterpreterServiceRequest.fxml"));
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

  public void pathButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/PathfindingMap.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void edgeButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/EditMap.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void nodeButtonAc(ActionEvent actionEvent) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/MapPNodeData.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void editButtonAc(){
    try {
      Parent root = FXMLLoader.load(getClass().getResource("..."));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

//  public void sanitationServiceBtn(ActionEvent actionEvent) {
//  }
}
