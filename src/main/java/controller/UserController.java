package controller;
import dao.CountryDAO;
import dao.CustomerDAO;
import dao.DivisionDAO;
import dao.UserDAO;
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
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TextField UserIDTextField, UsernameTextField, UserPasswordTextField;
    User CurrentUser;
    /**event to return to customer appointment**/
    @FXML
    void OnActionUsersContacts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("UsersContacts.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**populate data fields with existing customer data**/
    public void setUser(User user) throws SQLException {
        CurrentUser = user;
        UserIDTextField.setText(String.valueOf(user.getUserID()));
        UsernameTextField.setText(user.getUsername());
        UserPasswordTextField.setText(user.getPassword());
    }
    /**collects data and saves it to customerDAO's addcustomer for sql insert**/
    @FXML
    void OnActionSaveUser(ActionEvent event) throws IOException {
        try {
            int UserID;
            String Username = UsernameTextField.getText();
            String Password = UserPasswordTextField.getText();
            String CreatedBy = LoginController.UserLoggedIn();
            String UpdatedBy = LoginController.UserLoggedIn();
            if (UserIDTextField.getText().isEmpty()) {
                UserID = UserDAO.userGenerateID();
                UserIDTextField.setText(String.valueOf(UserID));
                UserDAO.addUser(UserID, Username, Password, CreatedBy);
            } else {
                UserID = Integer.parseInt(UserIDTextField.getText());
                UserDAO.updateUser(UserID, Username, Password, UpdatedBy);
            }
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("UsersContacts.fxml"));
            stage.setScene(new Scene(scene));
        } catch (NumberFormatException e){
            JDBC.ErrorMessage("Input Error", "Error in adding user due to incorrect input", "Please enter valid values for each text field.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**populates the comboboxes first with the name of the country then the divisions belonging to the country**/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}