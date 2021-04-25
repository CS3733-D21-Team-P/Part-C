package edu.wpi.p.database;

import java.util.HashMap;

public class DBRow {
    private HashMap<String, String> values;

    public DBRow() {
        values = new HashMap<String, String>();
    }
    public void addValue(String col, String value) {
        values.put(col, value);
    }

    public void changeValue(String col, String value) {
        values.put(col, value);
    }

    public String getValue(String col) {
        return values.get(col);
    }
}
