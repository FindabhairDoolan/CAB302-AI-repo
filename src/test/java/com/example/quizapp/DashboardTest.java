package com.example.quizapp;
import static org.junit.jupiter.api.Assertions.*;

import com.example.quizapp.Models.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DashboardTest {

    private List<Quiz> allQuizzes;

    @BeforeEach
    public void setup() {
        allQuizzes = new ArrayList<>();
        allQuizzes.add(new Quiz("Science Quiz", "Science", "Physics", 30, "Easy", "10", "USA", "Public", 1));
        allQuizzes.add(new Quiz("History Quiz", "History", "WWII", 25, "Medium", "11", "UK", "Public", 2));
        allQuizzes.add(new Quiz("Math Quiz", "Math", "Algebra", 20, "Hard", "12", "USA", "Public", 3));
        allQuizzes.add(new Quiz("Science Advanced", "Science", "Chemistry", 35, "Hard", "12", "USA", "Public", 1));
    }

    @Test
    public void testSearchQuizByTopic() {
        // Simulate search by topic "Science"
        List<Quiz> results = searchQuiz(allQuizzes, "Science");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(q -> q.getSubject().equalsIgnoreCase("Science")));
    }

    @Test
    public void testSearchQuizByPartialName() {
        // Simulate search by partial name "Quiz"
        List<Quiz> results = searchQuiz(allQuizzes, "Quiz");

        assertEquals(3, results.size()); // Science Quiz, History Quiz, Math Quiz
    }

    @Test
    public void testFilterQuizzesByDifficulty() {
        // Filter quizzes by difficulty "Hard"
        List<Quiz> hardQuizzes = filterQuizzesByDifficulty(allQuizzes, "Hard");

        assertEquals(2, hardQuizzes.size());
        assertTrue(hardQuizzes.stream().allMatch(q -> q.getDifficulty().equalsIgnoreCase("Hard")));
    }

    // Helper method simulating your search logic (by topic or name)
    private List<Quiz> searchQuiz(List<Quiz> quizzes, String searchTerm) {
        String lowerTerm = searchTerm.toLowerCase();
        List<Quiz> filtered = new ArrayList<>();
        for (Quiz q : quizzes) {
            if (q.getTopic().toLowerCase().contains(lowerTerm) || q.getName().toLowerCase().contains(lowerTerm)) {
                filtered.add(q);
            }
        }
        return filtered;
    }

    // Helper method simulating filter by difficulty
    private List<Quiz> filterQuizzesByDifficulty(List<Quiz> quizzes, String difficulty) {
        List<Quiz> filtered = new ArrayList<>();
        for (Quiz q : quizzes) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filtered.add(q);
            }
        }
        return filtered;
    }
}
