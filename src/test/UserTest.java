import com.example.quizapp.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private static final String userName = "alice";
    private static final String email = "alice@example.com";
    private static final String password = "password123";

    private static final String userName_2 = "bob";
    private static final String email_2 = "bob@example.com";
    private static final String password_2 = "secure456";

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(userName, email, password);
    }

    @Test
    public void testSetUserID() {
        user.setUserID(10);
        assertEquals(10, user.getUserID());
    }

    @Test
    public void testGetUserName() {
        assertEquals(userName, user.getUserName());
    }

    @Test
    public void testSetUserName() {
        user.setUserName(userName_2);
        assertEquals(userName_2, user.getUserName());
    }

    @Test
    public void testGetEmail() {
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail(email_2);
        assertEquals(email_2, user.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword(password_2);
        assertEquals(password_2, user.getPassword());
    }
}
