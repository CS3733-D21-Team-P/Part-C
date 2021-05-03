package edu.wpi.p.userstate;

import edu.wpi.p.database.DBUser;

public class LoggedOutState extends UserState {
    private User user;
    public LoggedOutState() {
        super();
    }

    @Override
    void beGuest() {
        User.getInstance().changeState(new GuestState());
    }

    @Override
    boolean isLoggedIn() {
        return false;
    }

    @Override
    void login(String username, String password) throws LoginException {
        user = User.getInstance();

        DBUser dbUser = new DBUser();
        if(password.equals("")){
            throw new LoginException("Wrong Information", "Please enter the Password");
        }

        if ((dbUser.checkUsername(username)).equals(password)) {
            user.setPermissions(dbUser.checkIdentity(username));
            user.changeState(new LoggedInState());
        }else {
            throw new LoginException("Wrong Information", "Please enter the correct Username and Password");
        }

    }

}
