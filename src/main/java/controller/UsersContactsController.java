package controller;

import dao.CustomerDAO;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import model.User;

import javax.swing.text.TableView;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
//ADD SCENES FOR CONTACT AND USER!!
//ADD DAO FOR CONTAVT AND USER!!
public class UsersContactsController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TableView<Contact> AllContactsTableView;
    @FXML
    private TableView<User> AllUsersTableView;
    @FXML
    private TableColumn<?, ?> ContactsTBID, ContactsTBName, ContactsTBEmail;
    @FXML
    private TableColumn<?, ?> UserTBID, UserTBUsername, UserTBPassword;
    @FXML
    void OnActionCustomersAppointments(ActionEvent event) throws IOException{
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void OnActionAddContact(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Contact.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void OnActionEditContact(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Contact.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionDeleteContact(ActionEvent event) throws IOException, SQLException {
        Contact selectedContact = AllContactsTableView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            int contactID = selectedContact.getContactID();
            String sql = "SELECT * FROM contacts WHERE contact_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rowsAffected = ps.executeQuery();
            if (!rowsAffected.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Contact?");
                alert.setHeaderText("Are you sure you want to delete the selected contact?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ContactDAO.deleteContact(selectedContact);
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            } else {
                JDBC.ErrorMessage("Error Deleting Contact", "Appointment associated with contact", "Please delete all appointments associated with this contact before trying to delete them again.");
            }
        }
    }


}
