package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Question DAO
 */

public interface IQuestionDAO {

    /**
     * Adds a new question to the database.
     * @param question The question to add to the database.
     */
    public void addQuestion(Question question);

    /**
     * Deletes a question from the database.
     * @param question The quiz to add to the database.
     */
    public void deleteQuestion(Question question);

    /**
     * Retrieve a list of questions realted to a quiz.
     * @param quizId The ID of the quiz the questions to retrieve are associated to.
     */
    public List<Question> getQuestionsForQuiz(int quizId);
}

