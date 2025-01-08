package controller;
import dao.AppointmentDAO;
import dao.UserDAO;
import helper.JDBC;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
/** LoginController is the main of this program */

public class LoginController extends Application implements Initializable {
    Stage stage;
    Parent scene;
    UserDAO userDAO = new UserDAO();
    private static String UserLoggedIn;
    private static int UserIDLoggedIn;
    @FXML
    private Label SetCurrentLocationLabel, UsernameLabel, PasswordLabel, CurrentLocationLabel;
    @FXML
    private Button LoginButton, ExitButton;
    @FXML
    private TextField UserNameTextField, PasswordTextField;
    @FXML
    /** exit application */
    void OnActionExit(ActionEvent event) {
        JDBC.closeConnection();
        System.exit(0);
    }
    boolean isNewUser = false;
    /** resourcebundle data and getting user's locale for french/english translation*/
    Locale locale = Locale.getDefault();
    ResourceBundle rb = ResourceBundle.getBundle("Lang", locale);
    /** login event, containing logic like:
     * check if username or password are empty.
     * iteration over sql query user list and compare with username and password text fields.
     * <p>a lambda function to convert dates to the user's timezone, to then be compared against appointment times starting in 15 minutes:</p>
     * <p>JDBC.LocalDateTimeConverter localDateTimeToUserZone = (LocalDateTime time) -> {</p>
     * <p>ZonedDateTime utcZonedDateTime = time.atZone(ZoneId.of("UTC"));</p>
     * <p>ZoneId userZoneId = ZoneId.systemDefault();</p>
     * <p>ZonedDateTime zonedTime = utcZonedDateTime.withZoneSameInstant(userZoneId);</p>
     * <p>return zonedTime.toLocalDateTime();</p>
     * <p>};</p>
     */
    @FXML
    public void OnActionLogin(ActionEvent event) throws IOException, SQLException {
        if (UserNameTextField.getText() == null || PasswordTextField.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("EmptyFieldTitle"));
            alert.setHeaderText(rb.getString("EmptyFieldHeader"));
            alert.setContentText(rb.getString("EmptyFieldContent"));
            alert.showAndWait();
        } else {
            boolean isAuthenticated = false;
            JDBC.openConnection();
            userDAO.getAllUsers();
            String UsernameEntered = UserNameTextField.getText();
            String PasswordEntered = PasswordTextField.getText();
            if (userDAO.getAllUsers().isEmpty()){
                int userID = UserDAO.userGenerateID();
                UserLoggedIn = UsernameEntered;
                userDAO.addUser(userID, UsernameEntered, PasswordEntered, UserLoggedIn);
                isAuthenticated = true;
                String filename = "login_activity.txt", item;
                FileWriter fwriter = new FileWriter(filename, true);
                PrintWriter outputFile = new PrintWriter(fwriter);
                item = "Successful Login and First User Creation: " + UsernameEntered + " on " + LocalDate.now() + " at " + LocalTime.now();
                outputFile.println(item);
                outputFile.close();
            } else {
                for (User user : userDAO.getAllUsers()) {
                    String Username = user.getUsername();
                    String Password = user.getPassword();
                    if (UsernameEntered.equals(Username) && PasswordEntered.equals(Password)) {
                        UserLoggedIn = Username;
                        isAuthenticated = true;
                        String filename = "login_activity.txt", item;
                        FileWriter fwriter = new FileWriter(filename, true);
                        PrintWriter outputFile = new PrintWriter(fwriter);
                        item = "Successful Login: " + Username + " on " + LocalDate.now() + " at " + LocalTime.now();
                        outputFile.println(item);
                        outputFile.close();
                    }
                }
            }
            if (isAuthenticated) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("CustomersAppointments.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                try {
                    boolean AppointmentSoon = false;
                    JDBC.LocalDateTimeConverter localDateTimeToUserZone = (LocalDateTime time) -> {
                        ZonedDateTime utcZonedDateTime = time.atZone(ZoneId.of("UTC"));
                        ZoneId userZoneId = ZoneId.systemDefault();
                        ZonedDateTime zonedTime = utcZonedDateTime.withZoneSameInstant(userZoneId);
                        return zonedTime.toLocalDateTime();
                    };
                    for (Appointment appointment : AppointmentDAO.getUserAppointments(LoginController.UserIDLoggedIn())) {
                        if (appointment.getStartTime().isAfter(JDBC.convertToUTC(LocalDateTime.now(), ZoneId.systemDefault()))
                                && appointment.getStartTime().isBefore(JDBC.convertToUTC(LocalDateTime.now().plusMinutes(15), ZoneId.systemDefault()))) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Upcoming Appointments");
                            alert.setHeaderText("You have an appointment soon. ID: " + appointment.getAppointmentID() + ", on " + LocalDate.from(localDateTimeToUserZone.convert(appointment.getStartTime())) + ", at " + LocalTime.from(localDateTimeToUserZone.convert(appointment.getStartTime())));
                            alert.showAndWait();
                            AppointmentSoon = true;
                            break;
                        }
                    }
                    /**no appointments found alert*/
                    if (!AppointmentSoon){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Upcoming Appointments");
                        alert.setHeaderText("You have no upcoming appointments.");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                /**failed login, in french or english, and records failed login in the login activity file*/
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Title"));
                alert.setHeaderText(rb.getString("Header"));
                alert.setContentText(rb.getString("Content"));
                alert.showAndWait();
                String filename = "login_activity.txt", item;
                FileWriter fwriter = new FileWriter(filename, true);
                PrintWriter outputFile = new PrintWriter(fwriter);
                item = "Failed Login: " + UsernameEntered + " on " + LocalDate.now() + " at " + LocalTime.now();
                outputFile.println(item);
                outputFile.close();
            }
        }
    }
    /**userloggedin method for "createdby" and "updatedby" in other files*/
    public static String UserLoggedIn(){
        System.out.println(UserLoggedIn);
        return UserLoggedIn;
    }
    public static int UserIDLoggedIn() throws SQLException {
        String sql = "SELECT user_id FROM users WHERE user_name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, UserLoggedIn);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            UserIDLoggedIn = rs.getInt("user_id");
            return UserIDLoggedIn;
        } else {
            throw new SQLException("User not found");
        }
    }
    /**loads the scene's fxml data*/
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**changes location on screen to the user's zone*/
    private void setLocationLabel() {
        ZoneId zoneId = ZoneId.systemDefault();
       SetCurrentLocationLabel.setText(zoneId.toString());
    }
    /**initialize sets labels to resourcebundle data*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();
        try {
            if (userDAO.getAllUsers().isEmpty()) {
                isNewUser = true;
                UsernameLabel.setText(rb.getString("FirstUsername"));
                PasswordLabel.setText(rb.getString("FirstPassword"));
                CurrentLocationLabel.setText(rb.getString("CurrentLocation"));
                LoginButton.setText(rb.getString("CreateUser"));
                ExitButton.setText(rb.getString("Exit"));
                setLocationLabel();
            } else {
                isNewUser = false;
                UsernameLabel.setText(rb.getString("Username"));
                PasswordLabel.setText(rb.getString("Password"));
                CurrentLocationLabel.setText(rb.getString("CurrentLocation"));
                LoginButton.setText(rb.getString("Login"));
                ExitButton.setText(rb.getString("Exit"));
                setLocationLabel();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){
        launch(args);
        JDBC.openConnection();
    }
}