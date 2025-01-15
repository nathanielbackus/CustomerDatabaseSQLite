package controller;
import dao.CountryDAO;
import dao.CustomerDAO;
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
import javafx.scene.control.Label;
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

public class CustomerController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TextField CustomerIDTextField, AddressTextField, NameTextField, PhoneTextField, PostalCodeTextField;
    @FXML
    private ComboBox<Country> CountryComboBox;
    @FXML
    private ComboBox<Division> DivisionComboBox;
    @FXML
    private Label TitleLabel;
    Customer CurrentCustomer;
    /**event to return to customer appointment**/
    @FXML
    void OnActionCustomerAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void OnActionLoadCountry(ActionEvent event){
        return;
    }
    /**populate data fields with existing customer data**/
    public void setCustomer(Customer customer) throws SQLException {
        CurrentCustomer = customer;
        CustomerIDTextField.setText(String.valueOf(customer.getCustomerID()));
        TitleLabel.setText("Update Customer");
        NameTextField.setText(customer.getCustomerName());
        AddressTextField.setText(customer.getAddress());
        PostalCodeTextField.setText(customer.getPostalCode());
        PhoneTextField.setText(customer.getPhone());
        CountryComboBox.getItems();
        String sql = "SELECT country_ID FROM divisions WHERE division_id = ?";
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
    /**collects data and saves it to customerDAO's addcustomer for sql insert**/
    @FXML
    void OnActionSaveCustomer(ActionEvent event) throws IOException {
        try {
            int CustomerID;
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
            String CreatedBy = LoginController.UserLoggedIn();
            String UpdatedBy = LoginController.UserLoggedIn();
            if (CustomerIDTextField.getText().isEmpty()) {
                CustomerID = CustomerDAO.CustomerGenerateID();
                CustomerIDTextField.setText(String.valueOf(CustomerID));
                CustomerDAO.addCustomer(CustomerID, CustomerName, Address, PostalCode, Phone, CreatedBy, DivisionID);
            } else {
                CustomerID = Integer.parseInt(CustomerIDTextField.getText());
                CustomerDAO.updateCustomer(CustomerID,CustomerName, Address, PostalCode, Phone, UpdatedBy, DivisionID);
            }
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
            stage.setScene(new Scene(scene));
        } catch (NumberFormatException e){
            JDBC.ErrorMessage("Input Error", "Error in adding customer due to incorrect input", "Please enter valid values for each text field.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**populates the comboboxes first with the name of the country then the divisions belonging to the country**/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Country> Countries = CountryDAO.getAllCountries();
            CountryComboBox.setItems(Countries);
            CountryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    ObservableList<Division> divisions = FXCollections.observableArrayList();
                    int countryId = newValue.getCountryID();
                    divisions = DivisionDAO.getSelectedDivisions(countryId);
                    DivisionComboBox.setItems(divisions);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}