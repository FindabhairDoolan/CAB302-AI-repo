package com.example.quizapp.Models;

/**
 * The quiz class, outlines the attributes of a quiz
 */
public class Quiz {

    private int id, creatorID, timer;
    private String quizName, subject, quizTopic, difficulty, yearLevel, country, visibility;

    /**
     * The constructor for the quiz class
     * @param quizName the name of the quiz
     * @param subject the subject of the quiz
     * @param quizTopic the topic of the quiz
     * @param quizMode the mode of the quiz: Practice/Exam
     * @param difficulty the difficulty of the quiz
     * @param yearLevel the year level the quiz is designed for
     * @param country the country the quiz is designed for
     * @param visibility the visibility of the quiz: Public/Private
     * @param creatorID the creator ID of the quiz owner
     */
    public Quiz(String quizName, String subject, String quizTopic, int quizMode, String difficulty, String yearLevel, String country, String visibility, int creatorID) {
        this.quizName = quizName;
        this.subject = subject;
        this.quizTopic = quizTopic;
        this.timer = quizMode;
        this.difficulty = difficulty;
        this.yearLevel = yearLevel;
        this.country = country;
        this.visibility = visibility;
        this.creatorID = creatorID;
    }

    /**
     * Gets the quiz ID of the quiz
     * @return the quiz ID of the quiz
     */
    public int getQuizID() { return id; }

    /**
     * Sets the quiz ID of the quiz
     * @param quizID the quiz ID of the quiz
     */
    public void setQuizID(int quizID) { this.id = quizID; }

    /**
     * Gets the name of the quiz
     * @return the name of the quiz
     */
    public String getName() { return quizName; }

    /**
     * Sets the name of the quiz
     * @param quizName the name of the quiz
     */
    public void setName(String quizName) { this.quizName = quizName; }

    /**
     * Gets the subject of the quiz
     * @return the subject of the quiz
     */
    public String getSubject() { return subject; }

    /**
     * Sets the subject of the quiz
     * @param subject the subject of the quiz
     */
    public void setSubject(String subject) { this.subject = subject; }

    /**
     * Gets the topic of the quiz
     * @return the topic of the quiz
     */
    public String getTopic() { return quizTopic; }

    /**
     * Sets the topic of the quiz
     * @param quizTopic the topic of the quiz
     */
    public void setTopic(String quizTopic) { this.quizTopic = quizTopic; }

    /**
     * Gets the timer of the quiz
     * @return the timer of the quiz in seconds
     */
    public int getTimer() { return timer; }

    /**
     * Sets the timer of the quiz
     * @param timer the timer of the quiz in seconds
     */
    public void setTimer(int timer) { this.timer = timer; }

    /**
     * Gets the difficulty of the quiz
     * @return the difficulty of the quiz
     */
    public String getDifficulty() { return difficulty; }

    /**
     * Sets the difficulty of the quiz
     * @param difficulty the difficulty of the quiz
     */
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    /**
     * Gets the year level the quiz is designed for
     * @return the year level the quiz is designed for
     */
    public String getYearLevel() { return yearLevel; }

    /**
     * Sets the year level the quiz is designed for
     * @param yearLevel the year level the quiz is designed for
     */
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    /**
     * Gets the country the quiz is designed for
     * @return the country the quiz is designed for
     */
    public String getCountry() { return country; }

    /**
     * Sets the country the quiz is designed for
     * @param country the country the quiz is designed for
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Gets the visibility of the quiz
     * @return the visibility of the quiz
     */
    public String getVisibility() { return visibility; }

    /**
     * Sets the visibility of the quiz
     * @param visibility the visibility of the quiz
     */
    public void setVisibility(String visibility) { this.visibility = visibility; }

    /**
     * Gets the creator ID of the quiz (the owner of the quiz)
     * @return the creator ID of the quiz
     */
    public int getCreatorID() { return creatorID; }

    /**
     * Sets the creator ID of the quiz (the owner of the quiz)
     * @param creatorID the creator ID of the quiz
     */
    public void setCreatorID(int creatorID) { this.creatorID = creatorID; }

    /**
     * Formats the quiz object as a readable string
     * @return the quiz in a readable form
     */
    @Override
    public String toString() {
        return String.format("Quiz{id=%d, name='%s', subject='%s', topic='%s',  timer='%d', difficulty='%s', year='%s', country='%s','visibility=%s' creatorID=%d}",
                id, quizName, subject, quizTopic, timer, difficulty, yearLevel, country, visibility, creatorID);
    }


}
