package com.example.quizapp.Models;

public class Question {
    private int questionID, quizID;
    private String questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3;

    public Question(int quizID, String questionText, String correctAnswer, String incorrectAnswer1, String incorrectAnswer2, String incorrectAnswer3) {
        this.quizID = quizID;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer1 = incorrectAnswer1;
        this.incorrectAnswer2 = incorrectAnswer2;
        this.incorrectAnswer3 = incorrectAnswer3;
    }

    public int getQuestionID() { return questionID; }
    public void setQuestionID(int questionID) { this.questionID = questionID; }

    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getIncorrectAnswer1() { return incorrectAnswer1; }
    public void setIncorrectAnswer1(String incorrectAnswer1) { this.incorrectAnswer1 = incorrectAnswer1; }

    public String getIncorrectAnswer2() { return incorrectAnswer2; }
    public void setIncorrectAnswer2(String incorrectAnswer2) { this.incorrectAnswer2 = incorrectAnswer2; }

    public String getIncorrectAnswer3() { return incorrectAnswer3; }
    public void setIncorrectAnswer3(String incorrectAnswer3) { this.incorrectAnswer3 = incorrectAnswer3; }

    @Override
    public String toString() {
        return String.format("Question{id=%d, quizID=%d, text='%s', correct='%s', wrong1='%s', wrong2='%s', wrong3='%s'}",
                questionID, quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
    }
}
