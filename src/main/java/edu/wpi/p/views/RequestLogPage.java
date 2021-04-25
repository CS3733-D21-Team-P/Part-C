package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class RequestLogPage {


    public JFXTreeTableView<ServiceRequestTableEntry> ReqLogView;
    public JFXButton markCompleteBtn;
    public JFXTextField filterField;
    public JFXButton incompleteBtn;
    public JFXButton backBtn;
    private List<ServiceRequest> requestList;
    private DBServiceRequest dbServiceRequest = new DBServiceRequest();

    public void initialize() {
        JFXTreeTableColumn<ServiceRequestTableEntry, String> reqName = new JFXTreeTableColumn<>("Name");
        reqName.setPrefWidth(150);
        reqName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getName());
            }
        });

        JFXTreeTableColumn<ServiceRequestTableEntry, String> id = new JFXTreeTableColumn<>("ID");
        id.setPrefWidth(150);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getID());
            }
        });

        JFXTreeTableColumn<ServiceRequestTableEntry, String> location = new JFXTreeTableColumn<>("Location");
        location.setPrefWidth(150);
        location.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getLocation());
            }
        });

        JFXTreeTableColumn<ServiceRequestTableEntry, String> assignment = new JFXTreeTableColumn<>("Assignment");
        assignment.setPrefWidth(150);
        assignment.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getAssignment());
            }
        });
        requestList = dbServiceRequest.getServiceRequests();
        ObservableList<ServiceRequestTableEntry> requests = FXCollections.observableArrayList();
        for (ServiceRequest r : requestList) {
            requests.add(new ServiceRequestTableEntry(new ServiceRequest(r.getName(), r.getLocation(), r.getID(), r.getAssignment())));
        }

        final TreeItem<ServiceRequestTableEntry> root = new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
        ReqLogView.getColumns().setAll(reqName, location, id, assignment);
        ReqLogView.setRoot(root);
        ReqLogView.setShowRoot(false);

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ReqLogView.setPredicate(new Predicate<TreeItem<ServiceRequestTableEntry>>() {
                    @Override
                    public boolean test(TreeItem<ServiceRequestTableEntry> serviceRequestTreeItem) {
                        Boolean flag = serviceRequestTreeItem.getValue().getServiceRequest().getAssignment().contains(newValue);
                        return flag;
                    }
                });
            }
        });

    }

    public void markCompleteBtnAc(ActionEvent actionEvent) {
        ReqLogView.getSelectionModel().getSelectedItem().getValue().getServiceRequest().completed();
        dbServiceRequest.updateServiceRequest(ReqLogView.getSelectionModel().getSelectedItem().getValue().getServiceRequest());
    }

    public void incompleteBtnAc(ActionEvent actionEvent) {
        ReqLogView.setPredicate(new Predicate<TreeItem<ServiceRequestTableEntry>>() {
            @Override
            public boolean test(TreeItem<ServiceRequestTableEntry> serviceRequestTreeItem) {
                Boolean flag = serviceRequestTreeItem.getValue().getServiceRequest().getCompleted();
                return !flag;
            }
        });
    }

    public void backBtnAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/ServiceRequestHomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
