package com.example.quizapp;

import com.example.quizapp.utils.AuthManager;
import com.example.quizapp.Models.MockUserDAO;
import com.example.quizapp.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private AuthManager authManager;
    private MockUserDAO mockUserDAO;

    @BeforeEach
    public void setUp() {
        AuthManager.setTestInstance(new MockUserDAO());

        authManager = AuthManager.getInstance();
        authManager.disableAlertsForTesting = true;

    }

    private static final String userName = "admin";
    private static final String email = "admin@example.com";
    private static final String password = "admin123";

    private User user;

    @Test
    public void testLoginWithCorrectCredentials() {
        boolean successfulLogin = authManager.login(email, password);
        assertTrue(successfulLogin);
    }

    @Test
    public void testIncorrectPassword() {
        boolean successfulLogin = authManager.login(email, "admin1234");
        assertFalse(successfulLogin);
    }

    @Test
    public void testIncorrectEmail() {
        boolean successfulLogin = authManager.login("admin@example.con", password);
        assertFalse(successfulLogin);
    }
}