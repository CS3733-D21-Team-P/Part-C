package edu.wpi.p.database;

import edu.wpi.p.database.rowdata.Employee;

import java.util.ArrayList;
import java.util.List;

public class DBEmployee {
    String EmployeeTable = "EmployeeTable";
    private List<DBColumn> columns = new ArrayList<>();

    private static DBEmployee instance;

    /**
     * Instance getter for the singleton
     * @return the singleton instance of DBEmployee
     */
    public static DBEmployee getInstance() {
        if (instance == null) {
            instance = new DBEmployee();
        }
        return instance;
    }

    public DBEmployee(){
        init();
        createEmployeeTable(columns);
    }

    private void init(){
        columns.add(new DBColumn("EmployeeID", "varchar(256)", ""));
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Position", "varchar(256)", ""));
        columns.add(new DBColumn("Salary", "varchar(256)", ""));
        columns.add(new DBColumn("AssignedSR", "varchar(256)", ""));

    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearEmployeeTable();
        }
        DatabaseInterface.createTableIfNotExists(EmployeeTable, columns);
    }

    public boolean createEmployeeTable(List<DBColumn> columns) {
        this.columns = columns;
        return DatabaseInterface.createTableIfNotExists(EmployeeTable, columns);
    }

    public void clearEmployeeTable() {
        DatabaseInterface.executeUpdate("DROP TABLE " + EmployeeTable);
    }

    public void addEmployee(Employee e) {
        String insertValue = "'" + e.getEmployeeID() + "', '" + e.getName() + "', '" + e.getPosition() + "', '" + e.getSalary() + "', '" + e.getAssignedSR() + "'";
        DatabaseInterface.insertIntoTable(EmployeeTable, insertValue);
    }

    public void removeEmployee(String employeeID) {
        String removeCommand = "DELETE FROM " + EmployeeTable +" WHERE EmployeeID='" + employeeID + "'";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void updateEmployee(Employee e) {
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET Name = "+e.getName()+" WHERE EmployeeID = '"+e.getEmployeeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET Position = '"+e.getPosition()+"' WHERE EmployeeID = '"+e.getEmployeeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET Salary = '"+e.getSalary()+"' WHERE EmployeeID = '"+e.getEmployeeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET AssignedSR = '"+e.getAssignedSR()+"' WHERE EmployeeID = '"+e.getEmployeeID()+"'");
    }

    /**
     * A helper function to get the index in the list of the column with the given name
     * @param columns List of column
     * @param targetName Name of column to get the index of in the list
     * @return Index of the column with name targetName in columns
     */
    private int indexOfColumnByName(List<DBColumn> columns, String targetName) {
        int i = 0;
        for(DBColumn c : columns) {
            if(c.getName().equals(targetName.toUpperCase())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public List<Employee> getEmployees() {
        List<List<String>> employeeData = DatabaseInterface.getAllFromTable(EmployeeTable);
        List<DBColumn> dbColumns = DatabaseInterface.getColumns(EmployeeTable);
        if (employeeData == null || dbColumns == null) {
            return null;
        }

        int Name = indexOfColumnByName(dbColumns, "Name");
        int EmployeeID = indexOfColumnByName(dbColumns, "EmployeeID");
        int Position = indexOfColumnByName(dbColumns, "Position");
        int Salary = indexOfColumnByName(dbColumns, "Salary");
        int AssignedSR = indexOfColumnByName(dbColumns, "AssignedSR");


        //create Users
        List<Employee> employeesFromDBS = new ArrayList<>(employeeData.size());
        for(int i = 1; i < employeeData.size(); i++) {
            List<String> employeeString = employeeData.get(i);

            Employee employeeFromDB = new Employee(
                    employeeString.get(Name),
                    employeeString.get(EmployeeID),
                    employeeString.get(Position),
                    employeeString.get(Salary),
                    employeeString.get(AssignedSR));
            employeesFromDBS.add(employeeFromDB);
        }

        return employeesFromDBS;
    }
}