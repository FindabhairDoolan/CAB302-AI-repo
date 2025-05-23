package com.example.quizapp.Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class QuizAttempt {
    private int id, quizID, userID;
    private double score;
    private LocalDateTime attemptTime;
    private List<String> selectedAnswers;



    public QuizAttempt(int quizID, int userID, double score, List<String> selectedAnswers) {
        this.quizID = quizID;
        this.userID = userID;
        this.score = score;
        this.selectedAnswers = selectedAnswers;
    }


    public List<String> getAnswers() {
        return this.selectedAnswers;
    }

    public void setAnswers(List<String> answers) {
        this.selectedAnswers = answers;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public LocalDateTime getAttemptTime() { return attemptTime; }
    public void setAttemptTime(LocalDateTime attemptTime) { this.attemptTime = attemptTime; }

    @Override
    public String toString() {
        return String.format("QuizAttempt{id=%d, quizID=%d, userID=%d, score=%d, attemptTime=%s}",
                id, quizID, userID, score, attemptTime);
    }
}
