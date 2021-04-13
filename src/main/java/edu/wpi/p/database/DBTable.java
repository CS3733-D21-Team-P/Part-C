package edu.wpi.p.database;

import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DBTable extends DatabaseInterface {
    private String name;
    private List<DBColumn> columns;


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


}
