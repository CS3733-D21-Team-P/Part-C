package edu.wpi.p.views;

import edu.wpi.p.App;
import edu.wpi.p.database.DBEmployee;
import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.UserFromDB;
import edu.wpi.p.database.rowdata.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.util.List;

public class EmployeeLog {
    public TextField tfname;
    public Button addEmployeeBtn;
    public Button deleteEmployeeBtn;
    public TableView<Employee> employeeDataTableView;
    public TableColumn<Employee, String> NameCol;
    public TableColumn<Employee, String> EmployeeIDCol;
    public TableColumn<Employee, String> PositionCol;
    public TableColumn<Employee, String> SalaryCol;
    public Button homeButton;
    public TextField tfemployeeid;
    public TextField tfsalary;
    public TextField tfposition;
    public TextField tfassignedsr;

    private final DBEmployee dbemployee = new DBEmployee();
    private List<Employee> employeeDataList;

    @FXML
    private void initialize() throws Exception {

        employeeDataList = dbemployee.getEmployees();

        NameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Name"));
        EmployeeIDCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("EmployeeID"));
        PositionCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Position"));
        SalaryCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("Salary"));

        //load in the edge data
        employeeDataTableView.setItems(getEmployeeData());
        employeeDataTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employeeDataTableView.getSelectionModel().setCellSelectionEnabled(true);
        employeeDataTableView.setEditable(true);
        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        EmployeeIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        SalaryCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private ObservableList<Employee> getEmployeeData(){
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for (Employee n : employeeDataList){
            employees.add(n);
        }
        return employees;
    }

    public void EditName(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
        employee.setName(cellEditEvent.getNewValue());

        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        Employee employee1 = employeeDataTableView.getItems().get(userRow);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.updateEmployee(employee1);
    }

    public void EditEmployeeID(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
        employee.setEmployeeID(cellEditEvent.getNewValue());

        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        Employee employee1 = employeeDataTableView.getItems().get(userRow);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.updateEmployee(employee1);
    }

    public void EditPosition(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
        employee.setPosition(cellEditEvent.getNewValue());

        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        Employee employee1 = employeeDataTableView.getItems().get(userRow);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.updateEmployee(employee1);
    }

    public void EditSalary(TableColumn.CellEditEvent<Employee, String> cellEditEvent) {
        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
        employee.setSalary(cellEditEvent.getNewValue());

        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        Employee employee1 = employeeDataTableView.getItems().get(userRow);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.updateEmployee(employee1);
    }

    public void addEmployeeAc(ActionEvent actionEvent) {
        Employee employee = new Employee(tfname.getText(), tfemployeeid.getText(),tfposition.getText(),tfsalary.getText(),tfassignedsr.getText());
        employeeDataTableView.getItems().add(employee);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.addEmployee(employee);
    }

    public void deleteEmployeeAc(ActionEvent actionEvent) {
        TablePosition userpos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userpos.getRow();
        Employee user = employeeDataTableView.getItems().get(userRow);
        DBEmployee dbemployee = new DBEmployee();
        dbemployee.removeEmployee(user.getEmployeeID());
        employeeDataTableView.getItems().removeAll(employeeDataTableView.getSelectionModel().getSelectedItem());
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
        Employee employee = employeeDataTableView.getSelectionModel().getSelectedItem();
        employee.setAssignedSR(cellEditEvent.getNewValue());

        TablePosition userPos = employeeDataTableView.getSelectionModel().getSelectedCells().get(0);
        int userRow = userPos.getRow();
        Employee employee1 = employeeDataTableView.getItems().get(userRow);

        DBEmployee dbemployee = new DBEmployee();
        dbemployee.updateEmployee(employee1);
    }
}
