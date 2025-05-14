package com.example.quizapp;

import com.example.quizapp.Models.QuizAttempt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizAttemptTest {

    private static final int quizID = 101;
    private static final int userID = 202;
    private static final int score = 85;

    private static final int quizID_2 = 103;
    private static final int userID_2 = 204;
    private static final int score_2 = 92;

    private QuizAttempt attempt;

    @BeforeEach
    public void setUp() {
        attempt = new QuizAttempt(quizID, userID, score);
    }

    @Test
    public void testSetId() {
        attempt.setId(1);
        assertEquals(1, attempt.getId());
    }

    @Test
    public void testGetQuizID() {
        assertEquals(quizID, attempt.getQuizID());
    }

    @Test
    public void testSetQuizID() {
        attempt.setQuizID(quizID_2);
        assertEquals(quizID_2, attempt.getQuizID());
    }

    @Test
    public void testGetUserID() {
        assertEquals(userID, attempt.getUserID());
    }

    @Test
    public void testSetUserID() {
        attempt.setUserID(userID_2);
        assertEquals(userID_2, attempt.getUserID());
    }

    @Test
    public void testGetScore() {
        assertEquals(score, attempt.getScore());
    }

    @Test
    public void testSetScore() {
        attempt.setScore(score_2);
        assertEquals(score_2, attempt.getScore());
    }

    @Test
    public void testSetAndGetAttemptTime() {
        LocalDateTime now = LocalDateTime.now();
        attempt.setAttemptTime(now);
        assertEquals(now, attempt.getAttemptTime());
    }
}
