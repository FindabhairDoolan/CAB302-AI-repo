package com.example.quizapp.Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * The QuizAttempt class outlines the attributes of a single quiz attempt by a user
 */
public class QuizAttempt {
    private int id, quizID, userID;
    private double score;
    private int attemptTime;
    private List<String> selectedAnswers;


    /**
     * The constructor for the QuizAttempt class
     * @param quizID the ID of the quiz that was attempted
     * @param userID the ID of the user who took the quiz
     * @param score the score the user received in this attempt
     * @param attemptTime the time taken for the attempt (in seconds)
     * @param selectedAnswers a list of answers selected by the user during the attempt
     */
    public QuizAttempt(int quizID, int userID, double score, int attemptTime, List<String> selectedAnswers) {
        this.quizID = quizID;
        this.userID = userID;
        this.score = score;
        this.attemptTime = attemptTime;
        this.selectedAnswers = selectedAnswers;
    }

    /**
     * Gets the list of answers selected by the user
     * @return the selected answers
     */
    public List<String> getAnswers() {
        return this.selectedAnswers;
    }

    /**
     * Sets the list of answers selected by the user
     * @param answers the selected answers
     */
    public void setAnswers(List<String> answers) {
        this.selectedAnswers = answers;
    }

    /**
     * Gets the ID of the quiz attempt
     * @return the attempt ID
     */
    public int getId() { return id; }

    /**
     * Sets the ID of the quiz attempt
     * @param id the attempt ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the ID of the quiz that was attempted
     * @return the quiz ID
     */
    public int getQuizID() { return quizID; }

    /**
     * Sets the ID of the quiz that was attempted
     * @param quizID the quiz ID
     */
    public void setQuizID(int quizID) { this.quizID = quizID; }

    /**
     * Gets the ID of the user who made the attempt
     * @return the user ID
     */
    public int getUserID() { return userID; }

    /**
     * Sets the ID of the user who made the attempt
     * @param userID the user ID
     */
    public void setUserID(int userID) { this.userID = userID; }

    /**
     * Gets the score of the quiz attempt
     * @return the score
     */
    public double getScore() { return score; }

    /**
     * Sets the score of the quiz attempt
     * @param score the score
     */
    public void setScore(double score) { this.score = score; }

    /**
     * Gets the time taken for the quiz attempt (in seconds)
     * @return the attempt time
     */
    public int getAttemptTime() { return attemptTime; }

    /**
     * Sets the time taken for the quiz attempt (in seconds)
     * @param attemptTime the attempt time
     */
    public void setAttemptTime(int attemptTime) { this.attemptTime = attemptTime; }


    /**
     * Formats the quiz attempt object as a readable string
     * @return the quiz attempt in a readable form
     */
    @Override
    public String toString() {
        return String.format("QuizAttempt{id=%d, quizID=%d, userID=%d, score=%d, attemptTime=%d}",
                id, quizID, userID, score, attemptTime);
    }
}
