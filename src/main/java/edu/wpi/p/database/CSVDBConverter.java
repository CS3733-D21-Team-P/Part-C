package edu.wpi.p.database;

import edu.wpi.p.AStar.Node;
import edu.wpi.p.csv.CSVData;
import edu.wpi.p.csv.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the conversion from CSVData -> DBTable and back
 */
public class CSVDBConverter {


    private static String SQLTypeFromColumnName(String csvColumnName) {
        switch (csvColumnName) {
            case "xcoord":
            case "ycoord":
                return "INT";
            default:
                return "VARCHAR(64)";
        }
    }

    private static String SQLOptionsFromColumnName(String csvColumnName) {
        switch (csvColumnName) {
            case "nodeID":
            case "edgeID":
                return "NOT NULL";
            default:
                return "";
        }
    }

    private static List<DBColumn> dbColumnsFromCSVColumns(List<Column> csvColumns) {
        List<DBColumn> dbColumns = new ArrayList<>(csvColumns.size());
        for(Column col : csvColumns) {
            String colName = col.getName();
            String type = SQLTypeFromColumnName(colName);
            String options = SQLOptionsFromColumnName(colName);
            DBColumn column = new DBColumn(colName, type, options);
            if (!options.equals("")) {
                column.setPrimarykey(true);
            }
            dbColumns.add(column);
        }
        return dbColumns;
    }

    private static List<Node> createNodesFromColumns(List<Column> nodeColumns) {
        List<String> idData = nodeColumns.get(0).getData();
        List<Integer> xCoordData = nodeColumns.get(1).getData();
        List<Integer> yCoordData = nodeColumns.get(2).getData();
        List<String> floorData = nodeColumns.get(3).getData();
        List<String> buildingData = nodeColumns.get(4).getData();
        List<String> nodeTypeData = nodeColumns.get(5).getData();
        List<String> longNameData = nodeColumns.get(6).getData();
        List<String> shortNameData = nodeColumns.get(7).getData();
        List<Node> nodes = new ArrayList<>(idData.size());
        for (int i = 0; i < idData.size(); i++) {
            Node newNode = new Node(longNameData.get(i),
                    idData.get(i), xCoordData.get(i), yCoordData.get(i), floorData.get(i), buildingData.get(i), nodeTypeData.get(i), shortNameData.get(i));
            nodes.add(newNode);
        }
        return nodes;
    }

    public static void addCSVNodesToTable(DBTable table, CSVData nodes) {
        List<Column> nodeColumns = nodes.getAllColumns();

        List<Node> tableNodes = createNodesFromColumns(nodeColumns);
        List<DBColumn> nodeDBCols = dbColumnsFromCSVColumns(nodeColumns);
        table.createNodeTable(nodeDBCols);

        for(Node n : tableNodes) {
            table.addNode(n);
        }
    }

    public static void addCSVEdgesToTable(DBTable table, CSVData edges) {
        List<Column> edgeColumns = edges.getAllColumns();

        List<DBColumn> edgeDBCols = dbColumnsFromCSVColumns(edgeColumns);
        table.createEdgeTable(edgeDBCols);

        List<String> edgeIDs =  edgeColumns.get(0).getData();
        List<String> startNodes = edgeColumns.get(1).getData();
        List<String> endNodes = edgeColumns.get(2).getData();
        for (int i = 0; i < edgeIDs.size(); i++) {
            table.addEdge(edgeIDs.get(i), startNodes.get(i), endNodes.get(i));
        }
    }
    /**
     * Returns a new instance of DBTable filled with the data and columns from CSVData for the nodes and edges
     * @param nodes CSVData parsed from MapNodes.csv
     * @param edges CSVData parsed from MapEdges.csv
     * @return new DBTable with data from CSV
     */
    public static DBTable tableFromCSVData(CSVData nodes, CSVData edges) {
        List<Column> nodeColumns = nodes.getAllColumns();
        List<Column> edgeColumns = edges.getAllColumns();

        List<DBColumn> nodeDBCols = dbColumnsFromCSVColumns(nodeColumns);
        List<DBColumn> edgeDBCols = dbColumnsFromCSVColumns(edgeColumns);

        DBTable table = new DBTable(nodeDBCols, edgeDBCols);

        List<Node> tableNodes = createNodesFromColumns(nodeColumns);
        for(Node n : tableNodes) {
            table.addNode(n);
        }

        List<String> edgeIDs = edgeColumns.get(0).getData();
        List<String> startNodes = edgeColumns.get(1).getData();
        List<String> endNodes = edgeColumns.get(2).getData();
        for (int i = 0; i < edgeIDs.size(); i++) {
            table.addEdge(edgeIDs.get(i), startNodes.get(i), endNodes.get(i));
        }

        return table;
    }

    public static CSVData csvNodesFromTable(DBTable table) {
        CSVData data = new CSVData("MapNodes");
        List<Node> nodes = table.getNodes();
        List<String> idData = nodes.stream().map(n -> n.getId()).collect(Collectors.toList());
        List<String> xCoordData = nodes.stream().map(n -> n.getXcoord()).map(x -> x.toString()).collect(Collectors.toList());
        List<String> yCoordData = nodes.stream().map(n -> n.getYcoord()).map(y -> y.toString()).collect(Collectors.toList());
        List<String> floorData = nodes.stream().map(n -> n.getFloor()).collect(Collectors.toList());
        List<String> buildingData = nodes.stream().map(n -> n.getBuilding()).collect(Collectors.toList());
        List<String> nodeTypeData = nodes.stream().map(n -> n.getType()).collect(Collectors.toList());
        List<String> longNameData = nodes.stream().map(n -> n.getName()).collect(Collectors.toList());
        List<String> shortNameData = nodes.stream().map(n -> n.getShortName()).collect(Collectors.toList());
        data.addColumnFromStringData("nodeID", idData);
        data.addColumnFromStringData("xcoord", xCoordData);
        data.addColumnFromStringData("ycoord", yCoordData);
        data.addColumnFromStringData("floor", floorData);
        data.addColumnFromStringData("building", buildingData);
        data.addColumnFromStringData("nodeType", nodeTypeData);
        data.addColumnFromStringData("longName", longNameData);
        data.addColumnFromStringData("shortName", shortNameData);
        return data;
    }

    public static CSVData csvEdgesFromTable(DBTable table) {
        CSVData data = new CSVData("MapEdges");
        List<List<String>> edgeData = table.getEdgeData();
        List edgeIds = edgeData.stream().map(list -> list.get(0)).collect(Collectors.toList());
        List startNode = edgeData.stream().map(list -> list.get(1)).collect(Collectors.toList());
        List endNode = edgeData.stream().map(list -> list.get(2)).collect(Collectors.toList());
        data.addColumnFromStringData("edgeID", edgeIds);
        data.addColumnFromStringData("startNode", startNode);
        data.addColumnFromStringData("endNode", endNode);
        return data;
    }
}
