package com.example.quizapp;

import com.example.quizapp.Models.QuizAttempt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizAttemptTest {

    private static final int quizID = 101;
    private static final int userID = 202;
    private static final double score = 85;
    private static final int time = 180;

    private static final int quizID_2 = 103;
    private static final int userID_2 = 204;
    private static final double score_2 = 92;
    private static final int time_2 = 120;
    private static final List<String> answers = Arrays.asList("1", "2", "3", "4", "5");

    private QuizAttempt attempt;

    @BeforeEach
    public void setUp() {
        attempt = new QuizAttempt(quizID, userID, score, time, answers);
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
    public void testGetAttemptTime() {
        assertEquals(time, attempt.getAttemptTime());
    }

    @Test
    public void testSetAttemptTime() {
        attempt.setAttemptTime(time_2);
        assertEquals(time_2, attempt.getAttemptTime());
    }
}
