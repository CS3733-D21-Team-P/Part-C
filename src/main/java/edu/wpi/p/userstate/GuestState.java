package edu.wpi.p.userstate;

public class GuestState extends UserState {

    public GuestState() {
        super();
    }

    @Override
    boolean isGuest() {
        return true;
    }

    @Override
    boolean isLoggedIn() {
        return false;
    }

    @Override
    void login(String username, String password) throws LoginException {
        LoggedInState loggedIn = new LoggedInState();
        User.getInstance().changeState(loggedIn);
        loggedIn.login(username, password);
    }


}
