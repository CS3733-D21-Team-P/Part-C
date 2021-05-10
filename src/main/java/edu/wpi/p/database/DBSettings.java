package edu.wpi.p.database;

import java.util.ArrayList;
import java.util.List;

public class DBSettings {
    private String settingsTable = "Settings";
    private List<DBColumn> columns = new ArrayList<>();

    private static DBSettings instance;

    /**
     * Instance getter for the singleton
     * @return the singleton instance of DBSettings
     */
    public static DBSettings getInstance() {
        if (instance == null) {
            instance = new DBSettings();
        }
        return instance;
    }

    public DBSettings(){
        init();
        createSettingsTable(columns);
    }

    private void init(){
        columns.add(new DBColumn("Name", "varchar(256)", ""));
        columns.add(new DBColumn("Value", "varchar(256)", ""));
    }

    private void createTables(boolean doClear) {
        if (doClear) {
            clearUserTable();
        }
        DatabaseInterface.createTableIfNotExists(settingsTable, columns);
    }

    public boolean createSettingsTable(List<DBColumn> columns) {
        this.columns = columns;
        return DatabaseInterface.createTableIfNotExists(settingsTable, columns);
    }

    public void clearUserTable() {
        DatabaseInterface.executeUpdate("DROP TABLE " + settingsTable);
    }

    public String getSetting(String settingName) {
        List<List<String>> settings = DatabaseInterface.getAllFromTable(settingsTable);
        List<DBColumn> dbColumns = DatabaseInterface.getColumns(settingsTable);
        int Name = indexOfColumnByName(dbColumns, "Name");
        int Value = indexOfColumnByName(dbColumns, "Value");
        for(List<String> setting : settings) {
            if (setting.get(Name).equals(settingName)) {
                return setting.get(Value);
            }
        }
        return null;
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

    private boolean hasSetting(String settingName) {
        List<List<String>> settings = DatabaseInterface.getAllFromTable(settingsTable);
        List<DBColumn> dbColumns = DatabaseInterface.getColumns(settingsTable);
        int Name = indexOfColumnByName(dbColumns, "Name");
        int Value = indexOfColumnByName(dbColumns, "Value");
        for(List<String> setting : settings) {
            if (setting.get(Name).equals(settingName)) {
                return true;
            }
        }
        return false;
    }
    public void setSetting(String settingName, String settingValue) {
        if (hasSetting(settingName)) {
            DatabaseInterface.executeUpdate("UPDATE " + settingsTable + " SET VALUE = '"+settingValue+"' WHERE NAME = '"+settingName+"'");
        }
        else {
            String insertValue = "'" + settingName + "', '" + settingValue + "'";
            DatabaseInterface.insertIntoTable(settingsTable, insertValue);
        }
    }
}
