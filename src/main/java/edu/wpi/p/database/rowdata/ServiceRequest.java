package edu.wpi.p.database.rowdata;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceRequest extends DBRow {
    private String nameCol = "NAME";
    private String locationCol = "LOCATION";
    private String IDCol = "ID";
    private String assignmentCol = "ASSIGNMENT";
    private String completeCol = "COMPLETE";
//    private StringProperty name;
//    private StringProperty location;
//    private StringProperty ID;
//    private StringProperty assignment;
//    private boolean complete;


    public ServiceRequest(String name, String location, String ID, String assignment) {
        this.addValue(nameCol, name);
        this.addValue(IDCol, ID);
        this.addValue(locationCol, location);
        this.addValue(assignmentCol, assignment);
        this.addValue(completeCol, false);
    }

//    public ServiceRequest (StringProperty name, StringProperty location, StringProperty ID, StringProperty assignment) {
//        this.name = name;
//        this.ID = ID;
//        this.location = location;
//        this.assignment = assignment;
//        this.complete = false;
//    }


    public String getName() {
        return (String) this.getValue(nameCol);
    }

    public void setName(String name) {
        this.changeValue(nameCol, name);
    }


    public String getLocation() {
        return (String) this.getValue(locationCol);
    }

    public void setLocation(String location) {
        this.changeValue(locationCol, location);
    }

    public String getID() {
        return (String) this.getValue(IDCol);
    }

    public void setID(String ID) {
        this.changeValue(IDCol, ID);
    }

    public String getAssignment() {
        return (String) this.getValue(assignmentCol);
    }

    public void setAssignment(String assignment) {
        this.changeValue(assignmentCol, assignment);
    }

    public void completed() {
        this.changeValue(completeCol, true);
    }

    public boolean getCompleted() {
        return (boolean) this.getValue(completeCol);
    }
}
