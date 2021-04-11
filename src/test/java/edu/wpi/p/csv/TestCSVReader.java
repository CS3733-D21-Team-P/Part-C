package edu.wpi.p.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCSVReader {
    private final static String simpleCSVDataLines[] = {"first, last", "alice, smith", "bob, smith", "george, brown"};
    private final static String simpleCSVName = "simple.csv";
    private final static String brokenCSVDataLines[] = {"first, last", "alice, smith", "bob", "george, brown"};
    private final static String brokenCSVName = "broken.csv";
    private final static String typesCSVDataLines[] = {"first, id", "alice, 1", "bob, 2", "george, 3"};
    private final static String typesCSVName = "types.csv";

    @TempDir
    static File tempDir;


    private static CSVData simpleCSV;
    private static CSVData typesCSV;

    @BeforeAll
    public static void setup() throws IOException {
        // simple, just strings
        File simpleCSVFile = new File(tempDir, simpleCSVName);
        Files.write(simpleCSVFile.toPath(), Arrays.asList(simpleCSVDataLines));
        System.out.println("csv path is " + simpleCSVFile.toPath());
        try {
            simpleCSV = CSVReader.readCSVFile(simpleCSVFile.toPath().toString());
        }
        catch (Exception e) {
            System.out.println("Simple CSV reading failed: " + e.getMessage());
            e.printStackTrace();
        }


        // types, has id which is an int
        File typesCSVFile = new File(tempDir, typesCSVName);
        Files.write(typesCSVFile.toPath(), Arrays.asList(typesCSVDataLines));
        try {
            typesCSV = CSVReader.readCSVFile(typesCSVFile.toPath().toString());
        }
        catch (Exception e) {
            System.out.println("Types CSV reading failed: " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Test
    public void testSimpleName() {
        assertEquals("simple", simpleCSV.getName());
    }

    @Test
    public void testSimpleColumns() {
        List<Column> columns = simpleCSV.getAllColumns();
        assertEquals(2, columns.size());
        assertEquals("first", columns.get(0).getName());
        assertEquals("last", columns.get(1).getName());

        final String firsts[] = {"alice", "bob", "george"};
        final String lasts[] = {"smith", "smith", "brown"};
        assertEquals(Arrays.asList(firsts), columns.get(0).getData());
        assertEquals(Arrays.asList(lasts), columns.get(1).getData());
    }

    @Test
    public void testExceptionBroken() throws IOException{
        // broken, one line not long enough
        File brokenCSVFile = new File(tempDir, brokenCSVName);
        Files.write(brokenCSVFile.toPath(), Arrays.asList(brokenCSVDataLines));
        Exception e = assertThrows(Exception.class, () -> {CSVReader.readCSVFile(brokenCSVFile.toPath().toString());});
    }

    @Test
    public void testTypes() {
        List<Column> columns = typesCSV.getAllColumns();
        assertEquals(2, columns.size());
        assertEquals("first", columns.get(0).getName());
        assertEquals("id", columns.get(1).getName());

        final String firsts[] = {"alice", "bob", "george"};
        final int ids[] = {1, 2, 3};
        assertEquals(Arrays.asList(firsts), columns.get(0).getData());
        assertEquals(ids[0], columns.get(1).getData().toArray()[0]);
    }

}
