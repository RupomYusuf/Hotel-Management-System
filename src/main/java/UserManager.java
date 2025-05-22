import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final Map<String, User> users = new HashMap<>();

    // For testing purposes
    public void clearUsers() {
        users.clear();
    }

    // Register a new user
    public void registerUser(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Username can only contain letters and numbers");
        }
        // Check for existing username case-insensitively
        for (String existingUsername : users.keySet()) {
            if (existingUsername.equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (role == null || (!role.equals("ADMIN") && !role.equals("STAFF"))) {
            throw new IllegalArgumentException("Invalid role. Must be ADMIN or STAFF");
        }
        User.UserRole userRole = role.equals("ADMIN") ? User.UserRole.ADMIN : User.UserRole.STAFF;
        users.put(username, new User(username, password, userRole));
    }

    // Login verification
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        User user = users.get(username);
        if (user == null) {
            return false; // User not found
        }
        return user.verifyPassword(password);
    }

    // Get user role (useful for authorization)
    public String getUserRole(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user.getRole().toString();
    }
} 