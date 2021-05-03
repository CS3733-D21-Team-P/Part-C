package edu.wpi.p.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.App;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.custom.ServiceRequestLogSection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import com.jfoenix.controls.JFXTreeTableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RequestLogPage {

    @FXML
    public VBox requestLogVBox;
    public JFXTreeTableView<ServiceRequestTableEntry> ReqLogView;
    public JFXButton markCompleteBtn;
    public JFXTextField filterField;
    public JFXButton incompleteBtn;
    public JFXButton backBtn;
    private List<ServiceRequest> requestList;
    private DBServiceRequest dbServiceRequest = new DBServiceRequest();

    public void initialize() {
        requestList = dbServiceRequest.getServiceRequests();
        HashMap<String, List<ServiceRequest>> sortedRequests = sortServiceRequestsByType(requestList);
        JFXTabPane tabPane = new JFXTabPane();
        requestLogVBox.getChildren().add(tabPane);

        for (String type : sortedRequests.keySet().stream().sorted().collect(Collectors.toList())) {
            ServiceRequestLogSection section = new ServiceRequestLogSection(type, sortedRequests.get(type));
            Tab tab = new Tab(type);
            tab.setContent(section);
            tabPane.getTabs().add(tab);
//            requestLogVBox.getChildren().add(section);
//            VBox sectionVbox = new VBox();
//
//            Label heading = new Label(type);
//            heading.setTextFill(Color.WHITE);
//            sectionVbox.getChildren().add(heading);
//
//            for (ServiceRequest request : sortedRequests.get(type)) {
//                HashMap<String, String> details = request.getDetailsMap();
//                GridPane detailsGrid = new GridPane();
//                int columnIndex = 0;
//                for (String detailName: details.keySet()) {
//                    Label name = new Label(detailName);
//                    name.setTextFill(Color.WHITE);
//                    Label value = new Label(details.get(detailName));
//                    value.setTextFill(Color.WHITE);
//                    VBox detailBox = new VBox(name, value);
//                    detailsGrid.addColumn(columnIndex, detailBox);
//                    columnIndex++;
//                }
//                sectionVbox.getChildren().add(detailsGrid);
//            }
//            sectionVbox.setVisible(true);
//            requestLogVBox.getChildren().add(sectionVbox);
        }
//        JFXTreeTableColumn<ServiceRequestTableEntry, String> reqName = new JFXTreeTableColumn<>("Name");
//        reqName.setPrefWidth(150);
//        reqName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
//                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getName());
//            }
//        });
//
//        JFXTreeTableColumn<ServiceRequestTableEntry, String> id = new JFXTreeTableColumn<>("ID");
//        id.setPrefWidth(150);
//        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
//                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getID());
//            }
//        });
//
//        JFXTreeTableColumn<ServiceRequestTableEntry, String> location = new JFXTreeTableColumn<>("Location");
//        location.setPrefWidth(150);
//        location.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
//                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getLocation());
//            }
//        });
//
//        JFXTreeTableColumn<ServiceRequestTableEntry, String> assignment = new JFXTreeTableColumn<>("Assignment");
//        assignment.setPrefWidth(150);
//        assignment.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
//                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getAssignment());
//            }
//        });
//        JFXTreeTableColumn<ServiceRequestTableEntry, String> details = new JFXTreeTableColumn<>("Details");
//        details.setPrefWidth(150);
//        details.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> p) {
//                return new SimpleStringProperty(p.getValue().getValue().getServiceRequest().getDetails());
//            }
//        });
//        requestList = dbServiceRequest.getServiceRequests();
//        ObservableList<ServiceRequestTableEntry> requests = FXCollections.observableArrayList();
//        for (ServiceRequest r : requestList) {
//            requests.add(new ServiceRequestTableEntry(r));
//        }
//
//        final TreeItem<ServiceRequestTableEntry> root = new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
//        ReqLogView.getColumns().setAll(reqName, location, id, assignment, details);
//        ReqLogView.setRoot(root);
//        ReqLogView.setShowRoot(false);
//
//        filterField.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                ReqLogView.setPredicate(new Predicate<TreeItem<ServiceRequestTableEntry>>() {
//                    @Override
//                    public boolean test(TreeItem<ServiceRequestTableEntry> serviceRequestTreeItem) {
//                        Boolean flag = serviceRequestTreeItem.getValue().getServiceRequest().getAssignment().contains(newValue);
//                        return flag;
//                    }
//                });
//            }
//        });

    }

    public HashMap<String, List<ServiceRequest>> sortServiceRequestsByType(List<ServiceRequest> serviceRequests) {
        HashMap<String, List<ServiceRequest>> result = new HashMap<>();
        for (ServiceRequest request : serviceRequests) {
            String name = request.getName();
            if (!result.containsKey(name)) {
                result.put(name, new ArrayList<>());
            }
            result.get(name).add(request);
        }

        return result;
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
