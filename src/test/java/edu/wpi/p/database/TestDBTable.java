package edu.wpi.p.database;

import edu.wpi.p.AStar.Node;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import edu.wpi.p.database.rowdata.Edge;
import edu.wpi.p.database.rowdata.ServiceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDBTable {
    private static final String NODE_NAME = "NODES";
    private static final String EDGE_NAME = "EDGES";
    private DBTable dbTable;

    @BeforeAll
    static void setup() {
        DatabaseInterface.init(false);

    }

    @BeforeEach
    void setupEach() throws Exception {
        if (DatabaseInterface.getTableNames().contains(NODE_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + NODE_NAME);
        }
        if (DatabaseInterface.getTableNames().contains(EDGE_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + EDGE_NAME);
        }
        dbTable = new DBTable();
        CSVData nodeData = CSVHandler.readCSVString("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
        CSVData edgeData = CSVHandler.readCSVString("edgeID,startNode,endNode");
        CSVDBConverter.tableFromCSVData(nodeData, edgeData);
    }

    @AfterAll
    static void cleanup() {
        if (DatabaseInterface.getTableNames().contains(NODE_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + NODE_NAME);
        }
        if (DatabaseInterface.getTableNames().contains(EDGE_NAME)) {
            DatabaseInterface.executeUpdate("DROP TABLE " + EDGE_NAME);
        }
    }

    @Test
    void testCreation() {
        List<String> dbNames = DatabaseInterface.getTableNames();
        boolean tableNameExists = dbNames.contains(NODE_NAME);
        tableNameExists |= dbNames.contains(EDGE_NAME);

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
    void testInsertNode() {
        Node n = new Node("test node 1", "TEST_NODE_1", 1, 2, "1", "buildling", "nodetype", "short name");
        dbTable.addNode(n);
        List<List<String>> rows = DatabaseInterface.getAllFromTable(NODE_NAME);
        assertEquals(1, rows.size());

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(NODE_NAME);
        int nodeID = indexOfColumnByName(dbColumns, "NODEID");
        String ID = rows.get(0).get(nodeID);
        assertEquals("TEST_NODE_1", ID);
    }

    @Test
    void testInsertEdge() {
        Node n = new Node("test node 1", "TEST_NODE_1", 1, 2, "1", "buildling", "nodetype", "short name");
        Node n2 = new Node("test node 2", "TEST_NODE_2", 3, 4, "1", "buildling", "nodetype", "short name");
        dbTable.addNode(n);
        dbTable.addNode(n2);
        dbTable.addEdge("TEST_EDGE_1", "TEST_NODE_1", "TEST_NODE_2");
        List<List<String>> rows = DatabaseInterface.getAllFromTable(EDGE_NAME);
        assertEquals(1, rows.size());

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(EDGE_NAME);
        System.out.println("dbColumns is: " + dbColumns);
        int edgeID = indexOfColumnByName(dbColumns, "EDGEID");
        String ID = rows.get(0).get(edgeID);
        assertEquals("TEST_EDGE_1", ID);
    }

    @Test
    void testGet() {
        Node n = new Node("test node 1", "TEST_NODE_1", 1, 2, "1", "buildling", "nodetype", "short name");
        Node n2 = new Node("test node 2", "TEST_NODE_2", 3, 4, "1", "buildling", "nodetype", "short name");
        dbTable.addNode(n);
        dbTable.addNode(n2);
        dbTable.addEdge("TEST_EDGE_1", "TEST_NODE_1", "TEST_NODE_2");

        List<Node> nodes = dbTable.getNodes();
        assertEquals(2, nodes.size());

        assertEquals("test node 1", nodes.get(0).getName());
        assertEquals("TEST_NODE_1", nodes.get(0).getId());
        assertEquals(1, nodes.get(0).getXcoord());
        assertEquals(2, nodes.get(0).getYcoord());
        assertEquals("buildling", nodes.get(0).getBuilding());
        assertEquals("nodetype", nodes.get(0).getType());
        assertEquals("short name", nodes.get(0).getShortName());


        assertEquals("test node 2", nodes.get(1).getName());
        assertEquals("TEST_NODE_2", nodes.get(1).getId());
        assertEquals(3, nodes.get(1).getXcoord());
        assertEquals(4, nodes.get(1).getYcoord());
        assertEquals("buildling", nodes.get(1).getBuilding());
        assertEquals("nodetype", nodes.get(1).getType());
        assertEquals("short name", nodes.get(1).getShortName());

    }

    @Test
    void testUpdateEdge() {
        Node n = new Node("test node 1", "TEST_NODE_1", 1, 2, "1", "buildling", "nodetype", "short name");
        dbTable.addNode(n);
        n.setName("new name");
        n.setBuilding("new building");
        n.setXcoord(10);
        n.setShortName("new short name");
        dbTable.updateNode(n);

        List<Node> nodes = dbTable.getNodes();
        assertEquals(1, nodes.size());

        assertEquals("new name", nodes.get(0).getName());
        assertEquals("TEST_NODE_1", nodes.get(0).getId());
        assertEquals(10, nodes.get(0).getXcoord());
        assertEquals(2, nodes.get(0).getYcoord());
        assertEquals("new building", nodes.get(0).getBuilding());
        assertEquals("nodetype", nodes.get(0).getType());
        assertEquals("new short name", nodes.get(0).getShortName());
    }

    @Test
    void testRemoveNode() {
        Node n = new Node("test node 1", "TEST_NODE_1", 1, 2, "1", "buildling", "nodetype", "short name");
        Node n2 = new Node("test node 2", "TEST_NODE_2", 3, 4, "1", "buildling", "nodetype", "short name");
        dbTable.addNode(n);
        dbTable.addNode(n2);
        dbTable.addEdge("TEST_EDGE_1", "TEST_NODE_1", "TEST_NODE_2");

        assertEquals(2, dbTable.getNodes().size());
        dbTable.removeNode("TEST_NODE_1");

        List<Node> nodes = dbTable.getNodes();
        assertEquals(1, nodes.size());
        assertEquals("TEST_NODE_2", nodes.get(0).getId());
        assertEquals(0, dbTable.getEdges().size());
    }
}
