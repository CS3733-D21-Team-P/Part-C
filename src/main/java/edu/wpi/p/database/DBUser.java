package edu.wpi.p.database;

import java.util.ArrayList;
import java.util.List;

public class DBUser {
    String DBUser = "USERS";
    private List<DBColumn> columns = new ArrayList<>();

    public DBUser(){
        init();
        createUserTable(columns);
    }

    private void init(){
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Username", "varchar(256)", ""));
        columns.add(new DBColumn("Password", "varchar(256)", ""));
        columns.add(new DBColumn("Status", "varchar(256)", ""));
        columns.add(new DBColumn("ParkingNodeId", "varchar(256)", ""));
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
        return DatabaseInterface.checkColumnObjects(selectCommand, "Password");
    }

    public void setParkingNodeID(String Username, String ParkingID){
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET ParkingNodeId = '"+ParkingID+"' WHERE Username = '"+Username+"'");
    }

    public String checkParkingNodeID(String Username){
        String selectCommand ="SELECT ParkingNodeId FROM " + DBUser + " WHERE Username='" + Username + "'";
        return DatabaseInterface.checkColumnObjects(selectCommand, "ParkingNodeId");
    }


    public String checkIdentity(String Username) {
        String selectCommand ="SELECT Status FROM " + DBUser + " WHERE Username='" + Username + "'";
        return DatabaseInterface.checkColumnObjects(selectCommand, "Status");
    }

    public void addUser(UserFromDB e) {
        String insertValue = "'" + e.getName() + "', '" + e.getUsername() + "', '" + e.getPassword() + "', '" + e.getIdentity() + "', '" + e.getParkingNodeID() + "'";
        DatabaseInterface.insertIntoTable(DBUser, insertValue);
    }

    public void removeUser(String Username) {
        String removeCommand = "DELETE FROM " + DBUser +" WHERE Username='" + Username + "'";
        DatabaseInterface.executeUpdate(removeCommand);
    }

    public void updateUser(UserFromDB e) {
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Name = "+e.getName()+" WHERE Username = '"+e.getUsername()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Password = '"+e.getPassword()+"' WHERE Username = '"+e.getUsername()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET Status = '"+e.getIdentity()+"' WHERE Username = '"+e.getUsername()+"'");
        DatabaseInterface.executeUpdate("UPDATE " + DBUser + " SET ParkingNodeId = '"+e.getParkingNodeID()+"' WHERE Username = '"+e.getUsername()+"'");
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
    public List<UserFromDB> getUsers() {
        List<List<String>> userData = DatabaseInterface.getAllFromTable(DBUser);//new ArrayList<>();

        List<DBColumn> dbColumns = DatabaseInterface.getColumns(DBUser);

        int Name = indexOfColumnByName(dbColumns, "Name");
        int Username = indexOfColumnByName(dbColumns, "Username");
        int Password = indexOfColumnByName(dbColumns, "Password");
        int Identity = indexOfColumnByName(dbColumns, "Status");
        int ParkingNodeID = indexOfColumnByName(dbColumns, "ParkingNodeId");

        //create Users
        List<UserFromDB> userFromDBS = new ArrayList<>(userData.size());
        for(int i = 1; i < userData.size(); i++) {
            List<String> userString = userData.get(i);

            UserFromDB userFromDB = new UserFromDB(
                    userString.get(Name),
                    userString.get(Username),
                    userString.get(Password),
                    userString.get(Identity));
            userFromDB.setParkingNodeID(userString.get(ParkingNodeID));
            userFromDBS.add(userFromDB);
        }

        return userFromDBS;
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