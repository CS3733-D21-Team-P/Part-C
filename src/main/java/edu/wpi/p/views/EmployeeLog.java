package edu.wpi.p.views;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.App;
import edu.wpi.p.database.DBEmployee;
import edu.wpi.p.database.rowdata.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeLog {
    public TextField tfname;
    public Button addEmployeeBtn;
    public Button deleteEmployeeBtn;
    public TableColumn<Employee, String> NameCol;
    public TableColumn<Employee, String> EmployeeIDCol;
    public TableColumn<Employee, String> PositionCol;
    public TableColumn<Employee, String> SalaryCol;
    public TableColumn<Employee, String> assignedsrCol;
    public Button homeButton;
    public TextField tfemployeeid;
    public TextField tfsalary;
    public TextField tfposition;
    public TextField tfassignedsr;

    private final DBEmployee dbemployee = new DBEmployee();
    public JFXTreeTableView employeeDataTableView;
    private List<Employee> employeeDataList;
    private List<Employee> removableList = new ArrayList<>();

    @FXML
    private void initialize() {

        JFXTreeTableColumn<Employee, String> employeeid = new JFXTreeTableColumn<>("Employee ID");
        employeeid.setPrefWidth(240);
        employeeid.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getEmployeeID());
            }
        });

        JFXTreeTableColumn<Employee, String> name = new JFXTreeTableColumn<>("Name");
        name.setPrefWidth(240);
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getName());
            }
        });

        JFXTreeTableColumn<Employee, String> position = new JFXTreeTableColumn<>("Position");
        position.setPrefWidth(240);
        position.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getPosition());
            }
        });

        JFXTreeTableColumn<Employee, String> salary = new JFXTreeTableColumn<>("Salary");
        salary.setPrefWidth(240);
        salary.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getSalary());
            }
        });

        JFXTreeTableColumn<Employee, String> assignedsr = new JFXTreeTableColumn<>("Assigned SR");
        assignedsr.setPrefWidth(240);
        assignedsr.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().getAssignedSR());
            }
        });

        employeeDataList = dbemployee.getEmployees();
        ObservableList<Employee> users = FXCollections.observableArrayList();
        for (Employee user : employeeDataList) {
            users.add(new Employee(user.getName(), user.getEmployeeID(), user.getPosition(), user.getSalary(), user.getAssignedSR()));
        }

        employeeDataTableView.setEditable(true);
        name.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        name.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> currentEditingUser = employeeDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setName(event.getNewValue());
                dbemployee.removeEmployee(currentEditingUser.getValue().getEmployeeID());
                dbemployee.addEmployee(currentEditingUser.getValue());
            }
        });

        position.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        position.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> currentEditingUser = employeeDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setPosition(event.getNewValue());
                dbemployee.removeEmployee(currentEditingUser.getValue().getEmployeeID());
                dbemployee.addEmployee(currentEditingUser.getValue());
            }
        });

        salary.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        salary.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> currentEditingUser = employeeDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setSalary(event.getNewValue());
                dbemployee.removeEmployee(currentEditingUser.getValue().getEmployeeID());
                dbemployee.addEmployee(currentEditingUser.getValue());
            }
        });

        assignedsr.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        assignedsr.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Employee, String> event) {
                TreeItem<Employee> currentEditingUser = employeeDataTableView.getTreeItem(event.getTreeTablePosition().getRow());
                currentEditingUser.getValue().setAssignedSR(event.getNewValue());
                dbemployee.removeEmployee(currentEditingUser.getValue().getEmployeeID());
                dbemployee.addEmployee(currentEditingUser.getValue());
            }
        });

        final TreeItem<Employee> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        employeeDataTableView.getColumns().setAll(name, employeeid, position, salary, assignedsr);
        employeeDataTableView.setRoot(root);
        employeeDataTableView.setShowRoot(false);

        employeeDataTableView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<Employee>>(){
            @Override
            public void changed(ObservableValue<? extends TreeItem<Employee>>
                                        observable, TreeItem<Employee> oldValue, TreeItem<Employee> newValue) {
                if (!(oldValue == newValue)) {
                    TreeTableView.TreeTableViewSelectionModel<Employee> sm = employeeDataTableView.getSelectionModel();
                    Employee n = sm.getSelectedItem().getValue(); //selected node
                    saveSelectedUser(n);
                }
            }
        });


//        NameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Name"));
//        EmployeeIDCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("EmployeeID"));
//        PositionCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Position"));
//        SalaryCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Salary"));
//        assignedsrCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("AssignedSR"));
//
//        //load in the edge data
//        employeeDataTableView.setItems(getEmployeeData());
//        employeeDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        employeeDataTableView.getSelectionModel().setCellSelectionEnabled(true);
//        employeeDataTableView.setEditable(true);
//        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        EmployeeIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        PositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        SalaryCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void saveSelectedUser(Employee n) {
        removableList.add(n);
        for (int i = 0; i < removableList.size(); i++) {
            removableList.remove(i);
        }
        removableList.add(n);
    }

    private ObservableList<Employee> getEmployeeData(){
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for (Employee n : employeeDataList){
            employees.add(n);
        }
        return employees;
    }

    public void EditName(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
//        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
//        employee.setName(cellEditEvent.getNewValue());
//
//        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        Employee employee1 = employeeDataTableView.getItems().get(userRow);
//
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.updateEmployee(employee1);
    }

    public void EditEmployeeID(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
//        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
//        employee.setEmployeeID(cellEditEvent.getNewValue());
//
//        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        Employee employee1 = employeeDataTableView.getItems().get(userRow);
//
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.updateEmployee(employee1);
    }

    public void EditPosition(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
//        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
//        employee.setPosition(cellEditEvent.getNewValue());
//
//        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        Employee employee1 = employeeDataTableView.getItems().get(userRow);
//
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.updateEmployee(employee1);
    }

    public void EditSalary(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
//        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
//        employee.setSalary(cellEditEvent.getNewValue());
//
//        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        Employee employee1 = employeeDataTableView.getItems().get(userRow);
//
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.updateEmployee(employee1);
    }

    public void addEmployeeAc(ActionEvent actionEvent) {
        Employee employee = new Employee(tfname.getText(), tfemployeeid.getText(),tfposition.getText(),tfsalary.getText(),tfassignedsr.getText());
        dbemployee.addEmployee(employee);
        initialize();
    }

    public void deleteEmployeeAc(ActionEvent actionEvent) {
        dbemployee.removeEmployee(removableList.get(0).getEmployeeID());
        initialize();
//        TablePosition userpos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userpos.getRow();
//        Employee user = employeeDataTableView.getItems().get(userRow);
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.removeEmployee(user.getEmployeeID());
//        employeeDataTableView.getItems().removeAll(employeeDataTableView.getSelectionModel().getSelectedItem());
    }

    public void homeButtonAc(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/p/fxml/HomePage.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void EditAssignedSR(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
//        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
//        employee.setAssignedSR(cellEditEvent.getNewValue());
//
//        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
//        int userRow = userPos.getRow();
//        Employee employee1 = employeeDataTableView.getItems().get(userRow);
//
//        DBEmployee dbemployee = new DBEmployee();
//        dbemployee.updateEmployee(employee1);
    }
}
