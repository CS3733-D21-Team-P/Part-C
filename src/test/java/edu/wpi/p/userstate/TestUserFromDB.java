package edu.wpi.p.userstate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserFromDB {
    private User user = new User();

    @Test
    void testUserFields() {
        User.getInstance().setName("test name");
        User.getInstance().setPermissions("admin");
        assertEquals("test name", User.getInstance().getName());
        assertEquals("admin", User.getInstance().getPermissions());
    }

    @Test
    void testUserStateLogin(){
        try {
            User.getInstance().login("admin", "admin");
            assertEquals(true, User.getInstance().isLoggedIn());
        } catch (LoginException loginException) {
            assertTrue(false);
        }

    }
    @Test
    void testUserStateLogout(){
        try {
            User.getInstance().login("admin", "admin");
        } catch (LoginException loginException) {
            assertTrue(false);
        }
        User.getInstance().logout();
        assertEquals(false, User.getInstance().isLoggedIn());
    }
}
