package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Quiz Attempt DAO
 */
public interface IQuizAttemptDAO {
    public void addQuizAttempt(QuizAttempt quizAttempt);
    public void updateQuizAttempt(QuizAttempt quizAttempt);
    public void deleteQuizAttempt(QuizAttempt quizAttempt);
    public List<QuizAttempt> getAttemptsByUserAndQuiz(int UserID, int quizID);
    public List<QuizWithScore> getQuizzesAttemptedByUser(int UserID);
}
