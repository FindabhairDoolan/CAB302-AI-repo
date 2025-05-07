import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.AuthManager;
import com.example.quizapp.Models.MockUserDAO;
import org.junit.jupiter.api.BeforeEach;
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