package edu.wpi.p.database;

import AStar.Node;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.CSVHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDatabaseInterface {

    @BeforeAll
    static void setup() {
        DatabaseInterface.init();
    }
    @Test
    void testTableCreation() {

        List<DBColumn> columns = new ArrayList<>(3);
        columns.add(new DBColumn("id", "INT", ""));
        columns.add(new DBColumn("lastName", "VARCHAR(64)", ""));
        columns.add(new DBColumn("firstName", "VARCHAR(64)", ""));
        DatabaseInterface.createTableIfNotExists("people", columns);
        DatabaseInterface.printTables();
        System.out.println();
        DatabaseInterface.printColumnNames("people");
        System.out.println();
        DatabaseInterface.insertIntoTable("PEOPLE", "1, 'foo', 'bar'");
        DatabaseInterface.getAllFromTable("PEOPLE");
    }

    @Test
    void testCSVDBInterface() {
        try {
            CSVData nodeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Nodes.csv");
            CSVData edgeData = CSVHandler.readCSVFile("src/main/java/AStar/L1Edges.csv");
            DBTable table = CSVDBConverter.tableFromCSVData(nodeData, edgeData);
            CSVData newNodes = CSVDBConverter.csvNodesFromTable(table);
            CSVData newEdges = CSVDBConverter.csvEdgesFromTable(table);
            CSVHandler.writeCSVData(newNodes, "newNodes.csv");
            CSVHandler.writeCSVData(newEdges, "newEdges.csv");

            System.out.println("Removing edge test: ");
            System.out.println("Edge count: " + table.getEdgeData().size());
            table.removeEdge("CCONF002L1", "WELEV00HL1");
            System.out.println("Edge count: " + table.getEdgeData().size());
            System.out.println("Removing node test");
            System.out.println("Node count: " + table.getNodes().size());
            System.out.println("Edge count: " + table.getEdgeData().size());
            table.removeNode("CCONF002L1");
            System.out.println("Edge count: " + table.getEdgeData().size());
            System.out.println("Node count: " + table.getNodes().size());
            List<Node> nodes = table.getNodes();
            Node test = nodes.get(0);
            test.setxCoord(0);
            test.setyCoord(0);
            test.setName("a test node");
            table.updateNode(test);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }


}
