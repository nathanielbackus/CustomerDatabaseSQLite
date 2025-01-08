package model;

public class ReportCustomer {
    private String customerID;
    private int appointmentCount;
    public ReportCustomer(String customerID, int appointmentCount){
        this.customerID = customerID;
        this.appointmentCount = appointmentCount;
    }
    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = this.customerID;
    }
    public int getAppointmentCount() {
        return appointmentCount;
    }
    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }
}
