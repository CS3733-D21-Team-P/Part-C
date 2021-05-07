package edu.wpi.p.userstate;

import java.util.UUID;

public class User {
    private static User instance;
    private String name = "";
    private String permissions = "";
    /**
     * A UUID that persists for the whole time the application is open
     * Allows you to track people between guest logins
     */
    private String id = "";
    // whether or not they are approved to enter the hospital, when we become a guest this is set to false by default
    private boolean approvedForEntry = true;
    private UserEntryLocation entryLocation = UserEntryLocation.EITHER_ENTRANCE;
    private UserState state;

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    void reset() {
        this.name = "";
        this.permissions = "";
        this.approvedForEntry = true;
        this.entryLocation = UserEntryLocation.EITHER_ENTRANCE;
    }

    public User() {
        this.state = new LoggedOutState();
        resetId();
    }

    void changeState(UserState newState) {
        state = newState;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    void resetId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getPermissions() {
        return permissions;
    }

    void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public boolean isApprovedForEntry() {
        return approvedForEntry;
    }

    void setApprovedForEntry(boolean approvedForEntry) {
        this.approvedForEntry = approvedForEntry;
    }

    public UserEntryLocation getEntryLocation() {
        return entryLocation;
    }

    void setEntryLocation(UserEntryLocation entryLocation) {
        this.entryLocation = entryLocation;
    }

    public boolean isLoggedIn() {
        return state.isLoggedIn();
    }
    public void login(String username, String password) throws LoginException{
        state.login(username, password);
    }
    public void logout() {
        this.state.logout();
    }
    public boolean isGuest() { return this.state.isGuest(); }
    public void beGuest() { this.state.beGuest(); }
}
