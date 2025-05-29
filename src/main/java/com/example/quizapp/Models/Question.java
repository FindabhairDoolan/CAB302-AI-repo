package com.example.quizapp.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The question class, outlines the attributes of a question in a quiz
 */
public class Question {
    private int questionID, quizID;
    private String questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3;
    private List<String> allAnswers;

    /**
     * The constructor for the question class
     * @param quizID The ID of the quiz the question is from
     * @param questionText the actual text of the question
     * @param correctAnswer the correct answer for the question
     * @param incorrectAnswer1 the first incorrect answer for the question
     * @param incorrectAnswer2 the second incorrect answer for the question
     * @param incorrectAnswer3 the third incorrect answer for the question
     */
    public Question(int quizID, String questionText, String correctAnswer, String incorrectAnswer1, String incorrectAnswer2, String incorrectAnswer3) {
        this.quizID = quizID;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer1 = incorrectAnswer1;
        this.incorrectAnswer2 = incorrectAnswer2;
        this.incorrectAnswer3 = incorrectAnswer3;

        this.allAnswers = new ArrayList<>();
        allAnswers.add(correctAnswer);
        allAnswers.add(incorrectAnswer1);
        allAnswers.add(incorrectAnswer2);
        allAnswers.add(incorrectAnswer3);
    }

    /**
     * Gets the question ID of the question
     * @return the question's ID
     */
    public int getQuestionID() { return questionID; }

    /**
     * Sets the question ID of the question
     * @param questionID the question's ID
     */
    public void setQuestionID(int questionID) { this.questionID = questionID; }

    /**
     * Gets the quiz ID of the question (the quiz the question is from)
     * @return the question's quiz ID
     */
    public int getQuizID() { return quizID; }

    /**
     * Sets the quiz ID of the question (the quiz the question is from)
     * @param quizID the question's quizID
     */
    public void setQuizID(int quizID) { this.quizID = quizID; }

    /**
     * Gets the question's text
     * @return the question's text
     */
    public String getQuestionText() { return questionText; }

    /**
     * Sets the question's text
     * @param questionText the question's text
     */
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    /**
     * Get the correct answer for the question
     * @return the correct answer
     */
    public String getCorrectAnswer() { return correctAnswer; }

    /**
     * Sets the correct answer for the question
     * @param correctAnswer the correct answer
     */
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    /**
     * Gets the first incorrect answer of the question
     * @return the first incorrect answer
     */
    public String getIncorrectAnswer1() { return incorrectAnswer1; }

    /**
     * Sets the first incorrect answer of the question
     * @param incorrectAnswer1 the first incorrect answer
     */
    public void setIncorrectAnswer1(String incorrectAnswer1) { this.incorrectAnswer1 = incorrectAnswer1; }

    /**
     * Gets the second incorrect answer of the question
     * @return the second incorrect answer
     */
    public String getIncorrectAnswer2() { return incorrectAnswer2; }
    /**
     * Sets the second incorrect answer of the question
     * @param incorrectAnswer2 the second incorrect answer
     */
    public void setIncorrectAnswer2(String incorrectAnswer2) { this.incorrectAnswer2 = incorrectAnswer2; }

    /**
     * Gets the third incorrect answer of the question
     * @return the third incorrect answer
     */
    public String getIncorrectAnswer3() { return incorrectAnswer3; }
    /**
     * Sets the third incorrect answer of the question
     * @param incorrectAnswer3 the second incorrect answer
     */
    public void setIncorrectAnswer3(String incorrectAnswer3) { this.incorrectAnswer3 = incorrectAnswer3; }

    /**
     * Randomises the order of the answers so users cannot cheat
     * @return the list of shuffled answers
     */
    public List<String> getShuffledAnswers() {
        List<String> shuffled = new ArrayList<>(allAnswers);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    /**
     * Formats the question object as a readable string
     * @return the question in a readable form
     */
    @Override
    public String toString() {
        return String.format("Question{id=%d, quizID=%d, text='%s', correct='%s', wrong1='%s', wrong2='%s', wrong3='%s'}",
                questionID, quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
    }
}
