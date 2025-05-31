package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Quiz DAO
 */
public interface IQuizDAO {

    /**
     * Adds a new quiz to the database.
     * @param quiz The quiz to add to the database.
     */
    public void addQuiz(Quiz quiz);

    /**
     * Updates an existing quiz in the database.
     * @param quiz The quiz to update.
     */
    public void updateQuiz(Quiz quiz);

    /**
     * Deletes a quiz from the database.
     * @param quiz The quiz to delete.
     */
    public void deleteQuiz(Quiz quiz);

    /**
     * Searches for quizzes in the sqlite database quizzes table by topic
     * @param phrase the topic of the quiz
     * @return A list of quizzes with the same subject
     */
    public List<Quiz> searchQuiz(String phrase);

    /**
     * Searches for the quiz in the sqlite database quizzes table by name
     * @param name the name of the quiz
     * @return the quiz with the same name, or null if not existent
     */
    public Quiz getQuizByName(String name);

    /**
     * Searches for the quizzes made by the same user in the sqlite database
     * @param creatorID the ID of the user who created the quiz
     * @return A list of the quizzes the user created
     */
    public List<Quiz> getQuizzesByCreator(int creatorID);

    /**
     * Retrieves all the quizzes from the sqlite database quizzes table
     * @return A list of all quizzes in the database
     */
    public List<Quiz> getAllQuizzes();
}
