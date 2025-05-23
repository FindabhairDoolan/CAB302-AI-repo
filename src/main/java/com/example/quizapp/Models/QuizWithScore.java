package com.example.quizapp.Models;

import java.sql.Timestamp;

public class QuizWithScore {
    private Quiz quiz;
    private QuizAttempt attempt;
    private Timestamp time;

    public QuizWithScore(Quiz quiz, QuizAttempt quizAttempt) {
        this.quiz = quiz;
        this.attempt = quizAttempt;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public double getScore() {
        return attempt.getScore();
    }

    public QuizAttempt getAttempt() {
        return attempt;
    }


}
