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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    /**scene elements**/
    Stage stage;
    Parent scene;
    @FXML
    private TextField AddAppointmentCustomerID, AddAppointmentDescription, AddAppointmentLocation, AddAppointmentTitle,
            AddAppointmentType, AddAppointmentUserID, AppointmentID;
    @FXML
    private DatePicker AddAppointmentEndDate, AddAppointmentStartDate;
    @FXML
    private ComboBox<LocalTime> AddAppointmentEndTimeComboBox, AddAppointmentStartTimeComboBox;
    @FXML
    private ComboBox<Contact> AddContactComboBox;
    /**event to open the customerappointments scene**/
    @FXML
    void OnActionCustomerAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**event to collect all data in the datafields, compare them to logical checks, and then pass them as an arguement to the creation of AppointmentDAO's addappointment**/
    @FXML
    void OnActionSaveNewAppointment(ActionEvent event) throws IOException {
        try {
            int appointmentID = AppointmentDAOImpl.AppointmentGenerateID();
            AppointmentID.setText(String.valueOf(appointmentID));
            String title = AddAppointmentTitle.getText();
            String description = AddAppointmentDescription.getText();
            String location = AddAppointmentLocation.getText();
            String type = AddAppointmentType.getText();
            String stringStartTime = String.valueOf((AddAppointmentStartTimeComboBox.getSelectionModel().getSelectedItem()));
            if (stringStartTime == null) {
                JDBC.ErrorMessage("Input Error", "Start time not selected", "Please select a start time.");
                return;
            }
            LocalTime startTime = LocalTime.parse(stringStartTime);
            LocalDate startDate = AddAppointmentStartDate.getValue();
            LocalDateTime startTimeAndDate = LocalDateTime.of(startDate, startTime);
            String stringEndTime = String.valueOf(AddAppointmentEndTimeComboBox.getSelectionModel().getSelectedItem());
            if (stringEndTime == null) {
                JDBC.ErrorMessage("Input Error", "End time not selected", "Please select a end time.");
                return;
            }
            LocalTime endTime = LocalTime.parse(stringEndTime);
            LocalDate endDate = AddAppointmentEndDate.getValue();
            LocalDateTime endTimeAndDate = LocalDateTime.of(endDate, endTime);
            int customerID = Integer.parseInt(AddAppointmentCustomerID.getText());
            int userID = Integer.parseInt(AddAppointmentUserID.getText());
            Contact contact = AddContactComboBox.getSelectionModel().getSelectedItem();
            if (contact == null) {
                JDBC.ErrorMessage("Input Error", "Contact not selected", "Please select a contact.");
                return;
            }
            JDBC.openConnection();
            String sql = "SELECT contact_id FROM contacts WHERE contact_name = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, AddContactComboBox.getSelectionModel().getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            int contactID = -1;
            if (rs.next()) {
                contactID = rs.getInt("contact_id");
            }
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
            LocalTime estStartTime = JDBC.convertToEST(startTime, ZoneId.systemDefault());
            LocalTime estEndTime = JDBC.convertToEST(endTime, ZoneId.systemDefault());
            if ((estStartTime.getHour() > 22)
            || estStartTime.getHour() < 8
            || estEndTime.getHour() > 22
            || estEndTime.getHour() < 8) {
                JDBC.ErrorMessage("Time Error", "Appointment Start Time or End Time Outside of Business Hours", "Please choose a time with our office hours of 8:00 a.m. to 22:00 p.m. ET");
                return;
            }
            LocalDateTime utcStartTime = JDBC.convertToUTC(startTimeAndDate, ZoneId.systemDefault());
            LocalDateTime utcEndTime = JDBC.convertToUTC(endTimeAndDate, ZoneId.systemDefault());
            System.out.println(utcStartTime);
            /**query if overlapping**/
            for (Appointment appointment : AppointmentDAO.getAllAppointments()) {
                if (appointment.getCustomerID() == customerID) {
                    LocalDateTime start = JDBC.convertToUTC(appointment.getStartTime(), ZoneId.systemDefault());
                    LocalDateTime end = JDBC.convertToUTC(appointment.getEndTime(), ZoneId.systemDefault());
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
            AppointmentDAO.addAppointment(appointmentID, title, description, location, type, startTimeAndDate, endTimeAndDate, CreatedBy, customerID, userID, contactID);
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
            stage.setScene(new Scene(scene));
            //add errors for 1. contact drop down 2. if start time > end time
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
            ContactDAOImpl.loadAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Contact> contacts = ContactDAOImpl.getAllContacts();
        AddContactComboBox.setItems(contacts);
        /**populate times in comboboxes**/
        String timeslots[] =
                {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(timeslots));
        AddAppointmentStartTimeComboBox.getItems().clear();
        AddAppointmentStartTimeComboBox.getItems().addAll();
        AddAppointmentStartTimeComboBox.setItems(combo_box.getItems());
        AddAppointmentEndTimeComboBox.setItems(combo_box.getItems());
    }
}