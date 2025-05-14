package com.example.quizapp.Models;

import java.util.List;

public class Quiz {

    private int id, creatorID;
    private String quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country;

    public Quiz(String quizName, String subject, String quizTopic, String quizMode, String difficulty, String yearLevel, String country, int creatorID) {
        this.quizName = quizName;
        this.subject = subject;
        this.quizTopic = quizTopic;
        this.quizMode = quizMode;
        this.difficulty = difficulty;
        this.yearLevel = yearLevel;
        this.country = country;
        this.creatorID = creatorID;
    }

    public int getQuizID() { return id; }
    public void setQuizID(int quizID) { this.id = quizID; }

    public String getQuizName() { return quizName; }
    public void setQuizName(String quizName) { this.quizName = quizName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getQuizTopic() { return quizTopic; }
    public void setQuizTopic(String quizTopic) { this.quizTopic = quizTopic; }

    public String getQuizMode() { return quizMode; }
    public void setQuizMode(String quizMode) { this.quizMode = quizMode; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getCreatorID() { return creatorID; }
    public void setCreatorID(int creatorID) { this.creatorID = creatorID; }

    @Override
    public String toString() {
        return String.format("Quiz{id=%d, name='%s', subject='%s', topic='%s',  mode='%s', difficulty='%s', year='%s', country='%s', creatorID=%d}",
                id, quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, creatorID);
    }


}
