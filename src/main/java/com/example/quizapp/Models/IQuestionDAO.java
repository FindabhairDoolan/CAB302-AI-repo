package com.example.quizapp.Models;

import java.util.List;

public interface IQuestionDAO {
    public void addQuestion(Question question);
    public void updateQuestion(Question question);
    public void deleteQuestion(Question question);
    public List<Question> getQuestionsForQuiz(int quizId);
}

