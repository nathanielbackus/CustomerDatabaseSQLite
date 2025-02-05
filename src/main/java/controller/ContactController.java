package controller;
import dao.*;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private Label contactLabel;
    @FXML
    private TextField ContactIDTextField, ContactNameTextField, ContactEmailTextField;
    Contact CurrentContact;
    /**event to return to customer appointment**/
    @FXML
    void OnActionUsersContacts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("UsersContacts.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**populate data fields with existing customer data**/
    public void setContact(Contact contact) throws SQLException {
        CurrentContact = contact;
        ContactIDTextField.setText(String.valueOf(contact.getContactID()));
        ContactNameTextField.setText(contact.getContactName());
        ContactEmailTextField.setText(contact.getEmail());
    }
    /**collects data and saves it to customerDAO's addcustomer for sql insert**/
    @FXML
    void OnActionSaveContact(ActionEvent event) throws IOException {
        try {
            int contactID;
            String contactName = ContactNameTextField.getText();
            String contactEmail = ContactEmailTextField.getText();
            if (ContactIDTextField.getText().isEmpty()) {
                contactID = ContactDAO.contactGenerateID();
                ContactIDTextField.setText(String.valueOf(contactID));
                ContactDAO.addContact(contactID, contactName, contactEmail);
            } else {
                contactLabel.setText("Update Contact");
                contactID = Integer.parseInt(ContactIDTextField.getText());
                ContactDAO.updateContact(contactID, contactName, contactEmail);
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