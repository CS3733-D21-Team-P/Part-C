package edu.wpi.p.database;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDatabaseInterface {

    @Test
    void testTableCreation() {
        DatabaseInterface.init();
        List<DBColumn> columns = new ArrayList<>(3);
        columns.add(new DBColumn("id", "INT", ""));
        columns.add(new DBColumn("lastName", "VARCHAR(64)", ""));
        columns.add(new DBColumn("firstName", "VARCHAR(64)", ""));
        DatabaseInterface.createTableIfNotExists("people", columns);
        DatabaseInterface.printTables();
        System.out.println();
        DatabaseInterface.printColumnNames("people");
        System.out.println();
        DatabaseInterface.insertIntoTable("PEOPLE", "1, 'foo', 'bar'");
        DatabaseInterface.getAllFromTable("PEOPLE");
    }


}
