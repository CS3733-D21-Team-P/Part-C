package edu.wpi.p.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * A column within a CSV file.
 * Has a type that is a generic and so is not known by the column at runtime
 */
public class Column<T>{
    /**
     * The name of the column
     */
    private final String name;

    /**
     * The data within the column
     */
    private final List<T> data;

    /**
     * @param name Name of the column
     */
    public Column(String name) {
        this.name = name;
        data = new ArrayList<>();
    }

    public void addValues(List<T> values) {
        data.addAll(values);
    }
    /**
     * Adds a value to the end of the column data
     * @param value Value to add to the column
     */
    public void addValue(T value) {
        data.add(value);
    }

    public String getName() {
        return name;
    }

    public List<T> getData() {
        return data;
    }
}
