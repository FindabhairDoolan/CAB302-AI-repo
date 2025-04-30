package com.example.quizapp.Models;

public class User {
    private int id;
    private String userName, email, password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;

    }
    public int getUserID() {
        return id;
    }
    public String getUserName() {
        return userName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setUserID(int userID) {
        this.id = userID;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', password='%s'}",
               id, userName, email, password);
    }


}
