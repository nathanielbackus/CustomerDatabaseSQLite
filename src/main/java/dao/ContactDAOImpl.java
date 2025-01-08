package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl {
    private List<Contact> contact;
    /**retrieves contact data from database**/
    public static void loadAllContacts() throws SQLException {
        allContacts.clear();
        JDBC.openConnection();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int ContactID = rs.getInt("Contact_ID");
            String ContactName = rs.getString("Contact_Name");
            String Email = rs.getString("Email");
            Contact contact = new Contact(ContactID, ContactName, Email);
            allContacts.add(contact);
        }
    }
    /**creates a list of all contacts and a function to retrieve them**/
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }
    public ContactDAOImpl() throws SQLException {
        contact = new ArrayList<Contact>();
        loadAllContacts();
    }
}
