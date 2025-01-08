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
    //RANDOM SHENANIGANS I CAN RETURN TO FOR USER STUFF
//    public static ObservableList<User> allUsers = FXCollections.observableArrayList();
//    public static void loadAllUsers() throws SQLException {
//        allUsers.clear();
//        JDBC.openConnection();
//        String sql = "SELECT * FROM USERS";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            int userID = rs.getInt("user_ID");
//            String username = rs.getString("username");
//            String password = rs.getString("password");
//            User user = new User(userID, username, password);
//            allUsers.add(user);
//        }
//    }
//    public static ObservableList<Customer> getAllCustomers() {
//        return allCustomers;
//    }
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
                User user = new User(
                        userID, username, password);
                userReturnList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userReturnList;
    }
    //add
    public static int addUser(int userID, String username, String password, String CreatedBy) throws SQLException {
        JDBC.openConnection();
        String sql = "INSERT INTO CUSTOMERS (userID, username, password, Create_Date, Created_By) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setString(2, username);
        ps.setString(3, password);
        ps.setString(4, CreatedBy);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    //update

    //delete

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
