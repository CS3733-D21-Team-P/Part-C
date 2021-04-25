package edu.wpi.p.database;



import edu.wpi.p.database.rowdata.Edge;
import edu.wpi.p.database.rowdata.ServiceRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class DBServiceRequest {

    private String serviceRequestTable = "SRT";
    private List<DBColumn> serviceRequestColumn;


    /**
     * Standard constructor, nothing fancy
     */
    public DBServiceRequest(){
        createTable(false);
    }

    /**
     * Constructor that can be used to set the table name
     * Should only be used for testing
     * @param tableName Name of the database table for the service requests
     */
    public DBServiceRequest(String tableName) {
        serviceRequestTable = tableName;
        createTable(false);
    }


    /**
     * Creates the list of columns with their types
     */
    private void init() {
        serviceRequestColumn = new ArrayList<>();
        serviceRequestColumn.add(new DBColumn("name", "varchar(256)", ""));
//        serviceRequestColumn.add(new DBColumn("type", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("ID", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("location", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("assignment", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("complete", "BOOLEAN", ""));
//        serviceRequestColumn.add(new DBColumn("data", "varchar(2048)", ""));
    }

    /**
     * Removes the table from the database
     */
    public void clearServiceRequest() {
        DatabaseInterface.executeUpdate("DROP TABLE " + serviceRequestTable);
    }

    /**
     * Creates the table if it doesn't already exist
     * Can optionally clear the table first
     * @param doClear true if you want the data to be removed from the table
     */
    private void createTable(boolean doClear) {
        if (doClear) {
            clearServiceRequest();
        }
        init();
        DatabaseInterface.createTableIfNotExists(serviceRequestTable, this.serviceRequestColumn);

    }

    /**
     * Changes the values of a service request in the database
     * Assumes the id of the passed service request matches the ID of a service request already in the database
     * Keeps the ID the same, updates all the other values
     * @param s Service request that contains the same ID as one in the database, updates the DB so that the other values match
     */
    public void updateServiceRequest(ServiceRequest s) {
        DatabaseInterface.updateDBRow(serviceRequestTable, "ID", s.getID(), s);
//        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET NAME = '" + s.getName() + "' WHERE ID = '" + s.getID() + "'");
//        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET LOCATION = '" + s.getLocation() + "' WHERE ID = '" + s.getID() + "'");
//        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET ASSIGNMENT = '" + s.getAssignment() + "' WHERE ID = '" + s.getID() + "'");
//        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET COMPLETE = " + (s.getCompleted() ? "true" : "false") + " WHERE ID = '" + s.getID() + "'");
    }


    /**
     * Adds a service request to the database
     * @param serviceRequest new service request to add to the database
     */
    public void addServiceRequest(ServiceRequest serviceRequest) {
        DatabaseInterface.insertDBRowIntoTable(serviceRequestTable, serviceRequest);
    }

    /**
     * Removes the service request with the given ID from the table
     * @param serviceRequestID ID of the service request you want to remove from the database
     */
    public void removeServiceRequest(String serviceRequestID) {
        String removeServiceRequestCommand = "DELETE FROM " + serviceRequestTable + " WHERE ID='" + serviceRequestID + "'";
        DatabaseInterface.executeUpdate(removeServiceRequestCommand);
    }

    /**
     * Gets all of the service requests in the database
     * Returns them as a list of ServiceRequest objects
     * @return list of service request objects representing all the data in the table
     */
    public List<ServiceRequest> getServiceRequests() {
        List<List<String>> requestData = DatabaseInterface.getAllFromTable(serviceRequestTable);

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(serviceRequestTable);
        List<ServiceRequest> serviceRequests = new ArrayList<>(requestData.size());

        for (List<String> row : requestData) {
            ServiceRequest sR = new ServiceRequest();
            for (int i = 0; i < row.size(); i++) {
                if (dbColumns.get(i).getType().equals("BOOLEAN")) {
                    sR.addValue(dbColumns.get(i).getName(), Boolean.parseBoolean(row.get(i)));
                }
                else {
                    sR.addValue(dbColumns.get(i).getName(), row.get(i));
                }

            }
            serviceRequests.add(sR);
        }

        return serviceRequests;
    }




}
