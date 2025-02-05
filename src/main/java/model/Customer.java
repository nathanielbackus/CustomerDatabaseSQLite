package model;

import java.time.LocalDateTime;

public class Customer {
    private int CustomerID, DivisionID;
    private String CustomerName, Address, PostalCode, Phone;
    public Customer(int CustomerID, String CustomerName, String Address, String PostalCode,
                    String Phone, int DivisionID){

        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.Address = Address;
        this.PostalCode = PostalCode;
        this.Phone = Phone;
        this.DivisionID = DivisionID;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public int getDivisionID() {
        return DivisionID;
    }

    public void setDivisionID(int divisionID) {
        DivisionID = divisionID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @Override
    public String toString() {
        return this.getCustomerName();
    }
}
