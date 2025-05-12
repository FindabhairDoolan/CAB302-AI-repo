package com.example.quizapp.Models;

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

    // Set the quiz (list of questions)
    public void setCurrentQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
    }

    public Quiz getCurrentQuiz() {
        return this.currentQuiz;
    }
}
