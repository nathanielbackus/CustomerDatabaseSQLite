package controller;

import dao.ContactDAO;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Contact;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

//ADD SCENES FOR CONTACT AND USER!!
//ADD DAO FOR CONTAVT AND USER!!
public class UsersContactsController implements Initializable {
    Stage stage;
    Parent scene;
    private ObservableList<User> observableUserList;
    private ObservableList<Contact> observableContactList;
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
    void OnActionEditContact(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Contact.fxml"));
        loader.load();
        ContactController CController = loader.getController();
        CController.setContact(AllContactsTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }
    @FXML
    public void OnActionDeleteContact(ActionEvent event) throws IOException, SQLException {
        Contact selectedContact = AllContactsTableView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            int contactID = selectedContact.getContactID();
            String sql = "SELECT * FROM appointments WHERE contact_ID = ?;";
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
                    observableContactList.remove(selectedContact);
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            } else {
                JDBC.ErrorMessage("Error Deleting Contact", "Appointment associated with contact", "Please delete all appointments associated with this contact before trying to delete them again.");
            }
        }
    }
    @FXML
    void OnActionAddUser(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("User.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void OnActionEditUser(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("User.fxml"));
        loader.load();
        UserController UController = loader.getController();
        UController.setUser(AllUsersTableView.getSelectionModel().getSelectedItem());
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }
    @FXML
    public void OnActionDeleteUser(ActionEvent event) throws IOException, SQLException {
        User selectedUser = AllUsersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            int userID = selectedUser.getUserID();
            if (userID != LoginController.UserIDLoggedIn()){
                String sql = "SELECT * FROM appointments WHERE user_ID = ?;";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, userID);
                ResultSet rowsAffected = ps.executeQuery();
                if (!rowsAffected.next()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete User?");
                    alert.setHeaderText("Are you sure you want to delete the selected user?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        UserDAO.deleteUser(selectedUser);
                        observableUserList.remove(selectedUser);
                    } else if (result.get() == ButtonType.CANCEL) {
                        return;
                    }
                } else if(userID == LoginController.UserIDLoggedIn()) {
                    JDBC.ErrorMessage("Error Deleting User", "User is logged in.", "Please log into a different user account to delete this user.");
                } else {
                    JDBC.ErrorMessage("Error Deleting User", "Appointment associated with user", "Please delete all appointments associated with this user before trying to delete them again.");
                }
            } else {
                JDBC.ErrorMessage("Error Deleting User", "Cannot Delete Self", "Please log into another account to delete this user.");
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            ContactDAO.getAllContacts();
            UserDAO.getAllUsers();
            observableUserList = FXCollections.observableArrayList(UserDAO.getAllUsers());
            observableContactList = FXCollections.observableArrayList(ContactDAO.getAllContacts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AllContactsTableView.setItems(observableContactList);
        ContactsTBID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        ContactsTBName.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        ContactsTBEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));

        AllUsersTableView.setItems(observableUserList);
        UserTBID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        UserTBUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        UserTBPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

}
