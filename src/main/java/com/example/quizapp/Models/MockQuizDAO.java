package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock Quiz DAO for testing
 */
public class MockQuizDAO {
    private List<Quiz> quizzes = new ArrayList<>();

    /**
     * Gets the quiz from the quizzes list by searching for its name
     * @param name the name of the quiz
     * @return the quiz if it exists, otherwise return null
     */
    public Quiz getQuizByName(String name) {
        for (Quiz quiz : quizzes) {
            if (quiz.getName().equals(name)) {
                return quiz;
            }
        }
        return null;
    }

    /**
     * Save the quiz to the quizzes list
     * @param quiz the quiz to be stored
     */
    public void saveQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }
}
