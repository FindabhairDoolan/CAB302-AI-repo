import com.example.quizapp.Models.AuthManager;
import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.MockUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupTest {

    private AuthManager authManager;
    private IUserDAO UserDAO;

    @BeforeEach
    public void setUp() {
        AuthManager.setTestInstance(new MockUserDAO());
        authManager = AuthManager.getInstance();
        authManager.disableAlertsForTesting = true;
    }

    @Test
    public void testSuccessfulSignUp() {
        boolean successfulSignup = authManager.signup("admin2", "admin2@example.com", "admin1234");
        assertTrue(successfulSignup);
    }

    @Test
    public void testRejectEmptyUsername() {
        boolean successfulSignup = authManager.signup("", "admin@example.com", "admin12345");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectEmptyEmail() {
        boolean successfulSignup = authManager.signup("username1", "", "admin12345");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectEmptyPassword() {
        boolean successfulSignup = authManager.signup("username2", "admin@example.com", "");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectExistingEmail() {
        boolean successfulSignup = authManager.signup("user5", "admin@example.com", "adminPassword1");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectInvalidEmail() {
        boolean successfulSignup = authManager.signup("user5", "admin", "adminPassword1");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectTooShortPassword() {
        boolean successfulSignup = authManager.signup("user5", "admin4@example.com", "pass12");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectTextOnlyPassword() {
        boolean successfulSignup = authManager.signup("user5", "admin4@example.com", "adminpassword");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectNumbersOnlyPassword() {
        boolean successfulSignup = authManager.signup("user5", "admin4@example.com", "123456789");
        assertFalse(successfulSignup);
    }


}