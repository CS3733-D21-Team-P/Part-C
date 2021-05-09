package edu.wpi.p.views.custom;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
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

import java.util.*;
import java.util.List;

public class ServiceRequestLogSection extends VBox {

    ObservableList<ServiceRequestTableEntry> observableRequests = FXCollections.observableArrayList();
    ;

    public ServiceRequestLogSection(String name, List<ServiceRequest> requests) {
        super();
        super.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("service request log section vbox change its width from " + oldValue + " to " + newValue);
        });
        HBox assignmentSearch = new HBox();

        JFXTreeTableView<ServiceRequestTableEntry> requestSection = makeGridSection(name, requests);

        Label assignmentLabel = makeAssignmentSearchLabel();
        JFXTextField filterField = makeAssignmentSearchField();
        JFXAutoCompletePopup<String> assignmentAutocompletePopup = new JFXAutoCompletePopup<>();
        assignmentAutocompletePopup.getSuggestions().addAll(getAllUserNames());

        assignmentSearch.getChildren().addAll(assignmentLabel, filterField);
        filterField.textProperty().addListener(observable -> {
            assignmentAutocompletePopup.filter(string -> string.toLowerCase().contains(filterField.getText().toLowerCase()));
            if (assignmentAutocompletePopup.getFilteredSuggestions().isEmpty() || filterField.getText().isEmpty()) {
                assignmentAutocompletePopup.hide();
                // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                assignmentAutocompletePopup.show(filterField);
            }
        });
        filterField.textProperty().addListener((o,oldVal,newVal)->{
            requestSection.setPredicate(serviceRequestTableEntryTreeItem -> {
                String assignment = serviceRequestTableEntryTreeItem.getValue().getServiceRequest().getAssignment();
                if (newVal.length() == 0) {
                    return true;
                }
                if (assignment != null) {
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

    private List<String> getAllUserNames() {
        DBUser dbUser = new DBUser();
        List<UserFromDB> users = dbUser.getUsers();
        List<String> names = new ArrayList<>(users.size());
        for (UserFromDB user : users) {
            names.add(user.getName());
        }
        return names;
    }
    private Label makeAssignmentSearchLabel() {
        Label assignmentLabel = new Label("Assigned to search:");
        assignmentLabel.setStyle("-fx-font-size: 24");
        assignmentLabel.setTextFill(Color.WHITE);
        return assignmentLabel;
    }

    private JFXTextField makeAssignmentSearchField() {
        JFXTextField filterField = new JFXTextField();
        filterField.setFocusColor(Color.WHITE);
        filterField.setUnFocusColor(Color.WHITE);
        filterField.setStyle("-fx-background-color: #f2f2f2");
        filterField.setMaxWidth(200);
        return filterField;
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
        assignedTo.setCellFactory(getAssignmentColumnCallback());
//        assignedTo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, String> param) ->
//                new SimpleStringProperty(param.getValue().getValue().getServiceRequest().getAssignment()));
//        assignedTo.setCellFactory((TreeTableColumn<ServiceRequestTableEntry, String> param) ->
//                new GenericEditableTreeTableCell<ServiceRequestTableEntry, String>(new TextFieldEditorBuilder()));
//
//        assignedTo.setOnEditCommit((TreeTableColumn.CellEditEvent<ServiceRequestTableEntry, String> t) -> {
//            ServiceRequest request = t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().getServiceRequest();
//            request.setAssignment(t.getNewValue());
//            DBServiceRequest dbServiceRequest = new DBServiceRequest();
//            dbServiceRequest.updateServiceRequest(request);
//
//        });
        JFXTreeTableColumn<ServiceRequestTableEntry, String> completeColumn = new JFXTreeTableColumn<>("Complete");
        completeColumn.setPrefWidth(100);
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
        treeView.setEditable(true);
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
                            toggleButton.setSelected(request.getCompleted());
                            toggleButton.armedProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue) {
                                    request.setCompleted(newValue);
                                    DBServiceRequest dbServiceRequest = new DBServiceRequest();
                                    dbServiceRequest.updateServiceRequest(request);
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

    private Callback<TreeTableColumn<ServiceRequestTableEntry, String>, TreeTableCell<ServiceRequestTableEntry, String>> getAssignmentColumnCallback() {
        return new Callback<TreeTableColumn<ServiceRequestTableEntry, String>, TreeTableCell<ServiceRequestTableEntry, String>>() {
            @Override
            public TreeTableCell<ServiceRequestTableEntry, String> call(final TreeTableColumn<ServiceRequestTableEntry, String> param) {
                return new TreeTableCell<ServiceRequestTableEntry, String>() {

                    final JFXTextField assignmentField = new JFXTextField();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            ServiceRequest request = getTreeTableView().getTreeItem(getIndex()).getValue().getServiceRequest();
                            System.out.println("request assignment is: " + request.getAssignment());
                            assignmentField.setText(request.getAssignment());
                            assignmentField.textProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue != null) {
                                    System.out.println("assignment field changed from: " + oldValue + " to: " + newValue);
                                    request.setAssignment(newValue);
                                    DBServiceRequest dbServiceRequest = new DBServiceRequest();
                                    dbServiceRequest.updateServiceRequest(request);
                                }

                            });
                            setGraphic(assignmentField);
                            setAlignment(Pos.CENTER);
                            setText(null);
                        }
                    }
                };
            }
        };
    }
}

