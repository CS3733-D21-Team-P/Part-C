package edu.wpi.p.database;

import java.util.ArrayList;
import java.util.List;

public class DBServiceRequest {

    private String serviceRequestTable = "SRT";
    private List<DBColumn> serviceRequestColumn;

    public void serviceRequestTable(List<DBColumn> serviceRequestColumn){
        this.serviceRequestColumn = serviceRequestColumn;

    }


    private void init() {
        List<DBColumn> columns = new ArrayList<>();
        columns.add(new DBColumn("name", "varchar(256)", ""));
        columns.add(new DBColumn("id", "varchar(256)", ""));
        columns.add(new DBColumn("location", "varchar(256)", ""));
        columns.add(new DBColumn("whatever else", "varchar(256)", ""));
    }

    public void DBServiceRequestTable(List<DBColumn> nodeColumns, List<DBColumn> edgeColumns) {
        this.serviceRequestColumn = serviceRequestColumn;


        this.createTables(false);

    }
    public void clearServiceRequest() {
        DatabaseInterface.executeUpdate("DROP TABLE " + serviceRequestTable);
    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearServiceRequest();
        }
        DatabaseInterface.createTableIfNotExists(serviceRequestTable, this.serviceRequestColumn);

    }

    public boolean createNodeTable(List<DBColumn> columns) {
        this.serviceRequestColumn = columns;
        return DatabaseInterface.createTableIfNotExists(serviceRequestTable, columns);
    }



}
