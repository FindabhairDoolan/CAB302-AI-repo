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
        boolean successfulSignup = signupController.signup("", "admin@example.com", "admin12345");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectEmptyEmail() {
        boolean successfulSignup = signupController.signup("username1", "", "admin12345");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectEmptyPassword() {
        boolean successfulSignup = signupController.signup("username2", "admin@example.com", "");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectExistingEmail() {
        boolean successfulSignup = signupController.signup("user5", "admin@example.com", "adminPassword1");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectInvalidEmail() {
        boolean successfulSignup = signupController.signup("user5", "admin", "adminPassword1");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectTooShortPassword() {
        boolean successfulSignup = signupController.signup("user5", "admin4@example.com", "pass12");
        assertFalse(successfulSignup);
    }


    @Test
    public void testRejectTextOnlyPassword() {
        boolean successfulSignup = signupController.signup("user5", "admin4@example.com", "adminpassword");
        assertFalse(successfulSignup);
    }

    @Test
    public void testRejectNumbersOnlyPassword() {
        boolean successfulSignup = signupController.signup("user5", "admin4@example.com", "123456789");
        assertFalse(successfulSignup);
    }


}