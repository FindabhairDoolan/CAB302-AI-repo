package com.example.quizapp.Models;

import java.time.LocalDateTime;

public class QuizAttempt {
    private int id, quizID, userID, scores;
    private LocalDateTime attemptTime;



    public QuizAttempt(int quizID, int userID, int score) {
        this.quizID = quizID;
        this.userID = userID;
        this.scores = scores;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getScore() { return scores; }
    public void setScore(int score) { this.scores = scores; }

    public LocalDateTime getAttemptTime() { return attemptTime; }
    public void setAttemptTime(LocalDateTime attemptTime) { this.attemptTime = attemptTime; }

    @Override
    public String toString() {
        return String.format("QuizAttempt{id=%d, quizID=%d, userID=%d, score=%d, attemptTime=%s}",
                id, quizID, userID, scores, attemptTime);
    }
}
