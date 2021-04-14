package edu.wpi.p.database;

import AStar.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;



public class DBTable extends DatabaseInterface {
    private String name;
    private List<DBColumn> columns;
    private List<Node> nodeList;
    private List<List<String>> edgeData;

//    public DBTable(String name, List<DBColumn> columns) {
//        this.name = name;
//        this.columns = columns;
//    }

    public void updateNode() throws SQLException {
        System.out.println("Enter nodeID: ");
        PreparedStatement anode;
        Scanner alpha = new Scanner(System.in);
        String bnode = alpha.nextLine();
        anode = conn.prepareStatement("SELECT * FROM Node WHERE nodeID ='"+bnode+"'");
        System.out.println("Enter new xcoord: ");
        String xcoord = alpha.nextLine();
        System.out.println("Enter new ycoord: ");
        String ycoord = alpha.nextLine();
        System.out.println("Enter new floor: ");
        String floor = alpha.nextLine();
        System.out.println("Enter new building: ");
        String building = alpha.nextLine();
        System.out.println("Enter new nodeType: ");
        String nodeType = alpha.nextLine();
        System.out.println("Enter new longName: ");
        String longName = alpha.nextLine();
        System.out.println("Enter new shortName: ");
        String shortName = alpha.nextLine();
        anode.executeUpdate("UPDATE xcoord = '"+xcoord+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE ycoord = '"+ycoord+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE floor = '"+floor+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE building = '"+building+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE nodeType = '"+nodeType+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE longName = '"+longName+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE shortName = '"+shortName+"' WHERE nodeID = '"+bnode+"'");
        anode.close();
        throw new SQLException();

    }

    public void addNode() throws SQLException {
        Statement anode = conn.createStatement();
        Scanner alpha = new Scanner(System.in);
        System.out.println("Enter new nodeID: ");
        String nodeID = alpha.nextLine();
        System.out.println("Enter new xcoord: ");
        String xcoord = alpha.nextLine();
        System.out.println("Enter new ycoord: ");
        String ycoord = alpha.nextLine();
        System.out.println("Enter new floor: ");
        String floor = alpha.nextLine();
        System.out.println("Enter new building: ");
        String building = alpha.nextLine();
        System.out.println("Enter new nodeType: ");
        String nodeType = alpha.nextLine();
        System.out.println("Enter new longName: ");
        String longName = alpha.nextLine();
        System.out.println("Enter new shortName: ");
        String shortName = alpha.nextLine();
        anode.executeUpdate("INSERT INTO Node + VALUES('"+nodeID+"','"+xcoord+"','"+ycoord+"','"+floor+"','"+building+"','"+nodeType+"','"+longName+"','"+shortName+"')");
        anode.close();

    }

    public void addEdge() throws SQLException {
        Statement aedge = conn.createStatement();
        Scanner alpha = new Scanner(System.in);
        System.out.println("Enter new edgeID: ");
        String edgeID = alpha.nextLine();
        System.out.println("Enter new startNode: ");
        String startNode = alpha.nextLine();
        System.out.println("Enter new endNode: ");
        String endNode = alpha.nextLine();
        aedge.executeUpdate("INSERT INTO Edge + VALUES('"+edgeID+"','"+startNode+"','"+endNode+"')");
        aedge.close();
    }

    public void removeEdge() throws SQLException {
        Statement aedge = conn.createStatement();
        Scanner alpha = new Scanner(System.in);
        System.out.println("Enter edgeID: ");
        String edgeID = alpha.nextLine();
        aedge.executeUpdate("DELETE FROM Edge WHERE edgeID = '"+edgeID+"'");
        aedge.close();
    }

    public void removeNode() throws SQLException {
        Statement anode = conn.createStatement();
        Scanner alpha = new Scanner(System.in);
        System.out.println("Enter nodeID: ");
        String nodeID = alpha.nextLine();
        anode.executeUpdate("DELETE FROM Node WHERE nodeID = '"+nodeID+"'");
        anode.close();

    }

    public List<Node> getNodes() {
        //parse L1Nodes
        List<List<String>> nodeData = new ArrayList<>();
        this.nodeList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Nodes.csv"))) {
            while (scanner.hasNextLine()) {
                nodeData.add(getNodeString(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //L1Nodes columns
        int nodeID = 0;
        int xcoord = 1;
        int ycoord = 2;
        int floor = 3;
        int building = 4;
        int nodeType = 5;
        int longName = 6;
        int shortName = 7;

        //create nodes
        for(int i = 1; i < nodeData.size(); i++) {
            List<String> nodeString = nodeData.get(i);

            Node node = new Node(
                    nodeString.get(nodeID),
                    nodeString.get(longName),
                    Integer.parseInt(nodeString.get(xcoord)),
                    Integer.parseInt(nodeString.get(ycoord)));
            this.nodeList.add(node);
        }

        return nodeList;
    }

    public List<List<String>> getEdgeData() {
        //parse L1Edges
        this.edgeData = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/AStar/L1Edges.csv"))) {
            while (scanner.hasNextLine()) {
                this.edgeData.add(getNodeString(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return edgeData;
    }

    private List<String> getNodeString(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }


}
