package com.example.quizapp;

import static org.junit.jupiter.api.Assertions.*;

import com.example.quizapp.Models.MockQuizDAO;
import com.example.quizapp.Models.Quiz;
import com.example.quizapp.Models.User;
import org.junit.jupiter.api.*;

public class GenerateUniqueQuizTitleTest {
    private MockQuizDAO mockQuizDAO;
    private User user;
    private static final String quizName = "Math Quiz";
    private static final String subject = "Mathematics";
    private static final String quizTopic = "Math basics";
    private static final String quizMode = "Practice";
    private static final String difficulty = "Hard";
    private static final String yearLevel = "year 10";
    private static final String country = "Australia";
    private static final int creatorID = 1;

    @BeforeEach
    void setUp() {
        mockQuizDAO = new MockQuizDAO();
        user = new User("Charlie", "charlie@example.com", "password2");
        user.setUserID(1); //Set user ID without database for testing
    }

    // Method to test the unique title logic with mock database
    private String generateUniqueQuizTitle(String title) {
        Quiz existingQuiz = mockQuizDAO.getQuizByName(title);

        if (existingQuiz != null && existingQuiz.getCreatorID() == user.getUserID()) {
            int duplicateNum = 1;
            String titleDuplicate = title + duplicateNum;
            while (mockQuizDAO.getQuizByName(titleDuplicate) != null) {
                duplicateNum++;
                titleDuplicate = title + duplicateNum;
            }
            title = titleDuplicate;
        }

        return title;
    }

    @Test
    void testUniqueQuizTitle_NoDuplicate() {
        String title = generateUniqueQuizTitle("Math Quiz");
        assertEquals("Math Quiz", title);
    }

    @Test
    void testUniqueQuizTitle_WithOneDuplicate() {
        mockQuizDAO.saveQuiz(new Quiz(quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, creatorID)); // Existing Quiz by User ID 1

        String title = generateUniqueQuizTitle("Math Quiz");
        assertEquals("Math Quiz1", title);
    }

    @Test
    void testUniqueQuizTitle_WithMultipleDuplicates() {
        mockQuizDAO.saveQuiz(new Quiz(quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, creatorID));
        //Assume first duplicate already went through the unique title method
        mockQuizDAO.saveQuiz(new Quiz("Math Quiz1", subject, quizTopic, quizMode, difficulty, yearLevel, country, creatorID)); // Two existing duplicates

        String title = generateUniqueQuizTitle("Math Quiz");
        assertEquals("Math Quiz2", title);
    }

    @Test
    void testUniqueQuizTitle_DifferentUser() {
        mockQuizDAO.saveQuiz(new Quiz(quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, 2)); // Different User (ID 2)

        String title = generateUniqueQuizTitle("Math Quiz");
        assertEquals("Math Quiz", title);
    }
}
