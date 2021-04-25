package edu.wpi.p.database;

import java.util.HashMap;

public class DBRow {
    private HashMap<String, Object> values;

    public DBRow() {
        values = new HashMap<String, Object>();
    }
    public void addValue(String col, Object value) {
        values.put(col, value);
    }

    public void changeValue(String col, Object value) {
        values.put(col, value);
    }

    public Object getValue(String col) {
        return values.get(col);
    }
}
