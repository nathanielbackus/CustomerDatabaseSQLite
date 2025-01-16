package dao;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.List;
public interface AppointmentDAO {
    /**list of all appointments, will populate this list with data from the database to then populate tabelviews**/
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static List<Appointment> getTimeQueryAppointments() {
        return allAppointments;
    }
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
            Timestamp CreateTimeTS = rs.getTimestamp("CreatedOn");
            LocalDateTime CreateTime = null;
            if (CreateTimeTS != null) {
                ZonedDateTime tempCreateTime = JDBC.TimeStampToUserZone(CreateTimeTS);
                CreateTime = tempCreateTime.toLocalDateTime();
            }
            String CreatedBy = rs.getString("CreatedBy");
            Timestamp UpdateTimeTS = rs.getTimestamp("updatedOn");
            LocalDateTime UpdateTime = null;
            if (UpdateTimeTS != null) {
                ZonedDateTime tempUpdateTime = JDBC.TimeStampToUserZone(UpdateTimeTS);
                UpdateTime = tempUpdateTime.toLocalDateTime();
            }
            String UpdatedBy = rs.getString("updatedBy");
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

    public static ObservableList<Appointment> getTimeQueryAppointments(int timeQuery) {
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

    /**sql query to get all matching appointment month and type data**/
    public static ObservableList<TypeMonthMatch> getTypeMonthAppointments() {
        ObservableList<TypeMonthMatch> combinedAppointments = FXCollections.observableArrayList();
        try {
//            JDBC.openConnection();
            String sql = "SELECT type AS appointmentType, month(start) AS appointmentMonth, count(1) AS totalCount FROM client_schedule.appointments GROUP BY type, month(start);";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String Type = rs.getString("appointmentType");
                String Month = rs.getString("appointmentMonth");
                int Count = Integer.parseInt(rs.getString("totalCount"));
                combinedAppointments.add(new TypeMonthMatch(Type, Month, Count));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return combinedAppointments;
    }
    /**sql query to collect data on appointments belonging to specified contact**/
    public static ObservableList<ReportContact> getContactAppointments(int contactID) {
        ObservableList<ReportContact> contactAppointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointment_id, title, description, location, type, start, end, customer_id, user_id FROM client_schedule.appointments WHERE contact_id = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointmentID = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                String description = rs.getString("description");
                String location = rs.getString("location");
                Timestamp startTS = rs.getTimestamp("Start");
                ZonedDateTime start = JDBC.TimeStampToUserZone(startTS);
                Timestamp EndTS = rs.getTimestamp("End");
                ZonedDateTime end = JDBC.TimeStampToUserZone(EndTS);
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                contactAppointments.add(new ReportContact(appointmentID, title, type, description, location, start.toLocalDateTime(), end.toLocalDateTime(), customerID, userID));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contactAppointments;
    }
    /**3rd report with the purpose of finding which customers have the most outstanding appointments**/
    public static ObservableList<ReportCustomer> getCustomerAppointmentTotal(){
        ObservableList<ReportCustomer> customerAppointmentTotal = FXCollections.observableArrayList();
        try {
            String sql = "SELECT customer_id, COUNT(1) FROM appointments GROUP BY customer_id;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String customerID = rs.getString("customer_id");
                int appointmentTotal = rs.getInt(2);
                customerAppointmentTotal.add(new ReportCustomer(customerID, appointmentTotal));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerAppointmentTotal;
    }
    /**collects data from the database on a specific user**/
    public static ObservableList<Appointment> getUserAppointments(int userId) {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM Appointments WHERE User_ID  = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int AppointmentID = rs.getInt("Appointment_ID");
                String Title = rs.getString("Title");
                String Description = rs.getString("Description");
                String Location = rs.getString("Location");
                String Type = rs.getString("Type");
                LocalDateTime Start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime End = rs.getTimestamp("End").toLocalDateTime();
                Timestamp CreateTimeTS = rs.getTimestamp("CreatedOn");
                LocalDateTime CreateTime = null;
                if (CreateTimeTS != null) {
                    ZonedDateTime tempCreateTime = JDBC.TimeStampToUserZone(CreateTimeTS);
                    CreateTime = tempCreateTime.toLocalDateTime();
                }
                String CreatedBy = rs.getString("CreatedBy");
                Timestamp UpdatedTimeTS = rs.getTimestamp("UpdateOn");
                LocalDateTime UpdatedTime = null;
                if (UpdatedTimeTS != null) {
                    ZonedDateTime tempUpdateTime = JDBC.TimeStampToUserZone(UpdatedTimeTS);
                    UpdatedTime = tempUpdateTime.toLocalDateTime();
                }
                String LastUpdatedBy = rs.getString("UpdatedBy");
                int CustomerID = rs.getInt("Customer_ID");
                int UserID = rs.getInt("User_ID");
                int ContactID = rs.getInt("Contact_ID");
                Appointment results = new Appointment(AppointmentID, Title, Description, Location,
                        Type, Start, End, CreateTime, CreatedBy, UpdatedTime, LastUpdatedBy, CustomerID, UserID, ContactID);
                userAppointments.add(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAppointments;
    }
    /**sql insert, to add an appointment, with createdby**/
    public static int addAppointment(int AppointmentID, String Title, String Description, String Location, String Type, LocalDateTime Start, LocalDateTime End, String CreatedBy, int CustomerID, int UserID, int ContactID) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, CreatedOn, CreatedBy, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, AppointmentID);
        ps.setString(2, Title);
        ps.setString(3, Description);
        ps.setString(4, Location);
        ps.setString(5, Type);
        ZoneId userZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime zonedStart = ZonedDateTime.of(Start, userZoneId);
        ZonedDateTime utcStart = ZonedDateTime.ofInstant(zonedStart.toInstant(), utcZoneId);
        ZonedDateTime zonedEnd = ZonedDateTime.of(End, userZoneId);
        ZonedDateTime utcEnd = ZonedDateTime.ofInstant(zonedEnd.toInstant(), utcZoneId);
        ps.setString(6, String.valueOf(utcStart.toLocalDateTime()));
        ps.setTimestamp(7, Timestamp.valueOf(utcEnd.toLocalDateTime()));
        ps.setString(8, CreatedBy);
        ps.setInt(9, CustomerID);
        ps.setInt(10, UserID);
        ps.setInt(11, ContactID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**sql update to update an appointment with new data and with updateby information**/
    public static int updateAppointment(int AppointmentID, String Title, String Description, String Location,
                                        String Type, LocalDateTime Start, LocalDateTime End, String UpdatedBy, int
                                                CustomerID, int UserID, int ContactID) throws SQLException {
        String sql = "UPDATE appointments SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, " +
                "updatedOn = CURRENT_TIMESTAMP, updatedBy = ?, customer_id = ?, user_id = ?, contact_id = ? " +
                "WHERE appointment_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Title);
        ps.setString(2, Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ZoneId userZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime zonedStart = ZonedDateTime.of(Start, userZoneId);
        ZonedDateTime utcStart = ZonedDateTime.ofInstant(zonedStart.toInstant(), utcZoneId);
        ZonedDateTime zonedEnd = ZonedDateTime.of(End, userZoneId);
        ZonedDateTime utcEnd = ZonedDateTime.ofInstant(zonedEnd.toInstant(), utcZoneId);
        ps.setTimestamp(5, Timestamp.valueOf(utcStart.toLocalDateTime()));
        ps.setTimestamp(6, Timestamp.valueOf(utcEnd.toLocalDateTime()));
        ps.setString(7, UpdatedBy);
        ps.setInt(8, CustomerID);
        ps.setInt(9, UserID);
        ps.setInt(10, ContactID);
        ps.setInt(11, AppointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**sql to delete appointment from database**/
    public static boolean deleteAppointment(Appointment selectedAppointment) throws SQLException {
        if (selectedAppointment != null && AppointmentDAO.allAppointments.contains(selectedAppointment)) {
            int appointmentID = selectedAppointment.getAppointmentID();
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointmentID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JDBC.InformationMessage("Appointment Deleted", "ID: " + selectedAppointment.getAppointmentID() + " Type: " + selectedAppointment.getType());
                allAppointments.remove(selectedAppointment);
                return true;
            }
        }
        return false;
    }
    public static int appointmentGenerateID() throws SQLException {
        int maxNumber = 0;
        for (Appointment appointment: getTimeQueryAppointments(0)){
            if (appointment.getAppointmentID() > maxNumber){
                maxNumber = appointment.getAppointmentID();
            }
        }
        int id = maxNumber+1;
        return id;
    }
}
