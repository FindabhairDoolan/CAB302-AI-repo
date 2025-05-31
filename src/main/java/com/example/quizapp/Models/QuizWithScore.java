package com.example.quizapp.Models;

import java.sql.Timestamp;

/**
 * The QuizWithScore class combines a quiz with a user's attempt and score
 */
public class QuizWithScore {
    private Quiz quiz;
    private QuizAttempt attempt;
    private Timestamp time;

    /**
     * The constructor for the QuizWithScore class
     * @param quiz the quiz that was attempted
     * @param quizAttempt the user's attempt on the quiz
     */
    public QuizWithScore(Quiz quiz, QuizAttempt quizAttempt) {
        this.quiz = quiz;
        this.attempt = quizAttempt;
    }

    /**
     * Gets the quiz associated with this object
     * @return the quiz
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Gets the score from the user's quiz attempt
     * @return the score
     */
    public double getScore() {
        return attempt.getScore();
    }

    /**
     * Gets the quiz attempt associated with this object
     * @return the quiz attempt
     */
    public QuizAttempt getAttempt() {
        return attempt;
    }


}
