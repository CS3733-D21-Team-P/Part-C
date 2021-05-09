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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    @FXML private Pane clippoID;
    @FXML private ClippoController clippoIDController;

    @FXML
    public VBox requestLogVBox;
    @FXML public BorderPane borderPane;
    public JFXTreeTableView<ServiceRequestTableEntry> ReqLogView;
    public JFXButton markCompleteBtn;
    public JFXTextField filterField;
    public JFXButton incompleteBtn;
    public JFXButton backBtn;
    private List<ServiceRequest> requestList;
    private DBServiceRequest dbServiceRequest = new DBServiceRequest();

    public void initialize() {
        clippoIDController.setPage("requestLogs");
        requestList = dbServiceRequest.getServiceRequests();
        HashMap<String, List<ServiceRequest>> sortedRequests = sortServiceRequestsByType(requestList);
        JFXTabPane tabPane = new JFXTabPane();
        tabPane.setMinWidth(0);
        borderPane.setCenter(tabPane);
        BorderPane.setAlignment(tabPane, Pos.TOP_LEFT);
        borderPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            tabPane.setMaxWidth(newValue.doubleValue());
            System.out.println("border pane width changed from " + oldValue + " to " + newValue);
        });
        tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("higher tab pane width changed from " + oldValue + " to " + newValue);
        });
//        requestLogVBox.getChildren().add(tabPane);

        for (String type : sortedRequests.keySet().stream().sorted().collect(Collectors.toList())) {
            ServiceRequestLogSection section = new ServiceRequestLogSection(type, sortedRequests.get(type));
            Tab tab = new Tab(type);

            tab.setContent(section);
            tabPane.getTabs().add(tab);
            section.maxWidthProperty().bind(tab.getTabPane().widthProperty());
            tab.getTabPane().widthProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("tab pane width changed from " + oldValue + " to " + newValue);
            });
        }
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
