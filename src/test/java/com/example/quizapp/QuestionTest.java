package com.example.quizapp;

import com.example.quizapp.Models.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionTest {
    private static final int quizID = 1;
    private static final String questionText = "What is QUT?";
    private static final String correctAnswer = "University";
    private static final String incorrectAnswer1 = "Food";
    private static final String incorrectAnswer2 = "Sport";
    private static final String incorrectAnswer3 = "Country";

    private static final int quizID_2 = 2;
    private static final String questionText_2 = "What is QUT?";
    private static final String correctAnswer_2 = "University";
    private static final String incorrectAnswer1_2 = "Food";
    private static final String incorrectAnswer2_2 = "Sport";
    private static final String incorrectAnswer3_2 = "Country";

    private Question question;

    @BeforeEach
    public void setUp() {
        question = new Question(quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
       // question2 = new Question(quizID_2, questionText_2, correctAnswer_2, incorrectAnswer1_2, incorrectAnswer2_2, incorrectAnswer3_2);
    }

    @Test
    public void testSetId() {
        question.setQuestionID(3);
        assertEquals(3, question.getQuestionID());
    }

    @Test
    public void testGetQuizID() {
        assertEquals(quizID, question.getQuizID());
    }

    @Test
    public void testSetQuizID() {
        question.setQuizID(quizID_2);
        assertEquals(quizID_2, question.getQuizID());
    }

    @Test
    public void testGetQuestionText() {
        assertEquals(questionText, question.getQuestionText());
    }

    @Test
    public void testSetQuestionText() {
        question.setQuestionText(questionText_2);
        assertEquals(questionText_2, question.getQuestionText());
    }

    @Test
    public void testGetCorrectAnswer() {
        assertEquals(correctAnswer, question.getCorrectAnswer());
    }

    @Test
    public void testSetCorrectAnswer() {
        question.setCorrectAnswer(correctAnswer_2);
        assertEquals(correctAnswer_2, question.getCorrectAnswer());
    }

    @Test
    public void testGetIncorrectAnswer1() {
        assertEquals(incorrectAnswer1, question.getIncorrectAnswer1());
    }

    @Test
    public void testSetIncorrectAnswer1() {
        question.setIncorrectAnswer1(incorrectAnswer1_2);
        assertEquals(incorrectAnswer1_2, question.getIncorrectAnswer1());
    }

    @Test
    public void testGetIncorrectAnswer2() {
        assertEquals(incorrectAnswer2, question.getIncorrectAnswer2());
    }

    @Test
    public void testSetIncorrectAnswer2() {
        question.setIncorrectAnswer2(incorrectAnswer2_2);
        assertEquals(incorrectAnswer2_2, question.getIncorrectAnswer2());
    }

    @Test
    public void testGetIncorrectAnswer3() {
        assertEquals(incorrectAnswer3, question.getIncorrectAnswer3());
    }

    @Test
    public void testSetIncorrectAnswer3() {
        question.setIncorrectAnswer3(incorrectAnswer3_2);
        assertEquals(incorrectAnswer3_2, question.getIncorrectAnswer3());
    }
}

