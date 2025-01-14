package dao;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AppointmentDAOImpl implements AppointmentDAO{
    private List<Appointment> appointment;
    /**collect the data on all appointments in the database**/
    public static void loadAllAppointments() throws SQLException {
        allAppointments.clear();
        String sql = "SELECT * FROM appointments;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int AppointmentID = rs.getInt("Appointment_ID");
            String Title = rs.getString("Title");
            String Description = rs.getString("Description");
            String Location = rs.getString("Location");
            String Type = rs.getString("Type");
            Timestamp startTS = rs.getTimestamp("Start");
            ZonedDateTime Start = JDBC.TimeStampToUserZone(startTS);
            Timestamp EndTS = rs.getTimestamp("End");
            ZonedDateTime End = JDBC.TimeStampToUserZone(EndTS);
            Timestamp CreateTimeTS = rs.getTimestamp("Create_Date");
            LocalDateTime CreateTime = null;
            if (CreateTimeTS != null) {
                ZonedDateTime tempCreateTime = JDBC.TimeStampToUserZone(CreateTimeTS);
                CreateTime = tempCreateTime.toLocalDateTime();
            }
            String CreatedBy = rs.getString("Created_By");
            Timestamp UpdateTimeTS = rs.getTimestamp("Last_Update");
            LocalDateTime UpdateTime = null;
            if (UpdateTimeTS != null) {
                ZonedDateTime tempUpdateTime = JDBC.TimeStampToUserZone(UpdateTimeTS);
                UpdateTime = tempUpdateTime.toLocalDateTime();
            }
            String UpdatedBy = rs.getString("Last_Updated_By");
            int CustomerID = rs.getInt("Customer_ID");
            int UserID = rs.getInt("User_ID");
            int ContactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(AppointmentID, Title, Description, Location, Type, Start.toLocalDateTime(), End.toLocalDateTime(),
                    CreateTime, CreatedBy, UpdateTime, UpdatedBy, CustomerID, UserID, ContactID);
            allAppointments.add(appointment);
        }
        ps.close();
        rs.close();
    }
    /**filtered appointment list**/
    public static ObservableList<Appointment> getAllAppointments(int timeQuery) {
        if (timeQuery <= 0) {
            return allAppointments;
        } else {
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
            for (Appointment appointment : allAppointments) {
                LocalDateTime startTime = appointment.getStartTime();
                if (startTime.isAfter(LocalDateTime.now()) && startTime.isBefore(LocalDateTime.now().plusDays(timeQuery))){
                    filteredAppointments.add(appointment);
                }
            }
            return filteredAppointments;
        }
    }
    public AppointmentDAOImpl() throws SQLException {
        appointment = new ArrayList<>();
        loadAllAppointments();
    }
    /**generate a unique ID**/
    public static int AppointmentGenerateID(){
        int maxNumber = 0;
        for (Appointment appointment : allAppointments){
            if (appointment.getAppointmentID() > maxNumber){
                maxNumber = appointment.getAppointmentID();
            }
        }
        int id = maxNumber+1;
        return id;
    }
}