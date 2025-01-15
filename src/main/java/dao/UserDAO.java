package dao;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    //puts all users in a array
    public static List<User> getAllUsers() throws SQLException{
        List<User> userReturnList = new ArrayList<>();
        String sql = "SELECT * FROM USERS;";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User user = new User(userID, username, password);
                userReturnList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userReturnList;
    }
    //add
    public static int addUser(int userID, String username, String password, String CreatedBy) throws SQLException {
//        JDBC.openConnection();
        String sql = "INSERT INTO users (user_ID, username, password, createdOn, CreatedBy) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.setString(4, CreatedBy);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    //update
    public static int updateUser(int user, String username, String password, String updatedBy) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, updatedBy = ?, updatedOn = CURRENT_TIMESTAMP WHERE user_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, updatedBy);
        ps.setInt(4, user);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    //delete
    public static boolean deleteUser(User selectedUser) throws SQLException {
//        if (selectedUser != null && CustomerDAOImpl.allCustomers.contains(selectedCustomer)) {
//            int customerID = selectedCustomer.getCustomerID();
//            String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ps.setInt(1, customerID);
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected > 0) {
//                allCustomers.remove(selectedCustomer);
//                return true;
//            }
//        }
        return false;
    }
    //generate id
    public static int userGenerateID() throws SQLException {
        int maxNumber = 0;
        for (User user : getAllUsers()){
            if (user.getUserID() > maxNumber){
                maxNumber = user.getUserID();
            }
        }
        int id = maxNumber+1;
        return id;
    }
}
