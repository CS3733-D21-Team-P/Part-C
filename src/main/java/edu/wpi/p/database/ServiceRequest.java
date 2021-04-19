package edu.wpi.p.database;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceRequest extends RecursiveTreeObject<ServiceRequest> {

    private StringProperty name;
    private StringProperty location;
    private StringProperty ID;
    private StringProperty assignment;
    private boolean complete;



    public ServiceRequest (String name, String location, String ID, String assignment) {
        this.name = new SimpleStringProperty(name);
        this.ID = new SimpleStringProperty(ID);
        this.location = new SimpleStringProperty(location);
        this.assignment = new SimpleStringProperty(assignment);
        this.complete = false;
    }


    public StringProperty getServiceRequestName() {
        return this.name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }


    public StringProperty getServiceRequestLocation() {
        return this.location;
    }

    public void setLocation(StringProperty location) {
        this.location = location;
    }

    public StringProperty getServiceRequestID() {
        return this.ID;
    }

    public void setID(StringProperty ID) {
        this.ID = ID;
    }

    public StringProperty getAssignment() {return this.assignment;}

    public void setAssignment(StringProperty assignment) {
        this.assignment = assignment;
    }

    public void completed() {
        this.complete = true;
    }

    public boolean getCompleted() {
        return this.complete;
    }
}
