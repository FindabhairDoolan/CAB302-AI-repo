import com.example.quizapp.Models.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizTest {
    private static final String quizName = "Science Quiz";
    private static final String quizTopic = "Physics";
    private static final String quizMode = "Practice";
    private static final String difficulty = "Hard";
    private static final String yearLevel = "10";
    private static final String country = "Australia";
    private static final int creatorID = 101;

    private static final String quizName_2 = "Math Quiz";
    private static final String quizTopic_2 = "Algebra";
    private static final String quizMode_2 = "Practice";
    private static final String difficulty_2 = "Easy";
    private static final String yearLevel_2 = "8";
    private static final String country_2 = "New Zealand";
    private static final int creatorID_2 = 202;

    private Quiz quiz;

    @BeforeEach
    public void setUp() {
        quiz = new Quiz(quizName, quizTopic, quizMode, difficulty, yearLevel, country, creatorID);
    }

    @Test
    public void testSetQuizID() {
        quiz.setQuizID(5);
        assertEquals(5, quiz.getQuizID());
    }

    @Test
    public void testGetQuizName() {
        assertEquals(quizName, quiz.getQuizName());
    }

    @Test
    public void testSetQuizName() {
        quiz.setQuizName(quizName_2);
        assertEquals(quizName_2, quiz.getQuizName());
    }

    @Test
    public void testGetQuizTopic() {
        assertEquals(quizTopic, quiz.getQuizTopic());
    }

    @Test
    public void testSetQuizTopic() {
        quiz.setQuizTopic(quizTopic_2);
        assertEquals(quizTopic_2, quiz.getQuizTopic());
    }

    @Test
    public void testGetQuizMode() {
        assertEquals(quizMode, quiz.getQuizMode());
    }

    @Test
    public void testSetQuizMode() {
        quiz.setQuizMode(quizMode_2);
        assertEquals(quizMode_2, quiz.getQuizMode());
    }

    @Test
    public void testGetDifficulty() {
        assertEquals(difficulty, quiz.getDifficulty());
    }

    @Test
    public void testSetDifficulty() {
        quiz.setDifficulty(difficulty_2);
        assertEquals(difficulty_2, quiz.getDifficulty());
    }

    @Test
    public void testGetYearLevel() {
        assertEquals(yearLevel, quiz.getYearLevel());
    }

    @Test
    public void testSetYearLevel() {
        quiz.setYearLevel(yearLevel_2);
        assertEquals(yearLevel_2, quiz.getYearLevel());
    }

    @Test
    public void testGetCountry() {
        assertEquals(country, quiz.getCountry());
    }

    @Test
    public void testSetCountry() {
        quiz.setCountry(country_2);
        assertEquals(country_2, quiz.getCountry());
    }

    @Test
    public void testGetCreatorID() {
        assertEquals(creatorID, quiz.getCreatorID());
    }

    @Test
    public void testSetCreatorID() {
        quiz.setCreatorID(creatorID_2);
        assertEquals(creatorID_2, quiz.getCreatorID());
    }
}
