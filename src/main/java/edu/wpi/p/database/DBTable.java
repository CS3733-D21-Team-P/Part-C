package edu.wpi.p.database;

import AStar.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

        this.createTables(false);

    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearNodes();
            clearEdges();
        }
        DatabaseInterface.createTableIfNotExists(nodeTable, this.nodeColumns);
        DatabaseInterface.createTableIfNotExists(edgeTable, this.edgeColumns);
    }

    public boolean createNodeTable(List<DBColumn> columns) {
        this.nodeColumns = columns;
        return DatabaseInterface.createTableIfNotExists(nodeTable, columns);
    }

    public boolean createEdgeTable(List<DBColumn> columns) {
        this.edgeColumns = columns;
        return DatabaseInterface.createTableIfNotExists(nodeTable, columns);
    }

    public void clearNodes() {
        DatabaseInterface.executeUpdate("DROP TABLE " + nodeTable);
    }

    public void clearEdges() {
        DatabaseInterface.executeUpdate("DROP TABLE " + edgeTable);
    }

    public void updateNode(Node n) {
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET xcoord = "+n.getYcoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET ycoord = "+n.getXcoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET floor = '"+n.getFloor()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET building = '"+n.getBuilding()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET nodeType = '"+n.getType()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET longName = '"+n.getName()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET shortName = '"+n.getShortName()+"' WHERE nodeID = '"+n.getId()+"'");
    }

    public void addNode(Node n) {
        String insertValue = "'" + n.getId() + "', " + n.getXcoord() + ", " + n.getYcoord() + ", '" + n.getFloor() + "', '" + n.getBuilding() + "', '" + n.getType() + "', '" + n.getName() + "', '" + n.getShortName() + "'";
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

    public void updateEdge(String edgeID, Edge edge) {
        DatabaseInterface.executeUpdate("UPDATE " + edgeTable + " SET EDGEID = "+edge.getEdgeID()+" WHERE nodeID = '"+edge.getEdgeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET STARTNODE = "+edge.getStartNode()+" WHERE nodeID = '"+edge.getEdgeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET ENDNODE = '"+edge.getEndNode()+"' WHERE nodeID = '"+edge.getEdgeID()+"'");
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
        this.edgeData = DatabaseInterface.getAllFromTable(edgeTable);
        return edgeData;
    }

    public List<Edge> getEdges() {
        List<List<String>> edgeData = this.getEdgeData();
        List<DBColumn> dbColumns = DatabaseInterface.getColumns(edgeTable);
        int edgeID = indexOfColumnByName(dbColumns, "edgeID");
        int startNode = indexOfColumnByName(dbColumns, "startNode");
        int endNode = indexOfColumnByName(dbColumns, "endNode");
        return edgeData.stream()
                .map(strings -> new Edge(strings.get(edgeID), strings.get(startNode), strings.get(endNode)))
                .collect(Collectors.toList());
    }


}
