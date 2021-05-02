package edu.wpi.p.database.rowdata;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.database.DBRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.ws.Service;
import java.util.HashMap;
import java.util.List;

public class ServiceRequest extends DBRow {
    private String nameCol = "NAME";
    private String locationCol = "LOCATION";
    private String IDCol = "ID";
    private String assignmentCol = "ASSIGNMENT";
    private String completeCol = "COMPLETE";
    private String detailCol = "DETAILS";
    private char lineSeperator = 0x1E;
    private char keyValueSeperator = 0x1F;
    public ServiceRequest() {

    }

    public ServiceRequest(String name, String location, String ID, String assignment) {
        this.addValue(nameCol, name);
        this.addValue(IDCol, ID);
        this.addValue(locationCol, location);
        this.addValue(assignmentCol, assignment);
        this.addValue(completeCol, false);
        this.setDetails("");
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

    public String getDetails() { return (String) this.getValue(detailCol);}
    public HashMap<String, String> getDetailsMap() {
        String details = this.getDetails();
        String[] entries = details.split("" + lineSeperator);
        HashMap<String, String> result = new HashMap<>();
        for (String line : entries) {
            String[] values = line.split("" + keyValueSeperator);
            if (values.length == 2) {
                result.put(values[0], values[1]);
            }
        }

        return result;

    }
    public void setDetails(String details) { this.changeValue(detailCol, details);}
    public void addDetail(String name, String value) {
        String currentDetails = this.getDetails();
        currentDetails += name + keyValueSeperator + value + lineSeperator;
        this.setDetails(currentDetails);
    }
}
