package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Quiz DAO
 */
public interface IQuizDAO {

    public void addQuiz(Quiz quiz);
    public void updateQuiz(Quiz quiz);
    public void deleteQuiz(Quiz quiz);
    public List<Quiz> searchQuiz(String phrase);
    public Quiz getQuizByName(String name);
    public List<Quiz> getQuizzesByCreator(int creatorID);
    public List<Quiz> getAllQuizzes();
}
