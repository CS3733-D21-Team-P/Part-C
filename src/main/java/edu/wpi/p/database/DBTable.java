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

    /**
     * Basic constructor, does nothing
     */
    public DBTable() {

    }

    /**
     * Constructor that sets the columns for the node and edge tables
     * Creates the tables if they don't already exist
     * @param nodeColumns Columns in the node table
     * @param edgeColumns Columns in the edge table
     */
    public DBTable(List<DBColumn> nodeColumns, List<DBColumn> edgeColumns) {
        this.nodeColumns = nodeColumns;
        this.edgeColumns = edgeColumns;

        this.createTables(false);
    }

    /**
     * Creates the tables from the nodeColumns and edgeColumns
     * If tables with that name already exist, nothing happends unless doClear is true
     * @param doClear if true the tables get deleted first
     */
    private void createTables(boolean doClear) {
        if (doClear) {
            clearNodes();
            clearEdges();
        }
        DatabaseInterface.createTableIfNotExists(nodeTable, this.nodeColumns);
        DatabaseInterface.createTableIfNotExists(edgeTable, this.edgeColumns);
    }

    /**
     * Creates a table with the given columns and the name in this.nodeTable
     * @param columns Columns for the node table
     * @return true if the table got created, false is table already existed or create failed
     */
    public boolean createNodeTable(List<DBColumn> columns) {
        this.nodeColumns = columns;
        return DatabaseInterface.createTableIfNotExists(nodeTable, columns);
    }

    /**
     * Creates a table with the given columns and the name in this.edgeTable
     * @param columns Columns for the node table
     * @return true if the table got created, false is table already existed or create failed
     */
    public boolean createEdgeTable(List<DBColumn> columns) {
        this.edgeColumns = columns;
        return DatabaseInterface.createTableIfNotExists(edgeTable, columns);
    }

    /**
     * Deletes the node table
     */
    public void clearNodes() {
        DatabaseInterface.executeUpdate("DROP TABLE " + nodeTable);
    }

    /**
     * Deleted the edge table
     */
    public void clearEdges() {
        DatabaseInterface.executeUpdate("DROP TABLE " + edgeTable);
    }

    /**
     * Sets the fields of the node in the data base with the same node ID as the given node
     * @param n Node with an ID that matches one in the DB, and other values will be set for that row in the DB
     */
    public void updateNode(Node n) {
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET ycoord = "+n.getYcoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET xcoord = "+n.getXcoord()+" WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET floor = '"+n.getFloor()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET building = '"+n.getBuilding()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET nodeType = '"+n.getType()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET longName = '"+n.getName()+"' WHERE nodeID = '"+n.getId()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET shortName = '"+n.getShortName()+"' WHERE nodeID = '"+n.getId()+"'");
    }

    /**
     * Inserts the given node into the database
     * @param n
     */
    public void addNode(Node n) {
        String insertValue = "'" + n.getId() + "', " + n.getXcoord() + ", " + n.getYcoord() + ", '" + n.getFloor() + "', '" + n.getBuilding() + "', '" + n.getType() + "', '" + n.getName() + "', '" + n.getShortName() + "'";
        DatabaseInterface.insertIntoTable(nodeTable, insertValue);
    }

    /**
     * Adds and edge between the two nodes in the database
     * @param edgeID Desired ID of the new edge
     * @param nodeIDOne ID of the starting node
     * @param nodeIDTwo ID of the ending node
     */
    public void addEdge(String edgeID, String nodeIDOne, String nodeIDTwo) {
        String insertValue = "'" + edgeID + "', '" + nodeIDOne + "', '" + nodeIDTwo + "'";
        DatabaseInterface.insertIntoTable(edgeTable, insertValue);
    }

    /**
     * Removes the edge between the two nodes represented by the given node ID's
     * Order of nodeIDOne and nodeIDTwo is important
     * @param nodeIDOne ID of starting node
     * @param nodeIDTwo ID of ending node
     */
    public void removeEdge(String nodeIDOne, String nodeIDTwo) {
        String removeCommand = "DELETE FROM " + edgeTable +" WHERE STARTNODE='" + nodeIDOne + "' AND ENDNODE='" + nodeIDTwo + "'";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    /**
     * Updates the edge with the given edge ID to have the same values as that of the given edge object
     * @param edgeID edgeID of the edge in the DB to update
     * @param edge new edge to update the information from
     */
    public void updateEdge(String edgeID, Edge edge) {
        DatabaseInterface.executeUpdate("UPDATE " + edgeTable + " SET EDGEID = "+edge.getEdgeID()+" WHERE nodeID = '"+edge.getEdgeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET STARTNODE = "+edge.getStartNode()+" WHERE nodeID = '"+edge.getEdgeID()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + nodeTable + " SET ENDNODE = '"+edge.getEndNode()+"' WHERE nodeID = '"+edge.getEdgeID()+"'");
    }

    /**
     * Removes the node with the given nodeID from the DB
     * Also removes all the edges connected to that node
     * @param nodeID ID of the node to be removed
     */
    public void removeNode(String nodeID) {
        String removeNodeCommand = "DELETE FROM " + nodeTable +" WHERE NODEID='" + nodeID + "'";
        DatabaseInterface.executeUpdate(removeNodeCommand);
        String removeEdgesCommand = "DELETE FROM " + edgeTable +" WHERE STARTNODE='" + nodeID + "' OR ENDNODE='" + nodeID + "'";
        DatabaseInterface.executeUpdate(removeEdgesCommand);
    }

    /**
     * A helper function to get the index in the list of the column with the given name
     * @param columns List of column
     * @param targetName Name of column to get the index of in the list
     * @return Index of the column with name targetName in columns
     */
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
     * Gets a list of all the nodes in the database
     * The nodes do not have their edges set
     * @return
     */
    public List<Node> getNodes() {
        List<List<String>> nodeData = DatabaseInterface.getAllFromTable(nodeTable);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(nodeTable);

        // this is bad, but the database currently doesn't guarantee order of the columns
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

    public List<String> getIDs() {
        //parse L1Nodes
        List<List<String>> nodeData = DatabaseInterface.getAllFromTable(nodeTable);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(nodeTable);
        int nodeID = indexOfColumnByName(dbColumns, "nodeID");


        //create nodes
        List<String> IDs = new ArrayList<>();
        for(int i = 1; i < nodeData.size(); i++) {
            List<String> nodeString = nodeData.get(i);
            String id = nodeString.get(nodeID);
            IDs.add(id);
        }

        return IDs;
    }

    /**
     * Gets a list of list of strings where each sub-list is a list of the nodeID for the starting and then ending node
     * @return
     */
    public List<List<String>> getEdgeData() {
        this.edgeData = DatabaseInterface.getAllFromTable(edgeTable);
        return edgeData;
    }

    /**
     * Gets a list of edge objects representing all the edges in the database
     * @return List of edges from the database
     */
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
