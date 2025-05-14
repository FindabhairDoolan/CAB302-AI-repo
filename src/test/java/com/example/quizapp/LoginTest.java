package com.example.quizapp;

import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.AuthManager;
import com.example.quizapp.Models.MockUserDAO;
import com.example.quizapp.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private AuthManager authManager;
    private IUserDAO UserDAO;

    @BeforeEach
    public void setUp() {
        AuthManager.setTestInstance(new MockUserDAO());

        authManager = AuthManager.getInstance();
        authManager.disableAlertsForTesting = true;

    }

    private static final String userName = "alice";
    private static final String email = "alice@example.com";
    private static final String password = "password123";

    private static final String userName_2 = "bob";
    private static final String email_2 = "bob@example.com";
    private static final String password_2 = "secure456";

    private User user;

    @Disabled("Temporarily disabled until fixed")
    @Test
    public void testLoginWithCorrectCredentials() {
        boolean successfulLogin = authManager.login("admin@example.com", "admin123");
        assertTrue(successfulLogin);
    }

    @Test
    public void testIncorrectPassword() {
        boolean successfulLogin = authManager.login("admin@example.com", "admin12345");
        assertFalse(successfulLogin);
    }

    @Test
    public void testIncorrectEmail() {
        boolean successfulLogin = authManager.login("admin@example.con", "admin123");
        assertFalse(successfulLogin);
    }
}