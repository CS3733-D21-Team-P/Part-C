package edu.wpi.p.database;

import java.util.ArrayList;
import java.util.List;

public class DBEmployee {
    String EmployeeTable = "EmployeeTable";
    private List<DBColumn> columns = new ArrayList<>();

    private void init(){
        columns.add(new DBColumn("EmployeeID", "varchar(256)", ""));
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Position", "varchar(256)", ""));
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
        String insertValue = "'" + e.getEmployeeID() + "', '" + e.getName() + "', '" + e.getPosition() + "'";
        DatabaseInterface.insertIntoTable(EmployeeTable, insertValue);
    }

    public void removeEmployee(String employeeID) {
        String removeCommand = "DELETE FROM " + EmployeeTable +" WHERE EmployeeID='" + employeeID + "";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void updateEmployee(Employee e) {
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET Name = "+e.getName()+" WHERE nodeID = '"+e.getEmployeeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + EmployeeTable + " SET Position = '"+e.getPosition()+"' WHERE nodeID = '"+e.getEmployeeID()+"'");
    }
}