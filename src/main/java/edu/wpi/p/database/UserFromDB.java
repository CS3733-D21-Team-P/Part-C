package edu.wpi.p.database;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.p.views.map.NodeTableEntry;

public class UserFromDB extends RecursiveTreeObject<UserFromDB> {
    private String Name;
    private String Username;
    private String Password;
    private String Status;
    private String ParkingNodeID;


    public UserFromDB(String Name, String Username, String Password, String Status) {
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
        this.Status = Status;
    }

    public UserFromDB(String Name, String Username, String Password, String Status,String ParkingNodeID) {
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
        this.Status = Status;
        this.ParkingNodeID = ParkingNodeID;
    }

    public UserFromDB(String username, String password) {
        this.Username = username;
        this.Password = password;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public String getBlank() {
        return ParkingNodeID;
    }

    public void setBlank(String blank) {
        ParkingNodeID = blank;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIdentity() {
        return Status;
    }

    public void setIdentity(String status) {
        Status = status;
    }

    public String getParkingNodeID() {
        return ParkingNodeID;
    }

    public void setParkingNodeID(String parkingNodeID) {
        this.ParkingNodeID = parkingNodeID;
    }
}
