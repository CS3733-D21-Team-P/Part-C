package edu.wpi.p.userstate;

public class GuestState extends UserState {

    public GuestState() {
        super();
        // TODO: 5/7/2021 check db for user covid approval
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
    void login(String username, String password){
        // can't log in while a guest, must log out first
    }

    @Override
    void logout() {
        User u = User.getInstance();
        u.reset();
        u.changeState(new LoggedOutState());
    }


}
