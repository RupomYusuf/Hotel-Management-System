//package com.company;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class User implements Serializable {
    private String username;
    private String password;
    private UserRole role;
    
    public enum UserRole {
        ADMIN,
        STAFF
    }
    
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = hashPassword(password);
        this.role = role;
    }
    
    private String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public boolean verifyPassword(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(hashPassword(password));
    }
    
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
} 