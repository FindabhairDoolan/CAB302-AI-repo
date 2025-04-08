package com.example.addressbook.model;

import org.sqlite.jdbc4.JDBC4PreparedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteContactDAO implements  IContactDAO{



    private static final String CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS contacts ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "firstName VARCHAR NOT NULL,"
            + "lastName VARCHAR NOT NULL,"
            + "phone VARCHAR NOT NULL,"
            + "email VARCHAR NOT NULL"
            + ")";


    private static final String CLEAR_CONTACTS = "DELETE FROM contacts";

    private static final String INSERT_CONTACTS = "INSERT INTO contacts (firstName, lastName, phone, email) VALUES "
            + "('John', 'Doe', '0423423423', 'johndoe@example.com'),"
            + "('Jane', 'Doe', '0423423424', 'janedoe@example.com'),"
            + "('Jay', 'Doe', '0423423425', 'jaydoe@example.com')";

    private static final String GET_ALL_CONTACTS = "SELECT * FROM CONTACTS";
    private static final String ADD_CONTACT = "INSERT INTO contacts (firstName, lastName, phone, email) VALUES (?, ?, ?, ?)";
    private static final String GET_CONTact_BY_ID = "SELECT * FROM CONTACTS WHERE id = ?";

    private static final String UPDATE_CONTACT ="UPDATE contacts SET firstName = ?, lastName = ?, phone = ?, email = ? WHERE id = ?";
    private static final String DELETE_CONTACT = "DELETE FROM contacts WHERE id = ?";
    private Connection connection;

    public SqliteContactDAO() {
        connection = SqliteConnection.getInstance();


    }

    /**
     * Private helper method to create contacts table
     */
 /*   private void createContactsTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(CONTACTS_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
*/
    /**
     * Private helper method to insert data into contacts table
     */
 /*   private void insertDataContactsTable() {
        try {
            Statement clearContacts = connection.createStatement();
            clearContacts.execute(CLEAR_CONTACTS);
            Statement insertStatement = connection.createStatement();
            insertStatement.execute(INSERT_CONTACTS);
        } catch (SQLException ex) {
            ex.printStackTrace();
       }
    }

*/


    /**
     * @param contact The contact to update.
     */
    @Override
    public void updateContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_CONTACT);
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPhone());
            statement.setString(4, contact.getEmail());
            statement.setInt(5, contact.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * @param contact The contact to add.
     */
    @Override
    public void addContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement(ADD_CONTACT);
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPhone());
            statement.setString(4, contact.getEmail());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                contact.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param contact The contact to delete.
     */
    @Override
    public void deleteContact(Contact contact) {
            try {
                PreparedStatement statement = connection.prepareStatement(DELETE_CONTACT);
                statement.setInt(1, contact.getId());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    /**
     * Method to return a contact based on ID number
     * @param id The id of the contact to retrieve.
     * @return Contact object
     */
    @Override
    public Contact getContact(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_CONTact_BY_ID);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                String firstName = results.getString("firstName");
                String lastName = results.getString("lastName");
                String email = results.getString("email");
                String phone = results.getString("phone");
                Contact newContact = new Contact(firstName,lastName,phone, email);
                newContact.setId(id);
                return newContact;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Method to get all the contacts from the database - entries used to create Contact objects
     * @return ArrayList of Contact objects
     */
    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(GET_ALL_CONTACTS);
            while (results.next()) {
                int id = results.getInt("id");
                String firstName = results.getString("firstName");
                String lastName = results.getString("lastName");
                String email = results.getString("email");
                String phone = results.getString("phone");
                Contact newContact = new Contact(firstName,lastName,phone, email);
                newContact.setId(id);
                contacts.add(newContact);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return contacts;

    }
}
