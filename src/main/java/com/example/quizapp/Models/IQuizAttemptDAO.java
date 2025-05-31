package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Quiz Attempt DAO
 */
public interface IQuizAttemptDAO {

    /**
     * Adds a new quizAttempt to the database.
     * @param quizAttempt The quizAttempt to add to the database.
     */
    public void addQuizAttempt(QuizAttempt quizAttempt);

    /**
     * Retrieves all quiz attempts that belong to the same user.
     * @param UserID The user whose quizAttempts to retrieve.
     */
    public List<QuizWithScore> getQuizzesAttemptedByUser(int UserID);
}
