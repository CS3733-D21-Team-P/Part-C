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
        List<DBColumn> serviceRequestColumn = new ArrayList<>();
        serviceRequestColumn.add(new DBColumn("name", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("serviceRequestID", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("location", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("whatever else", "varchar(256)", ""));
    }

    public void DBServiceRequestTable(List<DBColumn> serviceRequestColumn) {
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
        init();
        DatabaseInterface.createTableIfNotExists(serviceRequestTable, this.serviceRequestColumn);

    }

    public boolean createServiceRequestTable(List<DBColumn> columns) {
        this.serviceRequestColumn = columns;
        return DatabaseInterface.createTableIfNotExists(serviceRequestTable, columns);
    }
    public void updateServiceRequest(ServiceRequest n) {
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET serviceRequestName = " + n.getServiceRequestName() + " WHERE serviceRequestName = '" + n.getServiceRequestID() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET serviceRequestID = " + n.getServiceRequestID() + " WHERE serviceRequestID = '" + n.getServiceRequestID() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET serviceRequestLocation = '" + n.getServiceRequestLocation() + "' WHERE serviceRequestLocation = '" + n.getServiceRequestID() + "'");
    }


    public void addServiceRequest(ServiceRequest n) {
        String insertValue = "'" + n.getServiceRequestName() + "', " + n.getServiceRequestLocation() + ", " + n.getServiceRequestID() + "'";
        DatabaseInterface.insertIntoTable(serviceRequestTable, insertValue);
    }

    public void removeServiceRequest(String serviceRequestID) {
        String removeServiceRequestCommand = "DELETE FROM " + serviceRequestTable + " WHERE SERVICEREQUESTID='" + serviceRequestID + "'";
        DatabaseInterface.executeUpdate(removeServiceRequestCommand);
    }




}
