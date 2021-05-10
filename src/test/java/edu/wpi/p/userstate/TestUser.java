package edu.wpi.p.userstate;

import edu.wpi.p.database.DBUser;
import edu.wpi.p.database.DatabaseInterface;
import edu.wpi.p.database.UserFromDB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUser {

    private static DBUser dbuser;
    static UserFromDB admin = new UserFromDB("Admin", "admin", "admin", "Admin");
    static UserFromDB aemployee = new UserFromDB("David", "David", "123456", "Employee");
    static UserFromDB bemployee = new UserFromDB("Alex", "Alex", "123456", "Employee");
    static UserFromDB cemployee = new UserFromDB("Andrew", "Andrew", "123456", "Employee");
    static UserFromDB demployee = new UserFromDB("Nina", "Nina", "123456", "Employee");
    static UserFromDB eemployee = new UserFromDB("Loren", "Loren", "123456", "Employee");
    static UserFromDB femployee = new UserFromDB("Yoko", "Yoko", "123456", "Employee");
    static UserFromDB gemployee = new UserFromDB("Yang", "Yang", "123456", "Employee");
    static UserFromDB hemployee = new UserFromDB("Rohan", "Rohan", "123456", "Employee");
    static UserFromDB iemployee = new UserFromDB("Nicolas", "Nicolas", "123456", "Employee");
    static UserFromDB jemployee = new UserFromDB("Ian", "Ian", "123456", "Employee");

    @BeforeAll
    static void setup() {

        DatabaseInterface.init(false);
        List<String> tableNames = DatabaseInterface.getTableNames();
        if (!tableNames.contains("USERS")) {
            dbuser = new DBUser();
            dbuser.addUser(admin);
            dbuser.addUser(aemployee);
            dbuser.addUser(bemployee);
            dbuser.addUser(cemployee);
            dbuser.addUser(demployee);
            dbuser.addUser(eemployee);
            dbuser.addUser(femployee);
            dbuser.addUser(gemployee);
            dbuser.addUser(hemployee);
            dbuser.addUser(iemployee);
            dbuser.addUser(jemployee);
        } else {
            dbuser = new DBUser();
        }
    }

    @BeforeEach
    void setupEach() throws Exception {
        // this is very bad, but gets around the weirdness of the
        // singleton by force the creation of a new user each time
        Field instance = User.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }
    @Test
    void testUserFields() {
        User.getInstance().setName("test name");
        User.getInstance().setPermissions("admin");
        assertEquals("test name", User.getInstance().getName());
        assertEquals("admin", User.getInstance().getPermissions());
    }

    @Test
    void testUserStateLoginAdmin(){
        try {
            User.getInstance().login("admin", "admin");
            assertEquals(true, User.getInstance().isLoggedIn());
            assertEquals("Admin", User.getInstance().getPermissions());
        } catch (LoginException loginException) {
            assertTrue(false);
        }

    }

    @Test
    void testUserStateLoginEmployee(){
        try {
            User.getInstance().login("Alex", "123456");
            assertEquals(true, User.getInstance().isLoggedIn());
            assertEquals("Employee", User.getInstance().getPermissions());
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
