package edu.wpi.p.userstate;

import edu.wpi.p.database.DBUser;

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

        DBUser dbuser = new DBUser();
        String parkingID = dbuser.checkParkingNodeID(username);
        User.getInstance().setParkingNodeID(parkingID);
    }


}
