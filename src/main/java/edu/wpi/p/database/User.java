package edu.wpi.p.database;

public class User {
    private String Name;
    private String Username;
    private String Password;
    private String Status;


    public User(String Name, String Username, String Password, String Status) {
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
        this.Status = Status;
    }

    public User(String username, String password) {
        this.Username = username;
        this.Password = password;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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


}
