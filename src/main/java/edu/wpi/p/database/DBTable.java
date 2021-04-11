package edu.wpi.p.database;

import java.util.List;

public class DBTable {
    private String name;
    private List<DBColumn> columns;


    public DBTable(String name, List<DBColumn> columns) {
        this.name = name;
        this.columns = columns;
    }


}
