package edu.wpi.p.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServiceRequestDB {
    private static final String DB_NAME = "TEST_SERVICEREQUEST";
    private DBServiceRequest dbServiceRequest;
    @BeforeAll
    static void setup() {
        DatabaseInterface.init();

    }

    @BeforeEach
    void setupEach() {
        if (DatabaseInterface.getTableNames().contains(DB_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + DB_NAME);
        }
        dbServiceRequest = new DBServiceRequest(DB_NAME);
    }

    @AfterAll
    static void cleanup() {
        if (DatabaseInterface.getTableNames().contains(DB_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + DB_NAME);
        }
    }

    @Test
    void testCreation() {
        List<String> dbNames = DatabaseInterface.getTableNames();
        boolean tableNameExists = dbNames.contains(DB_NAME);

        assertEquals(true, tableNameExists);
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

    @Test
    void testInsert() {
        ServiceRequest sR = new ServiceRequest("test", "lobby", "lobby_test_sr_1", "Alice");
        dbServiceRequest.addServiceRequest(sR);
        List<List<String>> rows = DatabaseInterface.getAllFromTable(DB_NAME);
        assertEquals(1, rows.size());

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(DB_NAME);
        int serviceRequestID = indexOfColumnByName(dbColumns, "ID");
        String ID = rows.get(0).get(serviceRequestID);
        assertEquals("lobby_test_sr_1", ID);
    }

    @Test
    void testGet() {
        ServiceRequest sR = new ServiceRequest("test", "lobby", "lobby_test_sr_1", "Alice");
        dbServiceRequest.addServiceRequest(sR);
        sR = new ServiceRequest("test2", "stairs", "stairs_test_sr_2", "Bob");
        dbServiceRequest.addServiceRequest(sR);
        sR = new ServiceRequest("test3", "cafe", "cafe_test_sr_2", "Carol");
        sR.completed();
        dbServiceRequest.addServiceRequest(sR);

        List<ServiceRequest> serviceRequests = dbServiceRequest.getServiceRequests();
        assertEquals(3, serviceRequests.size());

        assertEquals("Alice", serviceRequests.get(0).getAssignment().getValue());
        assertEquals(false, serviceRequests.get(0).getCompleted());

        assertEquals("stairs_test_sr_2", serviceRequests.get(1).getServiceRequestID().getValue());
        assertEquals("stairs", serviceRequests.get(1).getServiceRequestLocation().getValue());

        assertEquals("test3", serviceRequests.get(2).getServiceRequestName().getValue());
        assertEquals(true, serviceRequests.get(2).getCompleted());

    }

    @Test
    void testUpdate() {
        ServiceRequest sR = new ServiceRequest("test", "lobby", "lobby_test_sr_1", "Alice");
        dbServiceRequest.addServiceRequest(sR);
        sR.setName("new name");
        sR.setLocation("stairs");
        sR.completed();
        sR.setAssignment("Bob");
        dbServiceRequest.updateServiceRequest(sR);

        List<ServiceRequest> serviceRequests = dbServiceRequest.getServiceRequests();
        assertEquals(1, serviceRequests.size());
        assertEquals("Bob", serviceRequests.get(0).getAssignment().getValue());
        assertEquals("stairs", serviceRequests.get(0).getServiceRequestLocation().getValue());
        assertEquals("new name", serviceRequests.get(0).getServiceRequestName().getValue());
        assertEquals(true, serviceRequests.get(0).getCompleted());
    }

    @Test
    void testRemove() {
        ServiceRequest sR = new ServiceRequest("test", "lobby", "lobby_test_sr_1", "Alice");
        dbServiceRequest.addServiceRequest(sR);
        sR = new ServiceRequest("test2", "stairs", "stairs_test_sr_2", "Bob");
        dbServiceRequest.addServiceRequest(sR);
        sR = new ServiceRequest("test3", "cafe", "cafe_test_sr_2", "Carol");
        sR.completed();
        dbServiceRequest.addServiceRequest(sR);
        assertEquals(3, dbServiceRequest.getServiceRequests().size());
        dbServiceRequest.removeServiceRequest("stairs_test_sr_2");

        List<ServiceRequest> serviceRequests = dbServiceRequest.getServiceRequests();
        assertEquals(2, serviceRequests.size());
        assertEquals("lobby_test_sr_1", serviceRequests.get(0).getServiceRequestID().getValue());
        assertEquals("cafe_test_sr_2", serviceRequests.get(1).getServiceRequestID().getValue());

    }
}
