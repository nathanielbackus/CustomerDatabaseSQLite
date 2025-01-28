package controller;
import dao.*;
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
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    private ObservableList<Contact> observableContactList;
    private ObservableList<Customer> observableCustomerList;
    private ObservableList<User> observableUserList;
    String appointmentType;
    @FXML
    private Label AppointmentLabel, LocationLabel;
    @FXML
    private TextField AppointmentDescriptionTextField, AppointmentLocationTextField, AppointmentTitleTextField,
            AppointmentIDTextField;
    @FXML
    private DatePicker AppointmentEndDatePicker, AppointmentStartDatePicker;
    @FXML
    private ComboBox<LocalTime> AppointmentEndTimeComboBox, AppointmentStartTimeComboBox;
    @FXML
    private ComboBox<Contact> ContactComboBox;
    @FXML
    private ComboBox<User> UserComboBox;
    @FXML
    private ComboBox<Customer> CustomerComboBox;
    Appointment CurrentAppointment;
    public void getAppointmentType(String appointmentTypeData) {
        appointmentType = appointmentTypeData;
        if (appointmentType.equals("InPerson")) {
            LocationLabel.setText("Location: ");
        } else {
            LocationLabel.setText("Meeting Link: ");
        }
    }
    /**event to open the customerappointments scene**/
    @FXML
    void OnActionCustomerAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void setAppointment(Appointment appointment) throws SQLException {
        CurrentAppointment = appointment;
        AppointmentIDTextField.setText(String.valueOf(appointment.getAppointmentID()));
        AppointmentTitleTextField.setText(appointment.getTitle());
        AppointmentDescriptionTextField.setText(appointment.getDescription());
        AppointmentLocationTextField.setText(appointment.getLocation());
        ContactComboBox.getItems();
        for (Contact contact : ContactComboBox.getItems()) {
            if (contact.getContactID() == appointment.getContactID()) {
                ContactComboBox.getSelectionModel().select(contact);
                break;
            }
        }
        CustomerComboBox.getItems();
        for (Customer customer : CustomerComboBox.getItems()) {
            if (customer.getCustomerID() == appointment.getCustomerID()) {
                CustomerComboBox.getSelectionModel().select(customer);
                break;
            }
        }
        UserComboBox.getItems();
        for (User user : UserComboBox.getItems()) {
            if (user.getUserID() == appointment.getUserID()) {
                UserComboBox.getSelectionModel().select(user);
                break;
            }
        }
        AppointmentStartDatePicker.setValue(JDBC.toSystemDefault(appointment.getStartTime()).toLocalDate());
        AppointmentStartTimeComboBox.setValue(JDBC.toSystemDefault(appointment.getStartTime()).toLocalTime());
        AppointmentEndDatePicker.setValue(JDBC.toSystemDefault(appointment.getEndTime()).toLocalDate());
        AppointmentEndTimeComboBox.setValue(JDBC.toSystemDefault(appointment.getEndTime()).toLocalTime());
        AppointmentLabel.setText("Update Appointment");
    }

    /**event to collect all data in the datafields, compare them to logical checks, and then pass them as an arguement to the creation of AppointmentDAO's addappointment**/
    @FXML
    void OnActionSaveAppointment(ActionEvent event) throws IOException {
        try {
            String title = AppointmentTitleTextField.getText();
            String description = AppointmentDescriptionTextField.getText();
            String location = AppointmentLocationTextField.getText();
            String type = appointmentType;
            String stringStartTime = String.valueOf((AppointmentStartTimeComboBox.getSelectionModel().getSelectedItem()));
            if (stringStartTime == null) {
                JDBC.ErrorMessage("Input Error", "Start time not selected", "Please select a start time.");
                return;
            }
            LocalTime startTime = LocalTime.parse(stringStartTime);
            LocalDate startDate = AppointmentStartDatePicker.getValue();
            LocalDateTime startTimeAndDate = LocalDateTime.of(startDate, startTime);
            String stringEndTime = String.valueOf(AppointmentEndTimeComboBox.getSelectionModel().getSelectedItem());
            if (stringEndTime == null) {
                JDBC.ErrorMessage("Input Error", "End time not selected", "Please select a end time.");
                return;
            }
            LocalTime endTime = LocalTime.parse(stringEndTime);
            LocalDate endDate = AppointmentEndDatePicker.getValue();
            LocalDateTime endTimeAndDate = LocalDateTime.of(endDate, endTime);
            Contact contact = ContactComboBox.getSelectionModel().getSelectedItem();
            if (contact == null) {
                JDBC.ErrorMessage("Input Error", "Contact not selected", "Please select a contact.");
                return;
            }
            Customer customer = CustomerComboBox.getSelectionModel().getSelectedItem();
            if (customer == null) {
                JDBC.ErrorMessage("Input Error", "Customer not selected", "Please select a customer.");
                return;
            }
            User user = UserComboBox.getSelectionModel().getSelectedItem();
            if (user == null) {
                JDBC.ErrorMessage("Input Error", "User not selected", "Please select a user.");
                return;
            }
            int contactID = JDBC.getIdByColumnValue("contacts", "contact_id", "contact_name", ContactComboBox.getSelectionModel().getSelectedItem().toString());
            int customerID = JDBC.getIdByColumnValue("customers", "customer_id", "customer_name", CustomerComboBox.getSelectionModel().getSelectedItem().toString());
            int userID = JDBC.getIdByColumnValue("users", "user_id", "username", UserComboBox.getSelectionModel().getSelectedItem().toString());

            /**query start after end**/
            if (startTimeAndDate.isAfter(endTimeAndDate)) {
                JDBC.ErrorMessage("Time Error", "Appointment Start Time is After End Time", "Please change your end time and date to be after the start time and date.");
                return;
            }
            /**query past times**/
            if (startTimeAndDate.isBefore(LocalDateTime.now()) || endTimeAndDate.isBefore(LocalDateTime.now())){
                JDBC.ErrorMessage("Time Error", "Appointment Start Time or End Time is before current time", " Please choose a time in the future for both start and end times.");
                return;
            }
            /**query business hours**/
            LocalTime estStartTime = JDBC.convertToEST(startTime);
            LocalTime estEndTime = JDBC.convertToEST(endTime);
            if ((estStartTime.getHour() > 22)
                || estStartTime.getHour() < 8
                || estEndTime.getHour() > 22
                || estEndTime.getHour() < 8) {
                JDBC.ErrorMessage("Time Error", "Appointment Start Time or End Time Outside of Business Hours", "Please choose a time with our office hours of 8:00 a.m. to 22:00 p.m. ET");
                return;
            }
            LocalDateTime utcStartTime = JDBC.toUTC(startTimeAndDate);
            LocalDateTime utcEndTime = JDBC.toUTC(endTimeAndDate);
            System.out.println(utcStartTime);
            /**query if overlapping**/
            for (Appointment appointment : AppointmentDAO.getTypedAppointments("All")){
                if (appointment.getCustomerID() == customer.getCustomerID()) {
                    LocalDateTime start = JDBC.toUTC(appointment.getStartTime());
                    LocalDateTime end = JDBC.toUTC(appointment.getEndTime());
                    if ((start.isBefore(utcEndTime) || start.isEqual(utcEndTime)) && end.isAfter(utcStartTime)) {
                        JDBC.ErrorMessage("Time Error", "Appointment Overlap", "Please choose a time for an appointment that does not overlap with another appointment.");
                        return;
                    }
                }
            }
            /**query if appointment too long, because no one wants to be in an appointment for more than 8 hours**/
            if (endTimeAndDate.isAfter(startTimeAndDate.plusHours(8))) {
                JDBC.ErrorMessage("Time Error", "Appointment Is Too Long", "Please choose a duration for the appointment shorter than 8 hours.");
                return;
            }
            String CreatedBy = LoginController.UserLoggedIn();
            String UpdatedBy = LoginController.UserLoggedIn();
            int appointmentID;
            if (AppointmentIDTextField.getText().isEmpty()) {
                appointmentID = AppointmentDAO.appointmentGenerateID();
                AppointmentIDTextField.setText(String.valueOf(appointmentID));
                AppointmentDAO.addAppointment(appointmentID, title, description, location, type, startTimeAndDate, endTimeAndDate, CreatedBy, customerID, userID, contactID);
            } else{
                appointmentID = Integer.parseInt(AppointmentIDTextField.getText());
                AppointmentDAO.updateAppointment(appointmentID, title, description, location, type, startTimeAndDate, endTimeAndDate, UpdatedBy, customerID, userID, contactID);
            }
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
            stage.setScene(new Scene(scene));
        } catch (NumberFormatException e){
            JDBC.ErrorMessage("Input Error", "Error in adding customer due to incorrect input", "Please enter valid values for each text field.");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            observableContactList = FXCollections.observableArrayList(ContactDAO.getAllContacts());
            observableUserList = FXCollections.observableArrayList(UserDAO.getAllUsers());
            observableCustomerList = FXCollections.observableArrayList(CustomerDAO.getAllCustomers());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ContactComboBox.setItems(observableContactList);
        UserComboBox.setItems(observableUserList);
        CustomerComboBox.setItems(observableCustomerList);
        /**populate times in comboboxes**/
        String timeslots[] =
                {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        ComboBox timeComboBox = new ComboBox(FXCollections.observableArrayList(timeslots));
        AppointmentStartTimeComboBox.getItems().clear();
        AppointmentStartTimeComboBox.getItems().addAll();
        AppointmentStartTimeComboBox.setItems(timeComboBox.getItems());
        AppointmentEndTimeComboBox.setItems(timeComboBox.getItems());
    }
}