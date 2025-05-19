package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.List;

public class MockQuizDAO {
    private List<Quiz> quizzes = new ArrayList<>();

    public Quiz getQuizByName(String name) {
        for (Quiz quiz : quizzes) {
            if (quiz.getName().equals(name)) {
                return quiz;
            }
        }
        return null;
    }

    public void saveQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }
}
