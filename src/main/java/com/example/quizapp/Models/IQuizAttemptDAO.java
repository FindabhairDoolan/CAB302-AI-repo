package com.example.quizapp.Models;

public interface IQuizAttemptDAO {
    public void addQuizAttempt(QuizAttempt quizAttempt);
    public void updateQuizAttempt(QuizAttempt quizAttempt);
    public void deleteQuizAttempt(QuizAttempt quizAttempt);
}
