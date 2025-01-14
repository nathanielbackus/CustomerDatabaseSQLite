package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CustomerDAOImpl implements CustomerDAO {
    /**creates a list of all customers and populates it with data from the database in a select statement**/
    private List<Customer> customer;
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
    @Override
    public Customer getCustomer(int customerID) {
        return customer.get(customerID);
    }
    public CustomerDAOImpl() throws SQLException {
        customer = new ArrayList<>();
        loadAllCustomers();
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
}
