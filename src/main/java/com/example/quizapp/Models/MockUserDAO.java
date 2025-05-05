package com.example.quizapp.Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class MockUserDAO implements IUserDAO{

     // A static list of users to be stored as a mock database

    private final ArrayList<User> users = new ArrayList<>();
    private static int autoIncrementedId = 0;

    public MockUserDAO() {
        // Add some initial users to the mock database
        addUser(new User("admin", "admin@example.com", "admin123"));
        addUser(new User("user1", "user1@example.com", "password1"));
        addUser(new User("user2", "user2@example.com", "password2"));
    }
    @Override
    public void addUser(User user) {
        if (isEmailRegistered(user.getEmail())) {
            System.out.println("Email already registered. Cannot add user.");
            return;
        }

        user.setUserID(autoIncrementedId++);
        // Hash password before storing
        user.setPassword(hashPassword(user.getPassword()));
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID() == user.getUserID()) {
                // [FIXED] Avoid double-hashing already hashed password
                String newPassword = user.getPassword();
                if (!newPassword.equals(users.get(i).getPassword())) {
                    newPassword = hashPassword(newPassword);
                }
                user.setPassword(newPassword);
                users.set(i, user);
                break;
            }
        }
    }
    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
    @Override
    public boolean validateCredentials(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                String hashedInput = hashPassword(password);// Hashes the input password string
                return Objects.equals(user.getPassword(), hashedInput);
            }
        }
        return false; // No user found with the given username

    }
    public boolean isSignupValid(User user) {
        // Check for duplicate email
        for (User existingUser : users) {
            if (existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                System.out.println("Email already registered");
                return false;
            }
        }

        // Validate password
        String password = user.getPassword();
        if (password.length() < 8) {
            System.out.println("Password should be at least 8 characters long");
            return false;
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            System.out.println("Password must contain both letters and digits");
            return false;
        }

        return true;
    }
    public boolean isEmailRegistered(String email) {
        for (User existingUser : users) {
            if (existingUser.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    return user;
                }
            }
            return null; // or throw an exception if preferred
    }


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 hashing not available", e);
        }
    }
}



