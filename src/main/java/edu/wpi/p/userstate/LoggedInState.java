package edu.wpi.p.userstate;

import edu.wpi.p.database.DBUser;

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
        DBUser dbuser = new DBUser();
        String parkingID = dbuser.checkParkingNodeID(username);
        User.getInstance().setParkingNodeID(parkingID);
    }

    @Override
    void logout() {
        User.getInstance().changeState(new LoggedOutState());
        User.getInstance().setParkingNodeID("");
    }
}
