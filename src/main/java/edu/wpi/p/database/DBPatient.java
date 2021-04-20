package edu.wpi.p.database;

import java.util.ArrayList;
import java.util.List;

public class DBPatient {
    String PatientTable = "PatientTable";
    private List<DBColumn> columns = new ArrayList<>();

    private void init(){
        columns.add(new DBColumn("PatientID", "varchar(256)", ""));
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Request", "varchar(256)", ""));
    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearPatientTable();
        }
        DatabaseInterface.createTableIfNotExists(PatientTable, columns);
    }

    public boolean createPatientTable(List<DBColumn> columns) {
        this.columns = columns;
        return DatabaseInterface.createTableIfNotExists(PatientTable, columns);
    }

    public void clearPatientTable() {
        DatabaseInterface.executeUpdate("DROP TABLE " + PatientTable);
    }

    public void addPatient(Patient p) {
        String insertValue = "'" + p.getPatientID() + "', '" + p.getName() + "', '" + p.getRequest() + "'";
        DatabaseInterface.insertIntoTable(PatientTable, insertValue);
    }

    public void removePatient(String patientID) {
        String removeCommand = "DELETE FROM " + PatientTable +" WHERE PatientID='" + patientID + "";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void updatePatient(Patient p) {
        DatabaseInterface.executeUpdate("UPDATE " + PatientTable + " SET Name = "+p.getName()+" WHERE nodeID = '"+p.getPatientID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + PatientTable + " SET Position = '"+p.getRequest()+"' WHERE nodeID = '"+p.getPatientID()+"'");
    }
}
