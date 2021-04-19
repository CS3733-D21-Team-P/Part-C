package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBTable;
import edu.wpi.p.database.Edge;
import edu.wpi.p.database.ServiceRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;

import javax.swing.*;
import java.util.function.Predicate;

public class RequestLogPage {


    public JFXTreeTableView<ServiceRequest> ReqLogView;
    public JFXButton markCompleteBtn;
    public JFXTextField filterField;
    public JFXButton incompleteBtn;

    public void initialize() {
        JFXTreeTableColumn<ServiceRequest, String> reqName = new JFXTreeTableColumn<>("Name");
        reqName.setPrefWidth(150);
        reqName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getName();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> id = new JFXTreeTableColumn<>("ID");
        id.setPrefWidth(150);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getID();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> location = new JFXTreeTableColumn<>("Location");
        location.setPrefWidth(150);
        location.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getLocation();
            }
        });

        JFXTreeTableColumn<ServiceRequest, String> assignment = new JFXTreeTableColumn<>("Location");
        assignment.setPrefWidth(150);
        assignment.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequest, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequest, String> p) {
                return p.getValue().getValue().getAssignment();
            }
        });

        ServiceRequest completedReq = new ServiceRequest("B", "here", "5", "Doctor");
        completedReq.completed();
        ObservableList<ServiceRequest> requests = FXCollections.observableArrayList();
        //TODO: loop through DB to add Service requests to tableView
        requests.add(completedReq);
        requests.add(new ServiceRequest("a", "there", "5", "nurse"));
        requests.add(new ServiceRequest("a", "there", "5", "nurse"));
        requests.add(new ServiceRequest("a", "there", "5", "nurse"));
        requests.add(new ServiceRequest("a", "there", "5", "nurse"));
        requests.add(new ServiceRequest("a", "there", "5", "nurse"));

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
}
