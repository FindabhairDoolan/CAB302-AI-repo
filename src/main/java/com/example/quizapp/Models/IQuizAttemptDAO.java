package com.example.quizapp.Models;

import java.util.List;

public interface IQuizAttemptDAO {
    public void addQuizAttempt(QuizAttempt quizAttempt);
    public void updateQuizAttempt(QuizAttempt quizAttempt);
    public void deleteQuizAttempt(QuizAttempt quizAttempt);
    public List<QuizAttempt> getAttemptsByUserAndQuiz(int UserID, int quizID);
    public List<Quiz> getQuizzesAttemptedByUser(int UserID);
}
