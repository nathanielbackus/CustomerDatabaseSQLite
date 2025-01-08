package controller;

import dao.CountryDAO;
import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import dao.DivisionDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TextField CustomerIDTextField, AddressTextField, NameTextField, PhoneTextField, PostalCodeTextField;
    @FXML
    private ComboBox<Country> CountryComboBox;
    @FXML
    private ComboBox<Division> DivisionComboBox;
    Customer CurrentCustomer;
    /**load customerappointments scene**/
    @FXML
    void OnActionCustomerAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**populate data fields with existing customer data**/
    public void setCustomer(Customer customer) throws SQLException {
        JDBC.openConnection();
        CurrentCustomer = customer;
        CustomerIDTextField.setText(String.valueOf(customer.getCustomerID()));
        NameTextField.setText(customer.getCustomerName());
        AddressTextField.setText(customer.getAddress());
        PostalCodeTextField.setText(customer.getPostalCode());
        PhoneTextField.setText(customer.getPhone());
        CountryComboBox.getItems();
        String sql = "SELECT country_ID FROM first_level_divisions WHERE division_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.getDivisionID());
        ResultSet rs = ps.executeQuery();
        int CountryID = 0;
        if (rs.next()) {
            CountryID = rs.getInt("country_ID");
        }
        for (Country country : CountryComboBox.getItems()) {
            if (country.getCountryID() == CountryID) {
                CountryComboBox.getSelectionModel().select(country);
                break;
            }
        }
        for (Division division : DivisionComboBox.getItems()) {
            if (division.getDivisionID() == customer.getDivisionID()) {
                DivisionComboBox.getSelectionModel().select(division);
                break;
            }
        }
    }
    /**save new data to database**/
    @FXML
    void OnActionSaveUpdatedCustomer(ActionEvent event) throws IOException, SQLException {
        try {
            int CustomerID = Integer.parseInt(CustomerIDTextField.getText());
            String CustomerName = NameTextField.getText();
            String Address = AddressTextField.getText();
            String PostalCode = PostalCodeTextField.getText();
            String Phone = PhoneTextField.getText();
            Division selectedDivision = DivisionComboBox.getSelectionModel().getSelectedItem();
            if (selectedDivision == null) {
                JDBC.ErrorMessage("Input Error", "Division not selected", "Please select a division.");
                return;
            }
            int DivisionID = selectedDivision.getDivisionID();
            String UpdatedBy = LoginController.UserLoggedIn();
            CustomerDAO.updateCustomer(CustomerID, CustomerName, Address, PostalCode, Phone, DivisionID, UpdatedBy);
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
            stage.setScene(new Scene(scene));
        } catch (NumberFormatException e){
            JDBC.ErrorMessage("Input Error", "Error in updating customer due to incorrect input", "Please enter valid values for each text field.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**populate comboboxes with data of a country or division**/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Country> Countries = CountryDAO.getAllCountries();
            CountryComboBox.setItems(Countries);
            CountryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    ObservableList<Division> divisions = FXCollections.observableArrayList();
                    int countryId = newValue.getCountryID();
                    switch (countryId) {
                        case 1:
                            divisions = DivisionDAO.getAllUSDivisions();
                            break;
                        case 2:
                            divisions = DivisionDAO.getAllUKDivisions();
                            break;
                        case 3:
                            divisions = DivisionDAO.getAllCADivisions();
                            break;
                    }
                    DivisionComboBox.setItems(divisions);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
