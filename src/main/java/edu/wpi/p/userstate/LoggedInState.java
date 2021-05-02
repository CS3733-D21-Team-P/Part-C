package edu.wpi.p.userstate;

public class LoggedInState extends UserState {

    public LoggedInState() {
        super();
    }

    @Override
    boolean isLoggedIn() {
        return true;
    }

    @Override
    void login(String username, String password) {

    }

    @Override
    void logout() {
        User.getInstance().changeState(new LoggedOutState());
    }
}
