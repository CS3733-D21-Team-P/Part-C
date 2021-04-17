package edu.wpi.p.database;

public class ServiceRequest {

    private String name;
    private String location;
    private String ID;



    public ServiceRequest (String name, String location, String ID) {
        this.name = name;
        this.ID = ID;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
