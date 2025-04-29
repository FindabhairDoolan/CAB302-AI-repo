package com.example.quizapp.Models;

import java.time.LocalDateTime;

public class QuizAttempt {
    private int id, quizID, userID, score;
    private LocalDateTime attemptTime;

    public QuizAttempt(int quizID, int userID, int score) {
        this.quizID = quizID;
        this.userID = userID;
        this.score = score;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDateTime getAttemptTime() { return attemptTime; }
    public void setAttemptTime(LocalDateTime attemptTime) { this.attemptTime = attemptTime; }

    @Override
    public String toString() {
        return String.format("QuizAttempt{id=%d, quizID=%d, userID=%d, score=%d, attemptTime=%s}",
                id, quizID, userID, score, attemptTime);
    }
}
