package edu.wpi.p.views.custom;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.ServiceRequestTableEntry;
import edu.wpi.p.views.servicerequests.ExternalpatienttransportationRequest;
import edu.wpi.p.views.servicerequests.FacilitiesMaintenanceRequest;
import edu.wpi.p.views.servicerequests.FloralDeliveryRequest;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ServiceRequestLogSection extends VBox {

    ObservableList<ServiceRequestTableEntry> observableRequests = FXCollections.observableArrayList();;

    public ServiceRequestLogSection(String name, List<ServiceRequest> requests) {
        super();
        super.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("service request log section vbox change its width from " + oldValue + " to " + newValue);
        });
        JFXTreeTableView<ServiceRequestTableEntry> requestSection = makeGridSection(name, requests);
        this.getChildren().add(requestSection);

    }

    private String[] getColumnFromType(String type) {
        switch (type) {
            case "External Patient Transport":
                return ExternalpatienttransportationRequest.fields;
            case "Facilities Maintenance Request":
                return FacilitiesMaintenanceRequest.fields;
            case "Floral Delivery Request":
                return FloralDeliveryRequest.fields;
            default:
                System.out.println("DON'T KNOW THE CLASS FOR TYPE OF " + type);
                return new String[]{};
        }
    }

    private Set<String> getAllDetailColumns(List<ServiceRequest> requests) {
        Set<String> detailNames = new HashSet<>();
        for (ServiceRequest request : requests) {
            for (String key : request.getDetailsMap().keySet()) {
                detailNames.add(key);
            }
        }

        return detailNames;
    }

    private JFXTreeTableView<ServiceRequestTableEntry> makeGridSection(String type, List<ServiceRequest> requests) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        String[] detailColumns = getColumnFromType(type);
        List<JFXTreeTableColumn> tableColumns = new ArrayList<>(detailColumns.length);
        for (ServiceRequest request : requests) {
            observableRequests.add(new ServiceRequestTableEntry(request));
        }

        for (String detailName : detailColumns) {
            JFXTreeTableColumn<ServiceRequestTableEntry, Label> column = new JFXTreeTableColumn<>(detailName);
            column.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, Label> param) -> {
                Label l = new Label(param.getValue().getValue().getServiceRequest().getDetailsMap().get(detailName));
                l.setWrapText(true);
                return new SimpleObjectProperty<Label>(l);
            });

            tableColumns.add(column);
            column.setResizable(true);
        }

        JFXTreeTableColumn<ServiceRequestTableEntry, String> assignedTo = new JFXTreeTableColumn<>("Assigned To");
//        assignedTo.setPrefWidth(100);
        assignedTo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().getServiceRequest().getAssignment()));
        JFXTreeTableColumn<ServiceRequestTableEntry, String> completeColumn = new JFXTreeTableColumn<>("Complete");
//        completeColumn.setPrefWidth(100);
        completeColumn.setCellFactory(getCompleteToggleButtonColumnCallback());

        final TreeItem<ServiceRequestTableEntry> root = new RecursiveTreeItem<>(observableRequests, RecursiveTreeObject::getChildren);
        JFXTreeTableView<ServiceRequestTableEntry> treeView = new JFXTreeTableView<>(root);
        for (JFXTreeTableColumn<ServiceRequestTableEntry, String> c : tableColumns) {
            treeView.getColumns().add(c);
        }

        treeView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeView.getColumns().add(assignedTo);
        treeView.getColumns().add(completeColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.maxWidthProperty().bind(super.widthProperty());
        return treeView;
    }

    /**
     * It is surprisingly hard to put a toggle button in a table
     *
     * @return The callback that makes the thing that does the thing
     */
    private Callback<TreeTableColumn<ServiceRequestTableEntry, String>, TreeTableCell<ServiceRequestTableEntry, String>> getCompleteToggleButtonColumnCallback() {
        return new Callback<TreeTableColumn<ServiceRequestTableEntry, String>, TreeTableCell<ServiceRequestTableEntry, String>>() {
            @Override
            public TreeTableCell<ServiceRequestTableEntry, String> call(final TreeTableColumn<ServiceRequestTableEntry, String> param) {
                return new TreeTableCell<ServiceRequestTableEntry, String>() {

                    final JFXToggleButton toggleButton = new JFXToggleButton();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            ServiceRequest request = getTreeTableView().getTreeItem(getIndex()).getValue().getServiceRequest();
                            toggleButton.selectedProperty().set(request.getCompleted());
                            toggleButton.armedProperty().addListener((observable, oldValue, newValue) -> {
                                request.setCompleted(newValue);
                                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                                dbServiceRequest.updateServiceRequest(request);
                            });
                            setGraphic(toggleButton);
                            setAlignment(Pos.CENTER);
                            setText(null);
                        }
                    }
                };
            }
        };
    }
}

