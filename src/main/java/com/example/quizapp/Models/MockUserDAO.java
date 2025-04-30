/**package com.example.quizapp.Models;

import java.util.ArrayList;

public class MockUserDAO implements IUserDAO{

     // A static list of users to be stored as a mock database

    public static final ArrayList<User> users = new ArrayList<>();
    private static int autoIncrementedId = 0;

    public MockUserDAO() {
        // Add some initial users to the mock database
        addUser(new User("admin", "admin@example.com", "admin123"));
        addUser(new User("user1", "user1@example.com", "password1"));
        addUser(new User("user2", "user2@example.com", "password2"));
    }
    @Override
    public void addUser(User user) {
        user.setId(autoIncrementedId);
        autoIncrementedId++;
        users.add(user);
    }
    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                break;
            }
        }
    }
    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
    @Override
    public boolean validateCredentials(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public void addUsername(String username) {

    }

    @Override
    public void addEmailaddress(String emailaddress) {

    }

    @Override
    public void addpassword(String password) {

    }

    @Override
    public void addUsername(string username) {
        user.add(user);
    }
}

*/

