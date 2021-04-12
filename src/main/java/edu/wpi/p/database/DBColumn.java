package edu.wpi.p.database;

public class DBColumn {
    private String name;
    private String type;
    private String options;

    public DBColumn(String name, String type, String options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }

    public String toString() {
        return name + " " + type + " " + options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
