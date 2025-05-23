package com.example.quizapp.Models;

import java.sql.Timestamp;
import java.util.List;

public class QuizWithScore {
    private Quiz quiz;
    private  int score;
    private Timestamp time;

    public QuizWithScore(Quiz quiz, int score) {
        this.quiz = quiz;
        this.score = score;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getScore() {
        return score;
    }


}
