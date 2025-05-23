package com.example.quizapp.Models;

import java.sql.Timestamp;
import java.util.List;

public class QuizWithScore {
    private Quiz quiz;
    private  List<Integer> score;
    private Timestamp time;

    public QuizWithScore(Quiz quiz, List<Integer> scores) {
        this.quiz = quiz;
        this.score = scores;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public List<Integer> getScore() {
        return score;
    }


}
