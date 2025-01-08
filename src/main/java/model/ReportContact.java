package model;

import java.time.LocalDateTime;

public class ReportContact {
    /**honestly I made this class because it was easier to populate the in the reports controller, same with reportcustomer and typemonthmatch. I have learned with this class that I do not like javafx. **/
    private int appointmentID, customerID, userID;
    private String title, type, description, location;
    private LocalDateTime start, end;
    public ReportContact(int appointmentID, String title, String type, String description, String location, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.type = type;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
