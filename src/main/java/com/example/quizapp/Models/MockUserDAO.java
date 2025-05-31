package com.example.quizapp.Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
/**
 * A mock implementation of the IUserDAO interface to simulate user data access operations.
 * Stores users in an in-memory list and provides user management functionalities such as
 * adding users, validating credentials, and checking registration.
 */
public class MockUserDAO implements IUserDAO{

    /**
     * A list to act as a mock database for storing User objects.
     */
    private final ArrayList<User> users = new ArrayList<>();
    /**
     * Static counter for assigning unique user IDs automatically.
     */
    private static int autoIncrementedId = 0;

    /**
     * Constructs a new MockUserDAO and populates it with some initial users.
     * The initial users have hardcoded usernames, emails, and hashed passwords.
     */
    public MockUserDAO() {
        // Add some initial users to the mock database
        User user1 = new User("admin", "admin@example.com", hashPassword("admin123"));
        User user2 = new User("user1", "user1@example.com", hashPassword("password1"));
        User user3 = new User("user2", "user2@example.com", hashPassword("password2"));
        user1.setUserID(1);
        user2.setUserID(2);
        user3.setUserID(3);
        addUser(user1);
        addUser(user2);
        addUser(user3);
    }

    /**
     * Adds a new user to the mock database if the email is not already registered.
     * Automatically assigns a unique user ID.
     *
     * @param user the User object to add
     */
    @Override
    public void addUser(User user) {
        if (isEmailRegistered(user.getEmail())) {
            System.out.println("Email already registered. Cannot add user.");
            return;
        }

        user.setUserID(autoIncrementedId++);
        users.add(user);
    }

    /**
     * Validates user login credentials by checking if the email exists
     * and the password matches the stored password.
     *
     * @param email the user's email address
     * @param password the password to validate
     * @return true if credentials are valid, false otherwise
     */
    @Override
    public boolean validateCredentials(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user.getPassword().equals(password);
            }
        }
        return false; // No user found with the given username

    }
    /**
     * Checks if the user's signup data is valid.
     * Validation includes checking for duplicate email,
     * password length, and ensuring password contains both letters and digits.
     *
     * @param user the User object containing signup data
     * @return true if signup is valid, false otherwise
     */
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
    /**
     * Checks if an email is already registered in the mock database.
     *
     * @param email the email to check
     * @return true if email is registered, false otherwise
     */
    public boolean isEmailRegistered(String email) {
        for (User existingUser : users) {
            if (existingUser.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a User object by their email address.
     *
     * @param email the email of the user to retrieve
     * @return the User object if found, or null if no matching user exists
     */
    @Override
    public User getUserByEmail(String email) {
            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    return user;
                }
            }
            return null; // or throw an exception if preferred
    }

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * @param password the plaintext password to hash
     * @return the hashed password as a hexadecimal string
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
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



