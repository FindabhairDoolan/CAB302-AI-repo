package com.example.quizapp.Models;

import java.sql.Timestamp;
import java.util.List;

public class QuizWithScore {
    private Quiz quiz;
    private List<Integer> scores;
    private Timestamp time;

    public QuizWithScore(Quiz quiz, List<Integer> scores) {
        this.quiz = quiz;
        this.scores = scores;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public List<Integer> getScores() {
        return scores;
    }

}
