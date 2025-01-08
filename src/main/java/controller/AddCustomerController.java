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
import model.Division;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TextField CustomerIDTextField, AddAddressTextField, AddNameTextField, AddPhoneTextField, AddPostalCodeTextField;
    @FXML
    private ComboBox<Country> AddCountryComboBox;
    @FXML
    private ComboBox<Division> AddDivisionComboBox;
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
    /**collects data and saves it to customerDAO's addcustomer for sql insert**/
    @FXML
    void OnActionSaveNewCustomer(ActionEvent event) throws IOException {
        try {
            int CustomerID = CustomerDAOImpl.CustomerGenerateID();
            CustomerIDTextField.setText(String.valueOf(CustomerID));
            String CustomerName = AddNameTextField.getText();
            String Address = AddAddressTextField.getText();
            String PostalCode = AddPostalCodeTextField.getText();
            String Phone = AddPhoneTextField.getText();
            Division selectedDivision = AddDivisionComboBox.getSelectionModel().getSelectedItem();
            if (selectedDivision == null) {
                JDBC.ErrorMessage("Input Error", "Division not selected", "Please select a division.");
                return;
            }
            int DivisionID = selectedDivision.getDivisionID();
            String CreatedBy = LoginController.UserLoggedIn();
            CustomerDAO.addCustomer(CustomerID, CustomerName, Address, PostalCode, Phone, DivisionID, CreatedBy);
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
            AddCountryComboBox.setItems(Countries);
            AddCountryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
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
                    AddDivisionComboBox.setItems(divisions);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}