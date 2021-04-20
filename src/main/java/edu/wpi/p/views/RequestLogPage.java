package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.Edge;
import edu.wpi.p.database.ServiceRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class RequestLogPage {


    public JFXTreeTableView<ServiceRequest> ReqLogView;
    public JFXButton markCompleteBtn;
    public JFXTextField filterField;
    public JFXButton incompleteBtn;
    public JFXButton backBtn;
    private List<ServiceRequest> requestList;
    private DBServiceRequest dbServiceRequest = new DBServiceRequest();

    public void initialize() {
        JFXTreeTableColumn<ServiceRequest, String> reqName = new JFXTreeTableColumn<>("Name");
        reqName.setPrefWidth(150);
        reqName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getServiceRequestName();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> id = new JFXTreeTableColumn<>("ID");
        id.setPrefWidth(150);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getServiceRequestID();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> location = new JFXTreeTableColumn<>("Location");
        location.setPrefWidth(150);
        location.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getServiceRequestLocation();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> assignment = new JFXTreeTableColumn<>("Assignment");
        assignment.setPrefWidth(150);
        assignment.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getAssignment();
            }
        });
        requestList = dbServiceRequest.getServiceRequests();
        ObservableList<ServiceRequest> requests = FXCollections.observableArrayList();
        for (ServiceRequest r : requestList) {
            requests.add(new ServiceRequest(r.getServiceRequestName(), r.getServiceRequestLocation(), r.getServiceRequestID(), r.getAssignment()));
        }

        final TreeItem<ServiceRequest> root = new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
        ReqLogView.getColumns().setAll(reqName, location, id, assignment);
        ReqLogView.setRoot(root);
        ReqLogView.setShowRoot(false);

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ReqLogView.setPredicate(new Predicate<TreeItem<ServiceRequest>>() {
                    @Override
                    public boolean test(TreeItem<ServiceRequest> serviceRequestTreeItem) {
                        Boolean flag = serviceRequestTreeItem.getValue().getAssignment().getValue().contains(newValue);
                        return flag;
                    }
                });
            }
        });

    }

    public void markCompleteBtnAc(ActionEvent actionEvent) {
        ReqLogView.getSelectionModel().getSelectedItem().getValue().completed();
        dbServiceRequest.updateServiceRequest(ReqLogView.getSelectionModel().getSelectedItem().getValue());
    }

    public void incompleteBtnAc(ActionEvent actionEvent) {
        ReqLogView.setPredicate(new Predicate<TreeItem<ServiceRequest>>() {
            @Override
            public boolean test(TreeItem<ServiceRequest> serviceRequestTreeItem) {
                Boolean flag = serviceRequestTreeItem.getValue().getCompleted();
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
