package model;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private int AppointmentID, CustomerID, UserID, ContactID;
    private String Title, Description, Location, Type, CreatedBy, LastUpdatedBy;
    private LocalDateTime StartTime, EndTime, CreateTime, UpdateTime;

    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createTime, String createdBy,
                       LocalDateTime updateTime, String lastUpdatedBy, int customerID, int userID, int contactID) {
        this.AppointmentID = appointmentID;
        this.CustomerID = customerID;
        this.UserID = userID;
        this.ContactID = contactID;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.CreateTime = createTime;
        this.CreatedBy = createdBy;
        this.UpdateTime = updateTime;
        this.LastUpdatedBy = lastUpdatedBy;
    }
    public int getAppointmentID() {
        return AppointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        AppointmentID = appointmentID;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return LastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        LastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        StartTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        EndTime = endTime;
    }

    public LocalDateTime getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        CreateTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        UpdateTime = updateTime;
    }
}
