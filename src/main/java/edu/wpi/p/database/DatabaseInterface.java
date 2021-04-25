package edu.wpi.p.database;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInterface {
    static String dbName = "TeamPDB";
    static String connectionURL = "jdbc:derby:" + dbName + ";create = true";
    static Connection conn = null;

    public static boolean hasInit() {
        return conn != null;
    }

    public static void init() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Trying to connect to " + connectionURL);
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + connectionURL);
//            turnOnBuiltInUsers(conn);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a table in the database, returns false if tale does not get created
     * Makes no attempt to prevent creating a table with a name the same as one already in the DB
     * @param tableName Name of the table
     * @param columns A list of DatabaseColumn objects that contain the name, type, and options for the column
     * @return true if the table gets created, false otherwise
     */
    public static boolean createTable(String tableName, List<DBColumn> columns) {
        String columnDefinitions = String.join(",\n", columns.stream().map(DBColumn::toString).collect(Collectors.toList()));
        String primaryKeyName = "";
        for (DBColumn col : columns) {
            if(col.isPrimarykey()) {
                primaryKeyName = col.getName();
                break;
            }
        }
        String tableCreation = "CREATE TABLE " + tableName + " (" + columnDefinitions + (primaryKeyName.equals("") ? "" : ",\nPRIMARY KEY (" + primaryKeyName + ")") + ")";
//        System.out.println(tableCreation);
        if (conn == null) {
            System.out.println("tried to create a table before connection initialized, returning");
            return false;
        }
        try {
            PreparedStatement statement = conn.prepareStatement(tableCreation);
            statement.execute();
            statement.close();
            return true;
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new table in th database with the given name
     * If there is already a table in the database with that name, nothing is done and false is returned
     * If that table does not exist but the attempt to create the table fails, false is returned
     * When a new table with that name gets successfully created, true is returned
     * @param tableName Name of the table
     * @param columns A list of DatabaseColumns that have the name, type, and options for each column
     * @return true if new table created, false otherwise
     */
    public static boolean createTableIfNotExists(String tableName, List<DBColumn> columns) {
        List names = getTableNames();
        if(names.contains(tableName.toUpperCase())) {
            return false;
        }
        return createTable(tableName, columns);
    }

    public static boolean updateDBRow(String table, String idCol, String id, DBRow row) {
        Collection<String> columnsToUpdate = row.getCols();
        columnsToUpdate.removeIf(s -> s.equals(idCol));
        String updateQuery = "UPDATE " + table + " SET ";
        List<String> updates = columnsToUpdate.stream().map(s -> s + " = ?").collect(Collectors.toList());
        updateQuery += String.join(", ", updates);
        updateQuery +=  " WHERE " + idCol + " = '" + id + "'";
//        System.out.println("update query is " + updateQuery);
        try {
            List<DBColumn> cols = getColumns(table);
            PreparedStatement statement = conn.prepareStatement(updateQuery);
            int i = 1;
            for (String c : columnsToUpdate) {
                DBColumn column = cols.stream().filter(col -> col.getName().equals(c)).findFirst().get();
                if (column.getType().equals("INTEGER")) {
                    statement.setInt(i, (Integer) row.getValue(column.getName()));
                }
                else if (column.getType().equals("BOOLEAN")) {
                    statement.setBoolean(i, (Boolean) row.getValue(column.getName()));
                }
                else {
                    statement.setString(i, (String) row.getValue(column.getName()));
                }
                i++;
            }
            statement.execute();
            statement.close();
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertDBRowIntoTable(String table, DBRow row) {
        try {
            List<DBColumn> cols = getColumns(table);
            String query = "INSERT INTO " + table + "(";
            query += String.join(", ", cols.stream().map(DBColumn::getName).collect(Collectors.toList()));
            query += ") VALUES (";
            for (int i = 0; i < cols.size(); i++) {
                query += "?";
                if (i < cols.size() - 1) {
                    query += ", ";
                }
            }
            query += ")";
            PreparedStatement statement = conn.prepareStatement(query);
            for (int i = 0; i < cols.size(); i++) {
                DBColumn col = cols.get(i);
                if (col.getType().equals("INTEGER")) {
                    statement.setInt(i + 1, (Integer) row.getValue(col.getName()));
                }
                else if (col.getType().equals("BOOLEAN")) {
                    statement.setBoolean(i + 1, (Boolean) row.getValue(col.getName()));
                }
                else {
                    statement.setString(i + 1, (String) row.getValue(col.getName()));
                }
            }
            statement.execute();
            statement.close();
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return false;
    }
    public static boolean insertIntoTable(String table, String data) {
        try {
            String insertString = "INSERT INTO "  + table + " VALUES (" + data + ")";
//            System.out.println(insertString);
            PreparedStatement statement = conn.prepareStatement(insertString);
            statement.execute();
            statement.close();
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return false;
    }

    public static void executeUpdate(String command) {
        if (conn == null) {
            System.out.println("tried to list the tables before connection initialized, returning");
            return;
        }
        try {
//            System.out.println("executeUpdate command: " + command);
            PreparedStatement statement = conn.prepareStatement(command);
            statement.executeUpdate();
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
    }

    public static List<List<String>> getQuery(String tableName, String selectQuery) {
        if (conn == null) {
            System.out.println("tried to list the tables before connection initialized, returning");
            return null;
        }
        try {
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            ResultSet result = statement.executeQuery();
            List<DBColumn> columns = getColumns(tableName);
            List<List<String>> rows = new ArrayList<>();
            while(result.next()) {
                try {
                    List<String> values = new ArrayList<>();
                    for(DBColumn c : columns) {
                        values.add(result.getString(c.getName()));
                    }
                    rows.add(values);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            result.close();
            statement.close();
            return rows;
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return null;
    }

    public static List<List<String>> getAllFromTable(String table) {
        String selectQuery = "SELECT * FROM " + table;

        if (conn == null) {
            System.out.println("tried to list the tables before connection initialized, returning");
            return null;
        }
        try {
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            ResultSet result = statement.executeQuery();
            List<DBColumn> columns = getColumns(table);
            List<List<String>> rows = new ArrayList<>();
            while(result.next()) {
                try {
                    List<String> values = new ArrayList<>();
                    for(DBColumn c : columns) {
                        values.add(result.getString(c.getName()));
                    }
                    rows.add(values);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            result.close();
            statement.close();
            return rows;
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Gets the names of the user tables
     * @return A list of the user table names
     */
    public static List<String> getTableNames() {
        String tableQuery = "SELECT * FROM SYS.SYSTABLES WHERE TABLETYPE=\'T\'";

        if (conn == null) {
            System.out.println("tried to list the tables before connection initialized, returning");
            return null;
        }
        try {
            PreparedStatement statement = conn.prepareStatement(tableQuery);
            ResultSet result = statement.executeQuery();

            List<String> names = new ArrayList<>(result.getFetchSize());
            while(result.next()) {
                try {
                    String name = result.getString("TABLENAME");
                    names.add(name);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            result.close();
            statement.close();
            return names;
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a list of types containing the name and type of the columns in the table with the given name
     * @param tableName
     * @return List<DatabaseColumn> where each DatabaseColumn contains the name and type of the column
     */
    public static List<DBColumn> getColumns(String tableName) {
        if (conn == null) {
            System.out.println("tried to list the tables before connection initialized, returning");
            return null;
        }
        try {
            String tableIDQuery = "SELECT TABLEID FROM SYS.SYSTABLES WHERE TABLENAME=?";
            PreparedStatement statement = conn.prepareStatement(tableIDQuery);
            statement.setString(1, tableName.toUpperCase());
            ResultSet result = statement.executeQuery();
            String tableID = "";
            if(result.next()) {
                tableID = result.getString("TABLEID");
            }
            else {
                System.out.println("table with name " + tableName + " does not exist");
            }
            String tableQuery = "SELECT * FROM SYS.SYSCOLUMNS WHERE REFERENCEID=?";
            statement = conn.prepareStatement(tableQuery);
            statement.setString(1, tableID);
            result = statement.executeQuery();

            List<DBColumn> cols = new ArrayList<>(result.getFetchSize());
            while(result.next()) {
                try {
                    String name = result.getString("COLUMNNAME");
                    String type = result.getString("COLUMNDATATYPE");
                    cols.add(new DBColumn(name, type, ""));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            result.close();
            statement.close();
            return cols;
        } catch (Exception e) {
            SQLExceptionPrint((SQLException) e);
            e.printStackTrace();
        }
        return null;
    }

    public static void printColumnNames(String tableName) {
        List<DBColumn> columns = getColumns(tableName);
        System.out.println("Columns in table " + tableName + ":");
        for(DBColumn c: columns) {
            System.out.println(c);
        }
    }

    public static void printTables() {
        List<String> tableNames = getTableNames();
        System.out.println("Table Names:");
        for(String name: tableNames) {
            System.out.println(name);
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