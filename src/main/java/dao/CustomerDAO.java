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
    public static List<Customer> getAllCustomers() throws SQLException{
        List<Customer> customerReturnList = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS;";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int customerID = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String address = rs.getString("address");
                String postalCode = rs.getString("postal_code");
                String phone = rs.getString("phone");
                int divisionID = rs.getInt("division_id");
                Customer customer = new Customer(customerID, customerName, address, postalCode, phone, divisionID);
                customerReturnList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerReturnList;
    }
    /**create a unique customer id**/
    public static int CustomerGenerateID() throws SQLException {
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
