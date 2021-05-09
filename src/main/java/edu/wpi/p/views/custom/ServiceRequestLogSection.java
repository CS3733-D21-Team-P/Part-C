package edu.wpi.p.views.custom;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.ServiceRequestTableEntry;
import edu.wpi.p.views.servicerequests.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;

import javax.swing.event.ChangeListener;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ServiceRequestLogSection extends VBox {

    ObservableList<ServiceRequestTableEntry> observableRequests = FXCollections.observableArrayList();


    public ServiceRequestLogSection(String name, List<ServiceRequest> requests) {
        super();
        HBox assignmentSearch = new HBox();
        Label assignmentLabel = new Label("Assigned to search:");
        assignmentLabel.setStyle("-fx-font-size: 24");
        assignmentLabel.setTextFill(Color.WHITE);
        JFXTreeTableView<ServiceRequestTableEntry> requestSection = makeGridSection(name, requests);
        JFXTextField filterField = new JFXTextField();
        filterField.setFocusColor(Color.WHITE);
        filterField.setUnFocusColor(Color.WHITE);
        filterField.setStyle("-fx-background-color: #f2f2f2");
        filterField.setMaxWidth(200);
        assignmentSearch.getChildren().addAll(assignmentLabel, filterField);
//        super.setFillWidth(false);


        filterField.textProperty().addListener((o,oldVal,newVal)->{
            requestSection.setPredicate(serviceRequestTableEntryTreeItem -> {
                String assignment = serviceRequestTableEntryTreeItem.getValue().getServiceRequest().getAssignment();
                System.out.println("assignment is " + assignment);
                if (newVal.length() == 0) {
                    return true;
                }
                if (assignment != null) {
                    System.out.println("newVal: " + newVal);
                    System.out.println("assignment isn't null, does assignment contain? " + assignment.contains(newVal));
                    return assignment.contains(newVal);
                }
                return false;
            });
        });
//        requestSection.prefHeightProperty().bind(super.heightProperty());
        super.setVgrow(requestSection, Priority.ALWAYS);
        this.getChildren().add(assignmentSearch);
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
            case "Food Delivery Request":
                return FoodDeliveryRequest.fields;
            case "Gift Delivery":
                return GiftDelivery.fields;
            case "Internal Patient Transportation":
                return InternalPatientTransportation.fields;
            case "Language Interpreter Request":
                return LanguageInterpreterServiceRequest.fields;
            case "Laundry Service Request":
                return LaundryServiceRequest.fields;
            case "Medicine Delivery Request":
                return MedicineDeliverySR.fields;
            case "Sanitation Request":
                return SanitationServiceRequest.fields;
            default:
                System.out.println("DON'T KNOW THE CLASS FOR TYPE OF " + type);
                return new String[]{};
        }
    }

    private List<String> getAllDetailColumns(List<ServiceRequest> requests) {
        List<String> detailNames = new ArrayList<>();
        for (ServiceRequest request : requests) {
            for (String key : request.getDetailNames()) {
                if (!detailNames.contains(key)) {
                    detailNames.add(key);
                }
            }
        }
        return detailNames;
    }

    private String[] getCovidEntryDetailColumn(List<ServiceRequest> requests) {
        List<String> detailColumn = getAllDetailColumns(requests);
        detailColumn.remove("UserID");
        return detailColumn.toArray(new String[0]);
    }

    private JFXTreeTableView<ServiceRequestTableEntry> makeGridSection(String type, List<ServiceRequest> requests) {
        boolean isCovidEntryRequest = type.equals("Covid Entry Request");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        String[] detailColumns = getColumnFromType(type);
        if (isCovidEntryRequest) {
            detailColumns = getCovidEntryDetailColumn(requests);
        }

        for (ServiceRequest request : requests) {
            observableRequests.add(new ServiceRequestTableEntry(request));
        }
        List<JFXTreeTableColumn<ServiceRequestTableEntry, Label>> tableColumns = makeDetailColumns(detailColumns);

        JFXTreeTableColumn<ServiceRequestTableEntry, String> assignedTo = makeAssignedToColumn();
        JFXTreeTableColumn<ServiceRequestTableEntry, String> completeColumn = makeCompleteColumn();

        final TreeItem<ServiceRequestTableEntry> root = new RecursiveTreeItem<>(observableRequests, RecursiveTreeObject::getChildren);
        JFXTreeTableView<ServiceRequestTableEntry> treeView = new JFXTreeTableView<>(root);
        for (JFXTreeTableColumn<ServiceRequestTableEntry, Label> c : tableColumns) {
            treeView.getColumns().add(c);
        }

        treeView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeView.getColumns().add(assignedTo);
        treeView.getColumns().add(completeColumn);
        if (isCovidEntryRequest) {
            treeView.getColumns().add(makeCovidRiskColumn());
        }
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.maxWidthProperty().bind(super.widthProperty());
        treeView.setEditable(true);
        return treeView;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, String>  makeCompleteColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, String> completeColumn = new JFXTreeTableColumn<>("Complete");
        completeColumn.setPrefWidth(100);
        completeColumn.setCellFactory(getCompleteToggleButtonColumnCallback((request, newValue) -> {
            if (newValue) {
                request.setCompleted(newValue);
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(request);
            }
        }, request -> request.getCompleted()));
        return completeColumn;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, String>  makeCovidRiskColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, String> completeColumn = new JFXTreeTableColumn<>("Is Covid Risk");
        completeColumn.setPrefWidth(100);
        completeColumn.setCellFactory(getCompleteToggleButtonColumnCallback((request, newValue) -> {
            if (newValue) {
                request.addDetail("Is Covid Risk", "yes");
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(request);
            }
        }, request -> request.getDetailNames().contains("Is Covid Risk")));
        return completeColumn;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, String>  makeAssignedToColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, String> assignedTo = new JFXTreeTableColumn<>("Assigned To");

        assignedTo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().getServiceRequest().getAssignment()));
        assignedTo.setCellFactory((TreeTableColumn<ServiceRequestTableEntry, String> param) ->
                new GenericEditableTreeTableCell<ServiceRequestTableEntry, String>(new TextFieldEditorBuilder()));
        assignedTo.setOnEditCommit((TreeTableColumn.CellEditEvent<ServiceRequestTableEntry, String> t) -> {
            ServiceRequest request = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().getServiceRequest();
            request.setAssignment(t.getNewValue());
            DBServiceRequest dbServiceRequest = new DBServiceRequest();
            dbServiceRequest.updateServiceRequest(request);
        });
        return assignedTo;
    }
    private List<JFXTreeTableColumn<ServiceRequestTableEntry, Label> > makeDetailColumns(String[] detailColumns) {
        List<JFXTreeTableColumn<ServiceRequestTableEntry, Label> > tableColumns = new ArrayList<>(detailColumns.length);
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
        return tableColumns;
    }
    /**
     * It is surprisingly hard to put a toggle button in a table
     *
     * @return The callback that makes the thing that does the thing
     */
    private Callback<TreeTableColumn<ServiceRequestTableEntry, String>, TreeTableCell<ServiceRequestTableEntry, String>> getCompleteToggleButtonColumnCallback(BiConsumer<ServiceRequest, Boolean> toggleChangeConsumer, Callback<ServiceRequest, Boolean> setterCallback) {
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
                            toggleButton.setSelected(setterCallback.call(request));
//                            toggleButton.armedProperty().addListener();
                            toggleButton.armedProperty().addListener((observable, oldValue, newValue) -> {
                                toggleChangeConsumer.accept(request, newValue);
                                if (newValue) {
                                    toggleChangeConsumer.accept(request, newValue);
                                } else {
                                    toggleButton.setSelected(true);
                                }
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

