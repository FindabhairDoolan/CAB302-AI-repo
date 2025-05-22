package com.example.quizapp.Models;

import java.sql.Timestamp;
import java.util.List;

public class QuizWithScore {
    private Quiz quiz;
    private int scores;
    private Timestamp time;

    public QuizWithScore(Quiz quiz, int scores) {
        this.quiz = quiz;
        this.scores = scores;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getScores() {
        return scores;
    }


}
