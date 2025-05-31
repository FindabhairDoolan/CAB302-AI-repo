package com.example.quizapp.Models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a singleton SQLite database connection for the application.
 */
 public class SqliteConnection {
    private static Connection instance = null;

    /**
     * Private constructor that establishes a connection to the SQLite database.
     */
    private SqliteConnection() {
        String url = "jdbc:sqlite:quizApp.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * Returns the singleton instance of the SQLite database connection.
     * @return the singleton {@code Connection} to the SQLite database
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}
