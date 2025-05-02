import com.example.quizapp.Controllers.LoginController;
import com.example.quizapp.Controllers.SignupController;
import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.MockUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupTest {

    private SignupController signupController;
    private IUserDAO UserDAO;

    @BeforeEach
    public void setUp() {
        signupController = new SignupController(new MockUserDAO());
        signupController.disableAlertsForTesting = true;
    }

    @Test
    public void testSuccessfulSignUp() {
        boolean successfulSignup = signupController.signup("admin2", "admin2@example.com", "admin1234");
        assertTrue(successfulSignup);
    }

    @Test
    public void testRejectEmptyUsername() {
        boolean successfulLogin = signupController.signup("", "admin@example.com", "admin12345");
        assertFalse(successfulLogin);
    }

    @Test
    public void testRejectEmptyEmail() {
        boolean successfulLogin = signupController.signup("username1", "", "admin12345");
        assertFalse(successfulLogin);
    }


    @Test
    public void testRejectEmptyPassword() {
        boolean successfulLogin = signupController.signup("username2", "admin@example.com", "");
        assertFalse(successfulLogin);
    }


    @Test
    public void testRejectExistingEmail() {
        boolean successfulLogin = signupController.signup("user5", "admin@example.com", "adminPassword1");
        assertFalse(successfulLogin);
    }

    @Test
    public void testRejectInvalidEmail() {
        boolean successfulLogin = signupController.signup("user5", "admin", "adminPassword1");
        assertFalse(successfulLogin);
    }

    @Test
    public void testRejectTooShortPassword() {
        boolean successfulLogin = signupController.signup("user5", "admin4@example.com", "pass12");
        assertFalse(successfulLogin);
    }


    @Test
    public void testRejectTextOnlyPassword() {
        boolean successfulLogin = signupController.signup("user5", "admin4@example.com", "adminpassword");
        assertFalse(successfulLogin);
    }

    @Test
    public void testRejectNumbersOnlyPassword() {
        boolean successfulLogin = signupController.signup("user5", "admin4@example.com", "123456789");
        assertFalse(successfulLogin);
    }


}