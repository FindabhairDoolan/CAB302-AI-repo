package com.example.quizapp.Models;

import java.util.List;

public interface IQuizDAO {

    public void addQuiz(Quiz quiz);
    public void updateQuizInfo(Quiz quiz);
    public void deleteQuiz(Quiz quiz);
    public List<Quiz> searchQuiz(String phrase);
    public Quiz getQuizByName(String name);
    public List<Quiz> getQuizzesByCreator(int creatorID);
    public List<Question> getQuestionsForQuiz(Quiz quiz);
    public int getNumberOfQuestions(Quiz quiz);
    public void addQuestionToQuiz(String username);
    public void removeQuestionFromQuiz(String emailaddress);
    public List<Quiz> getAllQuizzes();
}
