package edu.wpi.p.userstate;

public abstract class UserState {

    public UserState() {
    }

    void beGuest() {

    }

    boolean isGuest() {
        return false;
    }

    boolean isLoggedIn() {
        return false;
    }
    void login(String username, String password) throws LoginException {
        return;
    }
    void logout() {

    }

}
