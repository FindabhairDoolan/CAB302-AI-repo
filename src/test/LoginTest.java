import com.example.quizapp.Controllers.LoginController;
import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.MockUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private LoginController loginController;
    private IUserDAO UserDAO;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController(new MockUserDAO());

    }

    @Test
    public void testLoginWithCorrectCredentials() {
        boolean successfulLogin = loginController.login("admin@example.com", "admin123");
        assertTrue(successfulLogin);
    }

    @Test
    public void testIncorrectPassword() {
        boolean successfulLogin = loginController.login("admin@example.com", "admin12345");
        assertFalse(successfulLogin);
    }

    @Test
    public void testIncorrectUsername() {
        boolean successfulLogin = loginController.login("admin@example.con", "admin123");
        assertFalse(successfulLogin);
    }
}