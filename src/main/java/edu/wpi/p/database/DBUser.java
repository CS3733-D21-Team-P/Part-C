package edu.wpi.p.database;

import edu.wpi.p.AStar.Node;
import edu.wpi.p.csv.Column;
import org.sqlite.core.DB;

import java.util.ArrayList;
import java.util.List;

public class DBUser {
    String DBUser = "USER";
    private List<DBColumn> columns = new ArrayList<>();

    public DBUser(){
        init();
        createUserTable(columns);
    }

    private void init(){
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Username", "varchar(256)", ""));
        columns.add(new DBColumn("Password", "varchar(256)", ""));
        columns.add(new DBColumn("Identity", "varchar(256)", ""));
    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearUserTable();
        }
        DatabaseInterface.createTableIfNotExists(DBUser, columns);
    }

    public boolean createUserTable(List<DBColumn> columns) {
        this.columns = columns;
        return DatabaseInterface.createTableIfNotExists(DBUser, columns);
    }

    public void clearUserTable() {
        DatabaseInterface.executeUpdate("DROP TABLE " + DBUser);
    }

    public String checkUsername(String Username) {
        String selectCommand ="SELECT Password FROM " + DBUser + " WHERE Username='" + Username + "'";
        return DatabaseInterface.checkColumnObjects(selectCommand);
    }

    public String checkIdentity(String Username) {
        String selectCommand ="SELECT Identity FROM " + DBUser + " WHERE Username='" + Username + "'";
        return DatabaseInterface.checkColumnObjects(selectCommand);
    }

    public void addUser(User e) {
        String insertValue = "'" + e.getName() + "', '" + e.getUsername() + "', '" + e.getPassword() + "', '" + e.getIdentity() + "'";
        DatabaseInterface.insertIntoTable(DBUser, insertValue);
    }

    public void removeUser(String Username) {
        String removeCommand = "DELETE FROM " + DBUser +" WHERE Username='" + Username + "";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void updateUser(User e) {
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Name = "+e.getName()+" WHERE nodeID = '"+e.getUsername()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Position = '"+e.getPassword()+"' WHERE nodeID = '"+e.getUsername()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Position = '"+e.getIdentity()+"' WHERE nodeID = '"+e.getUsername()+"'");
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
    public List<User> getUsers() {
        List<List<String>> userData = DatabaseInterface.getAllFromTable(DBUser);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(DBUser);

        int Name = indexOfColumnByName(dbColumns, "Name");
        int Username = indexOfColumnByName(dbColumns, "Username");
        int Password = indexOfColumnByName(dbColumns, "Password");
        int Identity = indexOfColumnByName(dbColumns, "Identity");

        //create Users
        List<User> users = new ArrayList<>(userData.size());
        for(int i = 1; i < userData.size(); i++) {
            List<String> userString = userData.get(i);

            User user = new User(
                    userString.get(Name),
                    userString.get(Username),
                    userString.get(Password),
                    userString.get(Identity));
            users.add(user);
        }

        return users;
    }

    public List<String> getUsernames() {

        List<List<String>> userData = DatabaseInterface.getAllFromTable(DBUser);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(DBUser);
        int username = indexOfColumnByName(dbColumns, "Username");

        List<String> usernames = new ArrayList<>();
        for(int i = 1; i < userData.size(); i++) {
            List<String> nodeString = userData.get(i);
            String id = nodeString.get(username);
            usernames.add(id);
        }

        return usernames;
    }

    public List<String> getPasswords() {
        List<List<String>> userData = DatabaseInterface.getAllFromTable(DBUser);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(DBUser);
        int username = indexOfColumnByName(dbColumns, "Password");

        List<String> usernames = new ArrayList<>();
        for(int i = 1; i < userData.size(); i++) {
            List<String> nodeString = userData.get(i);
            String id = nodeString.get(username);
            usernames.add(id);
        }

        return usernames;
    }
}