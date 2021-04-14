package edu.wpi.p.database;

import AStar.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DBTable {
    private String nodeTable = "NODES";
    private String edgeTable = "EDGES";
    private List<DBColumn> nodeColumns;
    private List<DBColumn> edgeColumns;
    private List<Node> nodeList;
    private List<List<String>> edgeData;

    public DBTable() {

    }
    public DBTable(List<DBColumn> nodeColumns, List<DBColumn> edgeColumns) {
        this.nodeColumns = nodeColumns;
        this.edgeColumns = edgeColumns;

        this.createTables();

    }

    private void createTables() {
        DatabaseInterface.executeUpdate("DROP TABLE " + nodeTable);
        DatabaseInterface.executeUpdate("DROP TABLE " + edgeTable);
        DatabaseInterface.createTableIfNotExists(nodeTable, this.nodeColumns);
        DatabaseInterface.createTableIfNotExists(edgeTable, this.edgeColumns);
    }

    public void updateNode(Node n) {
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET xcoord = "+n.getyCoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET ycoord = "+n.getxCoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET floor = '"+n.getFloor()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET building = '"+n.getBuilding()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET nodeType = '"+n.getType()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET longName = '"+n.getName()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET shortName = '"+n.getShortName()+"' WHERE nodeID = '"+n.getId()+"'");
    }

    public void addNode(Node n) {
        String insertValue = "'" + n.getId() + "', " + n.getxCoord() + ", " + n.getyCoord() + ", '" + n.getFloor() + "', '" + n.getBuilding() + "', '" + n.getType() + "', '" + n.getName() + "', '" + n.getShortName() + "'";
        DatabaseInterface.insertIntoTable(nodeTable, insertValue);
    }

    public void addEdge(String edgeID, String nodeIDOne, String nodeIDTwo) {
        String insertValue = "'" + edgeID + "', '" + nodeIDOne + "', '" + nodeIDTwo + "'";
        DatabaseInterface.insertIntoTable(edgeTable, insertValue);
    }

    public void removeEdge(String nodeIDOne, String nodeIDTwo) {
        String removeCommand = "DELETE FROM " + edgeTable +" WHERE STARTNODE='" + nodeIDOne + "' AND ENDNODE='" + nodeIDTwo + "'";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void removeNode(String nodeID) {
        String removeNodeCommand = "DELETE FROM " + nodeTable +" WHERE NODEID='" + nodeID + "'";
        DatabaseInterface.executeUpdate(removeNodeCommand);
        String removeEdgesCommand = "DELETE FROM " + edgeTable +" WHERE STARTNODE='" + nodeID + "' OR ENDNODE='" + nodeID + "'";
        DatabaseInterface.executeUpdate(removeEdgesCommand);
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
    public List<Node> getNodes() {
        //parse L1Nodes
        List<List<String>> nodeData = DatabaseInterface.getAllFromTable(nodeTable);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(nodeTable);
        //L1Nodes columns
        int nodeID = indexOfColumnByName(dbColumns, "nodeID");
        int xcoord = indexOfColumnByName(dbColumns, "xcoord");
        int ycoord = indexOfColumnByName(dbColumns, "ycoord");
        int floor = indexOfColumnByName(dbColumns, "floor");
        int building = indexOfColumnByName(dbColumns, "building");
        int nodeType = indexOfColumnByName(dbColumns, "nodeType");
        int longName = indexOfColumnByName(dbColumns, "longName");
        int shortName = indexOfColumnByName(dbColumns, "shortName");

        //create nodes
        List<Node> nodes = new ArrayList<>(nodeData.size());
        for(int i = 1; i < nodeData.size(); i++) {
            List<String> nodeString = nodeData.get(i);

            Node node = new Node(
                    nodeString.get(longName),
                    nodeString.get(nodeID),
                    Integer.parseInt(nodeString.get(xcoord)),
                    Integer.parseInt(nodeString.get(ycoord)),
                    nodeString.get(floor),
                    nodeString.get(building),
                    nodeString.get(nodeType),
                    nodeString.get(shortName));
            nodes.add(node);
        }

        return nodes;
    }

    public List<List<String>> getEdgeData() {
        //parse L1Edges
        this.edgeData = DatabaseInterface.getAllFromTable(edgeTable);
//        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Edges.csv"))) {
//            while (scanner.hasNextLine()) {
//                this.edgeData.add(getNodeString(scanner.nextLine()));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        return edgeData;
    }

//    private List<String> getNodeString(String line) {
//        List<String> values = new ArrayList<>();
//        try (Scanner rowScanner = new Scanner(line)) {
//            rowScanner.useDelimiter(",");
//            while (rowScanner.hasNext()) {
//                values.add(rowScanner.next());
//            }
//        }
//        return values;
//    }


}
