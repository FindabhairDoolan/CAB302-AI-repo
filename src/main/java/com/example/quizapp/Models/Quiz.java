package com.example.quizapp.Models;

public class Quiz {

    private int id, creatorID;
    private String quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, visibility;

    public Quiz(String quizName, String subject, String quizTopic, String quizMode, String difficulty, String yearLevel, String country, String visibility, int creatorID) {
        this.quizName = quizName;
        this.subject = subject;
        this.quizTopic = quizTopic;
        this.quizMode = quizMode;
        this.difficulty = difficulty;
        this.yearLevel = yearLevel;
        this.country = country;
        this.visibility = visibility;
        this.creatorID = creatorID;
    }

    public int getQuizID() { return id; }
    public void setQuizID(int quizID) { this.id = quizID; }

    public String getName() { return quizName; }
    public void setName(String quizName) { this.quizName = quizName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return quizTopic; }
    public void setTopic(String quizTopic) { this.quizTopic = quizTopic; }

    public String getMode() { return quizMode; }
    public void setMode(String quizMode) { this.quizMode = quizMode; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public int getCreatorID() { return creatorID; }
    public void setCreatorID(int creatorID) { this.creatorID = creatorID; }

    @Override
    public String toString() {
        return String.format("Quiz{id=%d, name='%s', subject='%s', topic='%s',  mode='%s', difficulty='%s', year='%s', country='%s','visibility=%s' creatorID=%d}",
                id, quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, visibility, creatorID);
    }


}
