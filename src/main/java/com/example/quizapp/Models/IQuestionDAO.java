package com.example.quizapp.Models;

public interface IQuestionDAO {
    public void addQuestion(Question question);
    public void updateQuestion(Question question);
    public void deleteQuestion(Question question);
}

