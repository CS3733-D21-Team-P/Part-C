package edu.wpi.p.database.rowdata;

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

    public ServiceRequest (StringProperty name, StringProperty location, StringProperty ID, StringProperty assignment) {
        this.name = name;
        this.ID = ID;
        this.location = location;
        this.assignment = assignment;
        this.complete = false;
    }


    public StringProperty getServiceRequestName() {
        return this.name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }


    public StringProperty getServiceRequestLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location.setValue(location);
    }

    public StringProperty getServiceRequestID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID.setValue(ID);
    }

    public StringProperty getAssignment() {return this.assignment;}

    public void setAssignment(String assignment) {
        this.assignment.setValue(assignment);
    }

    public void completed() {
        this.complete = true;
    }

    public boolean getCompleted() {
        return this.complete;
    }
}
