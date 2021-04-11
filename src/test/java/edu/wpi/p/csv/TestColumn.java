package edu.wpi.p.csv;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestColumn {
    private static String name = "foo";
    private Column<String> column = new Column<>(name);

    @Test
    public void testColumnConstructorName() {
        assertEquals(name, column.getName());
    }

    @Test
    public void testColumnConstructorDataInit() {
        // we should be able to get the data object and add to it if we have initialized it
        List<String> data = column.getData();
        data.add("a");
        assertEquals(1, data.size());
    }

    @Test
    public void testAddValue() {
        column.addValue("a");
        column.addValue("b");
        List data = column.getData();
        assertEquals(2, data.size());
        assertEquals("a", data.get(0));
        assertEquals("b", data.get(1));
    }

    @Test
    void testAddValues() {
        final String values[] = {"a", "b", "c", "d"};
        column.addValues(Arrays.asList(values));
        List data = column.getData();
        assertEquals(4, data.size());
        assertEquals("a", data.get(0));
        assertEquals("d", data.get(3));
    }
}
