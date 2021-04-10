package edu.wpi.p.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to encapsulate the data in a CSV file
 */
public class CSVData {
    /**
     * Name of the csv file or the csv data as a whole
     */
    private String name;

    /**
     * Collection of the columns by their name
     */
    private HashMap<String, Column> columns;

    /**
     * A list of columnNames to be able to preserver order
     */
    private List<String> columnNames;

    /**
     * Stores the types of the columns by the column name
     */
    private HashMap<String, CSVDataType> types;

    public CSVData(String name) {
        this.name = name;
        this.columns = new HashMap<>();
        this.types = new HashMap<>();
        this.columnNames = new ArrayList<>();
    }

    /**
     * @param name Name of the column
     * @return
     */
    public Column getColumn(String name) {
        return columns.get(name);
    }

    /**
     *
     * @return The name of all the columns
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Add a value to the column
     * If the column does not exist, does nothing
     * If the column is of a type that is not compatible with the given value,
     * the column is change into a type that is (adding "cat" to a column of ints makes it into a column of strings)
     * @param name Column name
     * @param value String representing the data, even if the column is some other datatype
     */
    public void addToColumn(String name, String value) {
        Column column = columns.get(name);

        if (column == null)
            return;

        if (types.get(name) == CSVDataType.CSV_INT) {
            try {
                int val = Integer.parseInt(value);
                column.addValue(value);
            } catch(NumberFormatException e) {
                // have to convert the column to a string
                Column newColumn = new Column<String>(name);
                newColumn.addValues((List) column.getData().stream().map(i -> i.toString()).collect(Collectors.toList()));
                newColumn.addValue(value);
                columns.put(name, newColumn);
                types.put(name, CSVDataType.CSV_STRING);
            }
        }
        else {
            // Sting
            column.addValue(value);
        }
    }

    /**
     * Creates a new column at the end with the given name and data
     * Will try and use the most specific data type possible and convert if it can
     * @param name Name of the new column
     * @param data Data to go in the column, as strings
     */
    public void addColumnFromStringData(String name, List<String> data) {
        columnNames.add(name);

        List ints = tryAllInts(data);
        if (ints != null) {
            Column<Integer> column = new Column(name);
            column.addValues(ints);
            columns.put(name, column);
            types.put(name, CSVDataType.CSV_INT);
        }
        else {
            Column column = new Column(name);
            column.addValues(data);

            columns.put(name, column);
            types.put(name, CSVDataType.CSV_STRING);
        }
    }

    /**
     * Tries to create a list of integers by parsing the list of strings
     * If they are not all integers, returns null
     * @param data List of strings to parse
     * @return List of integers or null if any didn't parse properly
     */
    private List<Integer> tryAllInts(List<String> data) {
        List<Integer> ints = new ArrayList<>(data.size());
        for (String value : data) {
            try {
                int val = Integer.parseInt(value);
                ints.add(val);
            } catch(NumberFormatException e) {
                return null;
            }
        }
        return ints;
    }
    /**
     * @return A List of all of the columns, in order
     */
    public List<Column> getAllColumns() {
        return columnNames.stream().map(name -> columns.get(name)).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }
}
