package DatabaseService;

import java.sql.*;

public class Pdb {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "jdbcHospitalDB";
    String connectionURL = "jdbc:derby:" + dbName + ";create = true";
    Connection conn = null;

    public void LoadDriver() {
        try {
            Class.forName(driver);
            System.out.println(driver + " loaded.");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
            System.out.println("\n Make sure your CLASSPATH variable " +
                    "contains %DERBY_HOME%\\lib\\derby.jar (${DERBY_HOME}/lib/derby.jar). \n");
        }
    }

    public void setUsers() {
        try {
            System.out.println("Trying to connect to " + connectionURL);
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + connectionURL);

            turnOnBuiltInUsers(conn);

            // shut down the database
            conn.close();
            System.out.println("Closed connection");

            /* In embedded mode, an application should shut down Derby.
               Shutdown throws the XJ015 exception to confirm success. */
            boolean gotSQLExc = false;
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if ( se.getSQLState().equals("XJ015") ) {
                    gotSQLExc = true;
                }
            }
            if (!gotSQLExc) {
                System.out.println("Database did not shut down normally");
            } else {
                System.out.println("Database shut down normally");
            }

            // force garbage collection to unload the EmbeddedDriver
            //  so Derby can be restarted
            System.gc();
        } catch (Throwable e) {
            errorPrint(e);
            System.exit(1);
        }

    }

    public static void turnOnBuiltInUsers(Connection conn) throws SQLException {
        System.out.println("Turning on authentication.");
        Statement s = conn.createStatement();

        // Setting and Confirming requireAuthentication
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.connection.requireAuthentication', 'true')");

        // Setting authentication scheme to Derby
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.authentication.provider', 'BUILTIN')");

        // Creating the admin user
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.user.admin', 'admin')");

        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.database.defaultConnectionMode', 'noAccess')");

        // Defining admin users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.database.fullAccessUsers', 'admin')");

        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
                "'derby.database.propertiesOnly', 'false')");
        s.close();
    }

    static void errorPrint(Throwable e) {
        if (e instanceof SQLException)
            SQLExceptionPrint((SQLException)e);
        else {
            System.out.println("A non-SQL error occurred.");
            e.printStackTrace();
        }
    }

    static void SQLExceptionPrint(SQLException sqle) {
        while (sqle != null) {
            System.out.println("\n---SQLException Caught---\n");
            System.out.println("SQLState:   " + (sqle).getSQLState());
            System.out.println("Severity: " + (sqle).getErrorCode());
            System.out.println("Message:  " + (sqle).getMessage());
            sqle.printStackTrace();
            sqle = sqle.getNextException();
        }
    }
}
