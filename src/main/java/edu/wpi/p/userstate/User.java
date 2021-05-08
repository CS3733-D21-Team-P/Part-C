package edu.wpi.p.userstate;

public class User {
    private static User instance;
    private String name = "";
    private String permissions = "";
    private UserState state;
    private String username = "";


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public User() {
        this.state = new LoggedOutState();
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

    public String getPermissions() {
        return permissions;
    }

    void setPermissions(String permissions) {
        this.permissions = permissions;
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
