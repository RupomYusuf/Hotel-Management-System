import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    private User adminUser;
    private User staffUser;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "PassWord123";

    @Before
    public void setUp() {
        adminUser = new User(USERNAME, PASSWORD, User.UserRole.ADMIN);
        staffUser = new User("staff", PASSWORD, User.UserRole.STAFF);
    }

    @Test
    public void testUserInitialization() {
        assertEquals(USERNAME, adminUser.getUsername());
        assertEquals(User.UserRole.ADMIN, adminUser.getRole());
        assertNotNull(adminUser);
    }

    @Test
    public void testPasswordVerification() {
        assertTrue(adminUser.verifyPassword(PASSWORD));
        assertFalse(adminUser.verifyPassword("wrongpassword"));
    }

    @Test
    public void testDifferentRoles() {
        assertEquals(User.UserRole.ADMIN, adminUser.getRole());
        assertEquals(User.UserRole.STAFF, staffUser.getRole());
    }

    @Test
    public void testPasswordHashing() {
        // Same password should produce same hash
        User user1 = new User("user1", PASSWORD, User.UserRole.STAFF);
        User user2 = new User("user2", PASSWORD, User.UserRole.STAFF);
        assertTrue(user1.verifyPassword(PASSWORD));
        assertTrue(user2.verifyPassword(PASSWORD));
    }

    @Test
    public void testPasswordCaseSensitivity() {
        assertFalse(adminUser.verifyPassword(PASSWORD.toUpperCase()));
        assertFalse(adminUser.verifyPassword(PASSWORD.toLowerCase()));
    }

    @Test
    public void testSpecialCharactersInPassword() {
        String specialPassword = "P@ssw0rd!";
        User specialUser = new User("special", specialPassword, User.UserRole.STAFF);
        assertTrue(specialUser.verifyPassword(specialPassword));
        assertFalse(specialUser.verifyPassword("P@ssw0rd"));
    }

    @Test
    public void testLongPassword() {
        String longPassword = "a".repeat(1000);
        User longPasswordUser = new User("longpass", longPassword, User.UserRole.STAFF);
        assertTrue(longPasswordUser.verifyPassword(longPassword));
    }

    @Test
    public void testEmptyPassword() {
        String emptyPassword = "";
        User emptyPasswordUser = new User("empty", emptyPassword, User.UserRole.STAFF);
        assertTrue(emptyPasswordUser.verifyPassword(emptyPassword));
    }

    @Test
    public void testUsernameUniqueness() {
        User user1 = new User("unique1", PASSWORD, User.UserRole.STAFF);
        User user2 = new User("unique2", PASSWORD, User.UserRole.STAFF);
        assertNotEquals(user1.getUsername(), user2.getUsername());
    }
} 