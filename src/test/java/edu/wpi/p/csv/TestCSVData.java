package edu.wpi.p.csv;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCSVData {
    private static final String name =  "csv data name";
    private static final String stringColumnName = "string column";
    private static final String stringValues[] = {"cat", "dog", "owl"};
    private static final String difficultStringValues[] = {"123", "67", "1a3"};
    private static final String intValues[] = {"123", "67", "13"};
    private CSVData data = new CSVData(name);


    private void addStringColumnWithData(CSVData d) {
        d.addColumnFromStringData(stringColumnName, Arrays.asList(stringValues));
    }

    @Test
    void testConstructorName() {
        assertEquals(name, data.getName());
    }

    @Test
    void testAddColumnFromStringDataString() {
        addStringColumnWithData(data);
        assertEquals(1, data.getAllColumns().size());
        assertEquals(Arrays.asList(stringValues), data.getColumn(stringColumnName).getData());
    }

    @Test
    void testAddColumnFromDifficultStringData() {
        data.addColumnFromStringData("difficult string", Arrays.asList(difficultStringValues));
        assertEquals("123", data.getAllColumns().get(0).getData().get(0));
    }

    @Test
    void testAddColumnFromIntData() {
        data.addColumnFromStringData("int data", Arrays.asList(intValues));
        assertEquals(123, data.getAllColumns().get(0).getData().get(0));
    }

    @Test
    void testAddStringToIntData() {
        // adding a string to a column that was ints, should turn the whole column into strings
        data.addColumnFromStringData("int data", Arrays.asList(intValues));
        data.addToColumn("int data", "cat");
        assertEquals(4, data.getColumn("int data").getData().size());
        assertEquals("123", data.getAllColumns().get(0).getData().get(0));
    }

    @Test
    void testGetColumn() {
        addStringColumnWithData(data);
        assertNotNull(data.getColumn(stringColumnName));
    }

    @Test
    void testGetColumnNames() {
        addStringColumnWithData(data);
        assertEquals(1, data.getColumnNames().size());
        assertEquals(stringColumnName, data.getColumnNames().get(0));
    }

    @Test
    void testAddToColumn() {
        addStringColumnWithData(data);
        data.addToColumn(stringColumnName, "wolf");
        assertEquals(1, data.getColumnNames().size());
        assertEquals(4, data.getColumn(stringColumnName).getData().size());
    }
}
