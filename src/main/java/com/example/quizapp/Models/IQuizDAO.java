package com.example.quizapp.Models;

import java.util.List;

public interface IQuizDAO {

    public void addQuiz(Quiz quiz);
    public void updateQuizInfo(Quiz quiz);
    public void deleteQuiz(Quiz quiz);
    public void addQuestionToQuiz(String username); //not yet implemented
    public void removeQuestionFromQuiz(String emailaddress); //not yet implemented
}
