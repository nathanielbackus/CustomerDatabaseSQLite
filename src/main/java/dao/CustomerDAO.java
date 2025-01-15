package dao;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerDAO {
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static void loadAllCustomers() throws SQLException {
        allCustomers.clear();
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int CustomerID = rs.getInt("Customer_ID");
            String CustomerName = rs.getString("Customer_Name");
            String Address = rs.getString("Address");
            String PostalCode = rs.getString("Postal_Code");
            String Phone = rs.getString("Phone");
            int DivisionID = rs.getInt("Division_ID");
            Customer customer = new Customer(CustomerID, CustomerName, Address, PostalCode, Phone, DivisionID);
            allCustomers.add(customer);
        }
    }
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    /**create a unique customer id**/
    public static int CustomerGenerateID(){
        int maxNumber = 0;
        for (Customer customer : getAllCustomers()){
            if (customer.getCustomerID() > maxNumber){
                maxNumber = customer.getCustomerID();
            }
        }
        int id = maxNumber+1;
        return id;
    }
    /**inserts customer into sql database**/
    public static int addCustomer(int CustomerID, String CustomerName, String Address, String PostalCode, String Phone, String CreatedBy, int DivisionID) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone, createdBy, CreatedOn, Division_ID) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, CustomerID);
        ps.setString(2, CustomerName);
        ps.setString(3, Address);
        ps.setString(4, PostalCode);
        ps.setString(5, Phone);
        ps.setString(6, CreatedBy);
        ps.setInt(7, DivisionID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**updates customer in database**/
    public static int updateCustomer(int CustomerID, String CustomerName, String Address, String PostalCode, String Phone, String UpdatedBy, int DivisionID) throws SQLException {
        String sql = "UPDATE customers SET customer_name = ?, address = ?, postal_code = ?, phone = ?, updatedBy = ?, updatedOn = CURRENT_TIMESTAMP, division_id = ? WHERE customer_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, CustomerName);
        ps.setString(2, Address);
        ps.setString(3, PostalCode);
        ps.setString(4, Phone);
        ps.setString(5, UpdatedBy);
        ps.setInt(6, DivisionID);
        ps.setInt(7, CustomerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**deletes customer in database**/
    public static boolean deleteCustomer(Customer selectedCustomer) throws SQLException {
        if (selectedCustomer != null && allCustomers.contains(selectedCustomer)) {
            int customerID = selectedCustomer.getCustomerID();
            String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                allCustomers.remove(selectedCustomer);
                return true;
            }
        }
        return false;
    }
}
