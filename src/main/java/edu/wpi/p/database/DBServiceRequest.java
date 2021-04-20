package edu.wpi.p.database;



import javax.xml.ws.Service;
import java.util.ArrayList;
import java.util.List;

public class DBServiceRequest {

    private String serviceRequestTable = "SRT";
    private List<DBColumn> serviceRequestColumn;


    /**
     * private List<DBColumn> serviceRequestColumn;
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
        serviceRequestColumn.add(new DBColumn("ID", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("location", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("assignment", "varchar(256)", ""));
        serviceRequestColumn.add(new DBColumn("complete", "BOOLEAN", ""));
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

//    public boolean createServiceRequestTable(List<DBColumn> columns) {
//        this.serviceRequestColumn = columns;
//        return DatabaseInterface.createTableIfNotExists(serviceRequestTable, columns);
//    }

    /**
     * Changes the values of a service request in the database
     * Assumes the id of the passed service request matches the ID of a service request already in the database
     * Keeps the ID the same, updates all the other values
     * @param s Service request that contains the same ID as one in the database, updates the DB so that the other values match
     */
    public void updateServiceRequest(ServiceRequest s) {
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET NAME = '" + s.getServiceRequestName().getValue() + "' WHERE ID = '" + s.getServiceRequestID().getValue() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET LOCATION = '" + s.getServiceRequestLocation().get() + "' WHERE ID = '" + s.getServiceRequestID().getValue() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET ASSIGNMENT = '" + s.getAssignment().getValue() + "' WHERE ID = '" + s.getServiceRequestID().getValue() + "'");
        DatabaseInterface.executeUpdate("UPDATE " + serviceRequestTable + " SET COMPLETE = " + (s.getCompleted() ? "true" : "false") + " WHERE ID = '" + s.getServiceRequestID().getValue() + "'");
    }


    /**
     * Adds a service request to the database
     * @param serviceRequest new service request to add to the database
     */
    public void addServiceRequest(ServiceRequest serviceRequest) {
        String insertValue = "'" + serviceRequest.getServiceRequestName().getValue() + "', '" +
                serviceRequest.getServiceRequestID().getValue() + "', '" +
                serviceRequest.getServiceRequestLocation().getValue() + "', '" +
                serviceRequest.getAssignment().getValue() + "', " +
                (serviceRequest.getCompleted() ? "true" : "false");
        DatabaseInterface.insertIntoTable(serviceRequestTable, insertValue);
    }

    /**
     * Removes the service request with the given ID from the table
     * @param serviceRequestID ID of the service request you want to remove from the database
     */
    public void removeServiceRequest(String serviceRequestID) {
        String removeServiceRequestCommand = "DELETE FROM " + serviceRequestTable + " WHERE ID='" + serviceRequestID + "'";
        DatabaseInterface.executeUpdate(removeServiceRequestCommand);
    }

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

    /**
     * Gets all of the service requests in the database
     * Returns them as a list of ServiceRequest objects
     * @return list of service request objects representing all the data in the table
     */
    public List<ServiceRequest> getServiceRequests() {
        List<List<String>> requestData = DatabaseInterface.getAllFromTable(serviceRequestTable);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(serviceRequestTable);

        int name = indexOfColumnByName(dbColumns, "name");
        int serviceRequestID = indexOfColumnByName(dbColumns, "ID");
        int location = indexOfColumnByName(dbColumns, "location");
        int assignment = indexOfColumnByName(dbColumns, "assignment");
        int complete = indexOfColumnByName(dbColumns, "complete");

        List<ServiceRequest> requests = new ArrayList<>(requestData.size());
        for (List<String> row : requestData) {
            ServiceRequest sR = new ServiceRequest(row.get(name),
                    row.get(location), row.get(serviceRequestID), row.get(assignment));
            boolean completed = Boolean.parseBoolean(row.get(complete));
            if(completed) {
                sR.completed();
            }
            requests.add(sR);
        }

        return requests;
    }




}
