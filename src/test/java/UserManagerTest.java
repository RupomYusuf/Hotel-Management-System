import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserManagerTest {
    private UserManager userManager;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";

    @Before
    public void setUp() {
        userManager = new UserManager();
        userManager.clearUsers();
    }

    @Test
    public void testRegisterUser() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertTrue(userManager.login(USERNAME, PASSWORD));
    }

    @Test
    public void testLogin() {
        userManager.registerUser(USERNAME, PASSWORD, "STAFF");
        assertTrue(userManager.login(USERNAME, PASSWORD));
        assertFalse(userManager.login(USERNAME, "wrongpass"));
        assertFalse(userManager.login("nonexistent", PASSWORD));
    }

    @Test
    public void testGetUserRole() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertEquals("ADMIN", userManager.getUserRole(USERNAME));
    }

    @Test
    public void testRegisterDifferentRoles() {
        userManager.registerUser("admin", "admin123", "ADMIN");
        userManager.registerUser("staff", "staff123", "STAFF");
        assertEquals("ADMIN", userManager.getUserRole("admin"));
        assertEquals("STAFF", userManager.getUserRole("staff"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithEmptyUsername() {
        userManager.registerUser("", PASSWORD, "ADMIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithEmptyPassword() {
        userManager.registerUser(USERNAME, "", "ADMIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithSpecialCharacters() {
        userManager.registerUser("user@name", PASSWORD, "ADMIN");
    }

    @Test
    public void testCaseSensitiveUsername() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertFalse(userManager.login(USERNAME.toUpperCase(), PASSWORD));
    }

    @Test
    public void testMultipleRegistrations() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        userManager.registerUser("user2", PASSWORD, "STAFF");
        assertEquals("ADMIN", userManager.getUserRole(USERNAME));
        assertEquals("STAFF", userManager.getUserRole("user2"));
    }

    @Test
    public void testGetUserRoleAfterLogin() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertTrue(userManager.login(USERNAME, PASSWORD));
        assertEquals("ADMIN", userManager.getUserRole(USERNAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithNullUsername() {
        userManager.registerUser(null, PASSWORD, "ADMIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithNullPassword() {
        userManager.registerUser(USERNAME, null, "ADMIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithNullRole() {
        userManager.registerUser(USERNAME, PASSWORD, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithInvalidRole() {
        userManager.registerUser(USERNAME, PASSWORD, "INVALID_ROLE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithCaseInsensitiveRole() {
        userManager.registerUser(USERNAME, PASSWORD, "admin"); // lowercase
    }

    @Test
    public void testLoginWithNullUsername() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertFalse(userManager.login(null, PASSWORD));
    }

    @Test
    public void testLoginWithNullPassword() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertFalse(userManager.login(USERNAME, null));
    }

    @Test
    public void testGetUserRoleWithNullUsername() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        try {
            userManager.getUserRole(null);
            fail("Expected IllegalArgumentException for null username");
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testGetUserRoleWithNonExistentUser() {
        try {
            userManager.getUserRole("nonexistent");
            fail("Expected IllegalArgumentException for non-existent user");
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testMultipleLoginAttempts() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        assertTrue(userManager.login(USERNAME, PASSWORD));
        assertTrue(userManager.login(USERNAME, PASSWORD)); // Should still work
        assertFalse(userManager.login(USERNAME, "wrongpass"));
        assertTrue(userManager.login(USERNAME, PASSWORD)); // Should work again
    }

    @Test
    public void testUsernameLengthLimits() {
        // Test with very long username
        String longUsername = "a".repeat(100);
        userManager.registerUser(longUsername, PASSWORD, "ADMIN");
        assertTrue(userManager.login(longUsername, PASSWORD));
        assertEquals("ADMIN", userManager.getUserRole(longUsername));
    }

    @Test
    public void testPasswordLengthLimits() {
        // Test with very long password
        String longPassword = "a".repeat(1000);
        userManager.registerUser(USERNAME, longPassword, "ADMIN");
        assertTrue(userManager.login(USERNAME, longPassword));
    }

    @Test
    public void testConcurrentUserRegistration() {
        userManager.registerUser("user1", PASSWORD, "ADMIN");
        userManager.registerUser("user2", PASSWORD, "STAFF");
        userManager.registerUser("user3", PASSWORD, "ADMIN");
        
        assertEquals("ADMIN", userManager.getUserRole("user1"));
        assertEquals("STAFF", userManager.getUserRole("user2"));
        assertEquals("ADMIN", userManager.getUserRole("user3"));
        
        assertTrue(userManager.login("user1", PASSWORD));
        assertTrue(userManager.login("user2", PASSWORD));
        assertTrue(userManager.login("user3", PASSWORD));
    }

    @Test
    public void testUsernameUniquenessCaseSensitivity() {
        userManager.registerUser(USERNAME, PASSWORD, "ADMIN");
        try {
            userManager.registerUser(USERNAME.toUpperCase(), PASSWORD, "ADMIN");
            fail("Expected IllegalArgumentException for duplicate username");
        } catch (IllegalArgumentException e) {
            assertEquals("Username already exists", e.getMessage());
        }
    }
} 