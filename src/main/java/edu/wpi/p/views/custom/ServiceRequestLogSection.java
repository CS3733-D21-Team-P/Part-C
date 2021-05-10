package edu.wpi.p.views.custom;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBServiceRequest;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.database.rowdata.ServiceRequest;
import edu.wpi.p.views.ServiceRequestTableEntry;
import edu.wpi.p.views.servicerequests.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

public class ServiceRequestLogSection extends VBox {

    private ObservableList<ServiceRequestTableEntry> observableRequests = FXCollections.observableArrayList();
    private JFXTreeTableView<ServiceRequestTableEntry> requestSection;
    private JFXTextField filterField;
    private JFXToggleButton showCompleteToggle;
    public ServiceRequestLogSection(String name, List<ServiceRequest> requests) {
        super();
        HBox assignmentSearch = new HBox();
        HBox showComplete = new HBox();
        requestSection = makeGridSection(name, requests);

        Label assignmentLabel = makeStyledLabel("Assigned to search:");
        filterField = makeAssignmentSearchField();
        Label showCompleteLabel = makeStyledLabel("Show Completed:");
        showCompleteToggle = new JFXToggleButton();

        // making the autocompete for assignment search
        JFXAutoCompletePopup<String> assignmentAutocompletePopup = new JFXAutoCompletePopup<>();
        assignmentAutocompletePopup.getSuggestions().addAll(getAllUserNames());
        assignmentAutocompletePopup.setSelectionHandler(event -> {
            filterField.setText(event.getObject());
        });

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
            requestSection.setPredicate(assignmentPredicate().call(newVal).and(completePredicate().call(showCompleteToggle.isSelected())));
        });

        showCompleteToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            requestSection.setPredicate(assignmentPredicate().call(filterField.getText()).and(completePredicate().call(newValue)));
        });
        requestSection.setPredicate(assignmentPredicate().call(filterField.getText()).and(completePredicate().call(showCompleteToggle.isSelected())));
        showComplete.getChildren().addAll(showCompleteLabel, showCompleteToggle);

//        requestSection.prefHeightProperty().bind(super.heightProperty());
        super.setVgrow(requestSection, Priority.ALWAYS);
        this.getChildren().add(assignmentSearch);
        this.getChildren().add(showComplete);
        this.getChildren().add(requestSection);

    }

    private Callback<String, Predicate<TreeItem<ServiceRequestTableEntry>>> assignmentPredicate() {
        Callback<String, Predicate<TreeItem<ServiceRequestTableEntry>>> callback = new Callback<String, Predicate<TreeItem<ServiceRequestTableEntry>>>() {
            @Override
            public Predicate<TreeItem<ServiceRequestTableEntry>> call(String param) {
                Predicate<TreeItem<ServiceRequestTableEntry>> predicate = new Predicate<TreeItem<ServiceRequestTableEntry>>() {
                    @Override
                    public boolean test(TreeItem<ServiceRequestTableEntry> serviceRequestTableEntryTreeItem) {
                        String assignment = serviceRequestTableEntryTreeItem.getValue().getServiceRequest().getAssignment();
                        if (param.length() == 0) {
                            return true;
                        }
                        if (assignment != null) {
                            return assignment.contains(param);
                        }
                        return false;
                    }
                };
                return predicate;
            }
        };
        return callback;
    }
    private Callback<Boolean, Predicate<TreeItem<ServiceRequestTableEntry>>> completePredicate() {
        Callback<Boolean, Predicate<TreeItem<ServiceRequestTableEntry>>> callback = new Callback<Boolean, Predicate<TreeItem<ServiceRequestTableEntry>>>() {
            @Override
            public Predicate<TreeItem<ServiceRequestTableEntry>> call(Boolean param) {
                Predicate<TreeItem<ServiceRequestTableEntry>> predicate = new Predicate<TreeItem<ServiceRequestTableEntry>>() {
                    @Override
                    public boolean test(TreeItem<ServiceRequestTableEntry> serviceRequestTableEntryTreeItem) {
                        Boolean complete = serviceRequestTableEntryTreeItem.getValue().getServiceRequest().getCompleted();
                        if (param) {
                            return true;
                        }
                        else {
                            return !complete;
                        }

                    }
                };
                return predicate;
            }
        };
        return callback;
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
    private Label makeStyledLabel(String text) {
        Label assignmentLabel = new Label(text);
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
        detailColumn.remove("Is Covid Risk");
        detailColumn.remove("Approved to Enter");
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

        JFXTreeTableColumn<ServiceRequestTableEntry, JFXTextField> assignedTo = makeAssignedToColumn();
        JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton> completeColumn = makeCompleteColumn();

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
            treeView.getColumns().add(makeApprovedEntryColumn());
        }
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.maxWidthProperty().bind(super.widthProperty());
        treeView.setEditable(true);
        return treeView;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton>  makeCompleteColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton> completeColumn = new JFXTreeTableColumn<>("Complete");
        completeColumn.setPrefWidth(100);
        completeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, JFXToggleButton> param) -> {
            JFXToggleButton toggleButton = new JFXToggleButton();
            toggleButton.setSelected(param.getValue().getValue().getServiceRequest().getCompleted());
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                ServiceRequest serviceRequest = param.getValue().getValue().getServiceRequest();
                serviceRequest.setCompleted(newValue);
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(serviceRequest);
                requestSection.setPredicate(assignmentPredicate().call(filterField.getText()).and(completePredicate().call(showCompleteToggle.isSelected())));
            });
            return new SimpleObjectProperty<JFXToggleButton>(toggleButton);
        });
        return completeColumn;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton>  makeCovidRiskColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton> covidRiskColumn = new JFXTreeTableColumn<>("Is Covid Risk");
        covidRiskColumn.setPrefWidth(100);
        covidRiskColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, JFXToggleButton> param) -> {
            JFXToggleButton toggleButton = new JFXToggleButton();
            toggleButton.setSelected(param.getValue().getValue().getServiceRequest().getDetailNames().contains("Is Covid Risk"));
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                ServiceRequest serviceRequest = param.getValue().getValue().getServiceRequest();
                if (newValue) {
                    serviceRequest.addDetail("Is Covid Risk", "yes");
                }
                else {
                    serviceRequest.removeDetail("Is Covid Risk");
                }
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(serviceRequest);
            });
            return new SimpleObjectProperty<JFXToggleButton>(toggleButton);
        });
        return covidRiskColumn;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton>  makeApprovedEntryColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, JFXToggleButton> covidRiskColumn = new JFXTreeTableColumn<>("Approved to Enter");
        covidRiskColumn.setPrefWidth(100);
        covidRiskColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, JFXToggleButton> param) -> {
            JFXToggleButton toggleButton = new JFXToggleButton();
            toggleButton.setSelected(param.getValue().getValue().getServiceRequest().getDetailNames().contains("Approved to Enter"));
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                ServiceRequest serviceRequest = param.getValue().getValue().getServiceRequest();
                if (newValue) {
                    serviceRequest.addDetail("Approved to Enter", "yes");
                }
                else {
                    serviceRequest.removeDetail("Approved to Enter");
                }
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(serviceRequest);
            });
            return new SimpleObjectProperty<JFXToggleButton>(toggleButton);
        });
        return covidRiskColumn;
    }

    private JFXTreeTableColumn<ServiceRequestTableEntry, JFXTextField>  makeAssignedToColumn() {
        JFXTreeTableColumn<ServiceRequestTableEntry, JFXTextField> assignedTo = new JFXTreeTableColumn<>("Assigned To");
        assignedTo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestTableEntry, JFXTextField> param) -> {
            JFXTextField textField = new JFXTextField();
            ServiceRequest serviceRequest = param.getValue().getValue().getServiceRequest();
            textField.setText(serviceRequest.getAssignment());
            JFXAutoCompletePopup<String> assignmentAutocompletePopup = new JFXAutoCompletePopup<>();
            assignmentAutocompletePopup.getSuggestions().addAll(getAllUserNames());
            assignmentAutocompletePopup.setSelectionHandler(event -> {
                textField.setText(event.getObject());
            });

            textField.textProperty().addListener(observable -> {
                assignmentAutocompletePopup.filter(string -> string.toLowerCase().contains(textField.getText().toLowerCase()));
                if (assignmentAutocompletePopup.getFilteredSuggestions().isEmpty() || textField.getText().isEmpty()) {
                    assignmentAutocompletePopup.hide();
                } else {
                    assignmentAutocompletePopup.show(textField);
                }
            });
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                serviceRequest.setAssignment(newValue);
                DBServiceRequest dbServiceRequest = new DBServiceRequest();
                dbServiceRequest.updateServiceRequest(serviceRequest);
                requestSection.setPredicate(assignmentPredicate().call(filterField.getText()).and(completePredicate().call(showCompleteToggle.isSelected())));
            });
            return new SimpleObjectProperty<>(textField);
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

}

