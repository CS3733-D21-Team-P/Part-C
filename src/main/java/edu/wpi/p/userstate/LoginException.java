package edu.wpi.p.userstate;

public class LoginException extends Exception {
    private String title;

    public LoginException(String title, String message) {
        super(message);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
