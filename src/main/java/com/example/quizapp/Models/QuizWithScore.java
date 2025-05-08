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
        //this.time = time;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public List<Integer> getScores() {
        return scores;
    }

   // public Timestamp getTime() {
   //     return time;
    // }
}
