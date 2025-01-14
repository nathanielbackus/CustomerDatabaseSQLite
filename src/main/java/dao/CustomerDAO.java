package dao;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    static List<Customer> getAllCustomers() {
        return null;
    }
    public Customer getCustomer(int CustomerID);
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
        if (selectedCustomer != null && CustomerDAOImpl.allCustomers.contains(selectedCustomer)) {
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
