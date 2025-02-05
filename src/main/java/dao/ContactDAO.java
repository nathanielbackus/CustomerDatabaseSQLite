package dao;

import helper.JDBC;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    /**retrieves contact data from database**/
    public static List<Contact> getAllContacts() throws SQLException{
        List<Contact> contactReturnList = new ArrayList<>();
        String sql = "SELECT * FROM CONTACTS;";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                int contactID = rs.getInt("contact_id");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                Contact contact = new Contact(contactID, contactName, contactEmail);
                contactReturnList.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactReturnList;
    }
    //add
    public static int addContact(int contactID, String contactName, String contactEmail) throws SQLException {
        String sql = "INSERT INTO contacts (contact_ID, contact_name, contact_email) VALUES (?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ps.setString(2, contactName);
        ps.setString(3, contactEmail);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    //update
    public static int updateContact(int contactID, String contactName, String contactEmail) throws SQLException {
        String sql = "UPDATE contacts SET contact_name = ?, contact_email = ? WHERE contact_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ps.setString(2, contactEmail);
        ps.setInt(3, contactID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    //delete
    public static boolean deleteContact(Contact selectedContact) throws SQLException {
        int contactID = selectedContact.getContactID();
        String sql = "DELETE FROM contacts WHERE contact_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
    //generate id
    public static int contactGenerateID() throws SQLException {
        int maxNumber = 0;
        for (Contact contact : getAllContacts()){
            if (contact.getContactID() > maxNumber){
                maxNumber = contact.getContactID();
            }
        }
        int id = maxNumber+1;
        return id;
    }
}
