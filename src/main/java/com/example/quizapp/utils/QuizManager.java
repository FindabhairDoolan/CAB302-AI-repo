package com.example.quizapp.utils;

import com.example.quizapp.Models.Quiz;

public class QuizManager {
    private static QuizManager instance;
    private Quiz currentQuiz;

    private QuizManager() {}

    public static QuizManager getInstance() {
        if (instance == null) {
            instance = new QuizManager();
        }
        return instance;
    }

    /**
     * Given quiz will be the current quiz in use
     * @param quiz sets quiz to the current quiz
     */
    public void setCurrentQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
    }

    /**
     * Returns the current quiz
     * @return Quiz: The current quiz
     */
    public Quiz getCurrentQuiz() {
        return this.currentQuiz;
    }
}
