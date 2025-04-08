package com.example.addressbook.model;

/**
 * Class to create a single connection to SQLite Database - Implements singleton pattern
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {

    private static Connection instance = null;

    /**
     * Constructor to create connection using JDBC SQLite driver
     */
    public SqliteConnection() {
        String url = "jdbc:sqlite:contacts.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " - " + ex.toString());
        }
    }

    /**
     * Method to get instance of a SQL Connection
     * @return new SQLiteConnection
     */
    public static  Connection getInstance() {
        if (instance ==null) {
            new SqliteConnection();
        }
        return instance;
    }

}
