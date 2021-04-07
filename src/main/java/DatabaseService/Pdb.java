package DatabaseService;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;

public class Pdb {
    static String username = "";
    static String password = "";
    static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    static String dbName = "TeamPDB";
    static String connectionURL = "jdbc:derby:" + dbName + ";create=true";
    static Connection conn = null;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner reader = new Scanner(System.in);
        System.out.print("Username: ");
        username = reader.next();
        System.out.print("Password: ");
        password = reader.next();
        reader.close();

        if (args.length == 0) {
            System.out.println("1- Node Information");
            System.out.println("2- Update Node Coordinates");
            System.out.println("3- Update Node Location Long Name");
            System.out.println("4- Edge Information");
            System.out.println("5- Exit Program");
            return;
        }
        if (args.length == 1) {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectionURL);
            if (conn != null) {
                if (args[2].equals("1")) {Node(conn);}
                if (args[2].equals("2")) { UpdateNode(conn);}
                if (args[2].equals("3")) { UpdateLongName(conn);}
                if (args[2].equals("4")) { Edge(conn);}
                if (args[2].equals("5")) { conn.close(); }
                conn.close();
            }
        }
    }

    public static void Node(Connection c) throws SQLException {
        PreparedStatement anode;
        anode = c.prepareStatement("SELECT * FROM Node ");
        ResultSet r = anode.executeQuery();
        while(r.next()) {
            System.out.println("nodeID: " + r.getString("nodeID"));
            System.out.println("xcoord: " + r.getString("xcoord"));
            System.out.println("ycoord: " + r.getString("ycoord"));
            System.out.println("floor: " + r.getString("floor"));
            System.out.println("building: " + r.getString("building"));
            System.out.println("nodeType: " + r.getString("nodeType"));
            System.out.println("longName: " + r.getString("longName"));
            System.out.println("shortName: " + r.getString("shortName"));
        }

        r.close();
        anode.close();
        throw new SQLException();
    }

    public static void UpdateNode(Connection c) throws SQLException {
        System.out.println("Enter nodeID: ");
        PreparedStatement anode;
        Scanner alpha = new Scanner(System.in);
        String bnode = alpha.nextLine();
        anode = c.prepareStatement("SELECT * FROM Node WHERE nodeID ='"+bnode+"'");
        System.out.println("Enter new xcoord: ");
        String xcoord = alpha.nextLine();
        System.out.println("Enter new ycoord: ");
        String ycoord = alpha.nextLine();
        anode.executeUpdate("UPDATE xcoord = '"+xcoord+"' WHERE nodeID = '"+bnode+"'");
        anode.executeUpdate("UPDATE ycoord = '"+ycoord+"' WHERE nodeID = '"+bnode+"'");
        alpha.close();
        anode.close();
        throw new SQLException();
    }

    public static void UpdateLongName(Connection c) throws SQLException {
        System.out.println("Enter nodeID: ");
        PreparedStatement anode;
        Scanner alpha = new Scanner(System.in);
        String bnode = alpha.nextLine();
        anode = c.prepareStatement("SELECT * FROM Node WHERE nodeID ='"+bnode+"'");
        System.out.println("Enter new LongName: ");
        String longname = alpha.nextLine();
        anode.executeUpdate("UPDATE longName = '"+longname+"' WHERE nodeID = '"+bnode+"'");
        alpha.close();
        anode.close();
        throw new SQLException();
    }

    public static void Edge(Connection c) throws SQLException {
        PreparedStatement aedge;
        aedge = c.prepareStatement("SELECT * FROM Node ");
        ResultSet r = aedge.executeQuery();
        while(r.next()) {
            System.out.println("edgeID: " + r.getString("edgeID"));
            System.out.println("startNode: " + r.getString("startNode"));
            System.out.println("endNode: " + r.getString("endNode"));
        }
        r.close();
        aedge.close();
        throw new SQLException();
    }
}